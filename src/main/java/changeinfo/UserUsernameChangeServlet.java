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

@WebServlet("/change_data/updateUsername")
public class UserUsernameChangeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/registration_form";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final String SELECT_USERNAME = "SELECT username FROM users WHERE username = ?";
    private static final String SELECT_PASSWORD = "SELECT password FROM users WHERE username = ?";
    private static final String UPDATE_USERNAME = "UPDATE users SET username = ? WHERE username = ?";

    private static final String STATUS = "status";
    private static final String FAILURE_USERNAME_TAKEN = "failure_username_taken";
    private static final String FAILURE_WRONG_PASSWORD = "failure_wrong_password";
    private static final String SUCCESS = "success";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newUsername = request.getParameter("newUsername");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        String oldUsername = (String) session.getAttribute("name");

        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (isUsernameTaken(connection, newUsername)) {
                request.setAttribute(STATUS, FAILURE_USERNAME_TAKEN);
            } else {
                if (!isPasswordCorrect(connection, oldUsername, password)) {
                    request.setAttribute(STATUS, FAILURE_WRONG_PASSWORD);
                } else {
                    if (updateUsername(connection, oldUsername, newUsername)) {
                        request.setAttribute(STATUS, SUCCESS);
                        session.setAttribute("name", newUsername); 
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

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("change_username.jsp");
        requestDispatcher.forward(request, response);
    }

    private boolean isUsernameTaken(Connection connection, String username) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERNAME)) {
            preparedStatement.setString(1, username);
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

    private boolean updateUsername(Connection connection, String oldUsername, String newUsername) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERNAME)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
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
}
