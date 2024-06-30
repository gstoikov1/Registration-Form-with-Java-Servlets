package changeinfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/change_data/updateEmail")
public class UserEmailChangeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/registration_form";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final String SELECT_EMAIL = "SELECT username FROM users WHERE email = ?";
    private static final String SELECT_PASSWORD = "SELECT password FROM users WHERE email = ?";
    private static final String UPDATE_EMAIL = "UPDATE users SET email = ? WHERE email = ?";

    private static final String STATUS = "status";
    private static final String FAILURE_EMAIL_TAKEN = "failure_email_taken";
    private static final String FAILURE_WRONG_PASSWORD = "failure_wrong_password";
    private static final String SUCCESS = "success";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newEmail = request.getParameter("newEmail");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        String oldEmail = (String) session.getAttribute("email");

        Connection connection = null;
        try {
            
            connection = getConnection();

            if (isEmailTaken(connection, newEmail)) {
                request.setAttribute(STATUS, FAILURE_EMAIL_TAKEN);
            } else {
                if (!isPasswordCorrect(connection, oldEmail, password)) {
                    request.setAttribute(STATUS, FAILURE_WRONG_PASSWORD);
                } else {
                    if (updateEmail(connection, oldEmail, newEmail)) {
                        request.setAttribute(STATUS, SUCCESS);
                        session.setAttribute("email", newEmail); 
                    } else {
                        request.setAttribute(STATUS, "failure"); 

                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("change_email.jsp");
        requestDispatcher.forward(request, response);
    }

    private boolean isEmailTaken(Connection connection, String email) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMAIL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean isPasswordCorrect(Connection connection, String username, String password) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PASSWORD)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    return storedPassword.equals(password);
                }
            }
        }
        return false;
    }

    private boolean updateEmail(Connection connection, String oldEmail, String newEmail) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMAIL)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, oldEmail);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    protected Connection getConnection() throws SQLException, ClassNotFoundException {
    	Class.forName(JDBC_DRIVER);
    	return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
