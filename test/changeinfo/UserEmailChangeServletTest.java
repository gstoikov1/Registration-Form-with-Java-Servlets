package changeinfo;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEmailChangeServletTest {
    @InjectMocks
    private UserEmailChangeServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        servlet = new UserEmailChangeServlet() {
            @Override
            protected Connection getConnection() throws SQLException, ClassNotFoundException {
                return connection;
            }
        };
    }

    @Test
    public void testEmailTaken() throws Exception {
        when(request.getParameter("newEmail")).thenReturn("taken@example.com");
        when(request.getParameter("password")).thenReturn("password");
        when(session.getAttribute("email")).thenReturn("old@example.com");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); 

        servlet.doPost(request, response);

        verify(request).setAttribute("status", "failure_email_taken");
        verify(requestDispatcher).forward(request, response);
    }

    
}
