package server.services.authentication;

import org.junit.jupiter.api.*;
import server.model.User;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import shared.DTO.UserDTO;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.user.BalanceUpdateRequest;
import shared.user.BalanceUpdateResponse;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceImplTest {

    private AuthServiceImpl authService;
    private final String TEST_USERNAME = "testUser";
    private final String TEST_EMAIL = "testUser@example.com";
    private final String TEST_PASSWORD = "password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
                "postgres", "1234"
        );
    }

    @BeforeAll
    public void setup() {
        authService = new AuthServiceImpl();
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM game_user WHERE username = ?");
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }
    }

    @Test
    public void testRegisterUser_success() {
        RegisterRequest request = new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        RegisterRespond response = authService.registerUser(request);

        assertEquals("success", response.message());
        assertNotNull(response.payload());
        UserDTO testDTO = (UserDTO) response.payload();
        assertEquals(TEST_USERNAME, testDTO.username());
    }

    @Test
    public void testRegisterUser_duplicate() {
        authService.registerUser(new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD));
        RegisterRespond response = authService.registerUser(new RegisterRequest(TEST_USERNAME, TEST_EMAIL, "anotherPass"));

        assertEquals("error", response.message());
        assertTrue(response.payload().toString().toLowerCase().contains("exists"));
    }

    @Test
    public void testLoginUser_success() {
        authService.registerUser(new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD));
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);
        LoginRespond response = authService.loginUser(loginRequest);

        assertEquals("success", response.message());
        assertNotNull(response.payload());
        UserDTO testDTO = (UserDTO) response.payload();
        assertEquals(TEST_USERNAME, testDTO.username());
    }

    @Test
    public void testLoginUser_wrongPassword() {
        authService.registerUser(new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD));
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, "wrongpass");
        LoginRespond response = authService.loginUser(loginRequest);

        assertEquals("error", response.message());
        assertTrue(response.payload().toString().toLowerCase().contains("not match"));
    }

    @Test
    public void testUpdateBalance_success() {
        authService.registerUser(new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD));

        BalanceUpdateRequest updateRequest = new BalanceUpdateRequest(TEST_USERNAME, 2000);
        BalanceUpdateResponse response = authService.updateBalance(updateRequest);

        assertEquals("success", response.message());
        UserDTO user = (UserDTO) response.payload();
        assertEquals(2000, user.balance());
    }
}
