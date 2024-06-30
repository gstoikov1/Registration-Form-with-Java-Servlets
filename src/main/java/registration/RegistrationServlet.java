package registration;

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

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/registration_form";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final String SELECT_USER_BY_EMAIL_OR_USERNAME = "SELECT * FROM users WHERE email = ? OR username = ?";
    private static final String SELECT_USERNAME = "SELECT username FROM users WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO users(username, password, email, age) VALUES(?, ?, ?, ?)";

    private static final String REGISTER_JSP = "register.jsp";
    private static final String LOGIN_JSP = "login.jsp";
    private static final String ERROR_JSP = "error.jsp";

    private static final String STATUS = "status";
    private static final String ERROR_MESSAGE = "errorMessage";

    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String FAILURE_USERNAME_TAKEN = "failure_username_taken";
    private static final String FAILURE_EMAIL_TAKEN = "failure_email_taken";
    private static final String ERROR = "error";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int age = Integer.parseInt(request.getParameter("age"));

        RequestDispatcher requestDispatcher = null;
        Connection connection = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (userExists(connection, username, email)) {
                requestDispatcher = request.getRequestDispatcher(REGISTER_JSP);
                if (usernameExists(connection, username)) {
                    request.setAttribute(STATUS, FAILURE_USERNAME_TAKEN);
                } else {
                    request.setAttribute(STATUS, FAILURE_EMAIL_TAKEN);
                }
            } else {
                if (registerUser(connection, username, password, email, age)) {
                    request.setAttribute(STATUS, SUCCESS);
                    requestDispatcher = request.getRequestDispatcher(LOGIN_JSP);
                } else {
                    request.setAttribute(STATUS, FAILURE);
                    requestDispatcher = request.getRequestDispatcher(REGISTER_JSP);
                }
            }

            requestDispatcher.forward(request, response);

        } catch (ClassNotFoundException |  SQLException e) {
            handleError(request, response, e);
        } finally {
            closeConnection(connection);
        }
    }

    private boolean userExists(Connection connection, String username, String email) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_OR_USERNAME)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean usernameExists(Connection connection, String username) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERNAME)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean registerUser(Connection connection, String username, String password, String email, int age) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, age);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute(STATUS, ERROR);
        request.setAttribute(ERROR_MESSAGE, "An error occurred while processing your request: " + e.getMessage());
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_JSP);
        requestDispatcher.forward(request, response);
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
