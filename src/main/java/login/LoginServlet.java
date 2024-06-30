package login;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/registration_form?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final String SELECT_USER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";

    private static final String INDEX_JSP = "index.jsp";
    private static final String LOGIN_JSP = "login.jsp";

    private static final String STATUS = "status";
    private static final String WRONG_DATA = "wrong_data";

    private static final String NAME = "name";
    private static final String EMAIL = "email";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        RequestDispatcher requestDispatcher = null;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (authenticateUser(connection, email, password, session)) {
                requestDispatcher = request.getRequestDispatcher(INDEX_JSP);
            } else {
                request.setAttribute(STATUS, WRONG_DATA);
                requestDispatcher = request.getRequestDispatcher(LOGIN_JSP);
            }

            requestDispatcher.forward(request, response);

        } catch (ClassNotFoundException | SQLException e) {
            handleError(response, e);
        } finally {
            closeConnection(connection);
        }
    }

    private boolean authenticateUser(Connection connection, String email, String password, HttpSession session) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_AND_PASSWORD)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    session.setAttribute(NAME, resultSet.getString("username"));
                    session.setAttribute(EMAIL, email);
                    return true;
                }
            }
        }
        return false;
    }

    private void handleError(HttpServletResponse response, Exception e) throws IOException {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request: " + e.getMessage());
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
