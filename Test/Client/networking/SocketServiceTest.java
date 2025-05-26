package client.networking;

import client.modelManager.MessageListener;
import client.ui.util.ErrorHandler;
import client.ui.util.RespondValidate;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import server.networking.exceptions.InvalidMessageException;
import shared.Request;
import shared.Respond;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocketServiceTest {

    private TestServer testServer;
    private SocketService socketService;
    private MessageListener mockListener;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        gson = new Gson();
        mockListener = mock(MessageListener.class);

        // Start a test server
        testServer = new TestServer();
        testServer.start();

        // Wait a bit for server to start
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    void tearDown() {
        if (socketService != null) {
            socketService.disconnect();
        }
        if (testServer != null) {
            testServer.stop();
        }
    }

    @Test
    void constructor_WithValidHostAndPort_CreatesSocketService() throws IOException {
        // Act
        socketService = new SocketService("localhost", testServer.getPort());

        // Assert
        assertNotNull(socketService);
        assertTrue(socketService.isRunning());
    }

    @Test
    void constructor_WithInvalidPort_ThrowsIOException() {
        // Act & Assert
        assertThrows(IOException.class, () -> {
            new SocketService("localhost", 99999); // Invalid port
        });
    }

    @Test
    void addListener_WithValidListener_AddsListener() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());

        // Act
        socketService.addListener(mockListener);

        // Assert - No direct way to verify, but we can test through notification
        // This will be tested in the notifyListener tests
        assertDoesNotThrow(() -> socketService.addListener(mockListener));
    }

    @Test
    void sendRequest_WithValidRequest_SendsJsonToServer() throws IOException, InterruptedException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        Request testRequest = new Request("test", "action", gson.toJsonTree("payload"));

        // Act
        socketService.sendRequest(testRequest);

        // Give some time for the message to be sent
        Thread.sleep(100);

        // Assert
        String receivedMessage = testServer.getLastReceivedMessage();
        assertNotNull(receivedMessage);
        assertTrue(receivedMessage.contains("\"handler\":\"test\""));
        assertTrue(receivedMessage.contains("\"action\":\"action\""));
    }

    @Test
    void sendRequest_WithNullRequest_HandlesGracefully() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());

        // Act & Assert
        assertDoesNotThrow(() -> socketService.sendRequest(null));
    }

    @Test
    void receive_WithValidJsonResponse_NotifiesListeners() throws IOException, InterruptedException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        socketService.addListener(mockListener);

        Respond testRespond = new Respond("test", "payload");
        String jsonResponse = gson.toJson(testRespond);

        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mockListener).update(anyString(), anyString());

        try (MockedStatic<RespondValidate> mockedValidator = mockStatic(RespondValidate.class)) {
            mockedValidator.when(() -> RespondValidate.decode(any(Respond.class)))
                    .thenReturn(testRespond);

            // Act
            socketService.receive(jsonResponse);

            // Wait for async processing
            latch.await(1, TimeUnit.SECONDS);

            // Assert
            verify(mockListener).update("test", "payload");
        }
    }

    @Test
    void receive_WithInvalidJson_HandlesGracefully() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        socketService.addListener(mockListener);

        // Act & Assert
        assertDoesNotThrow(() -> socketService.receive("invalid json"));

        // Verify no listeners were notified
        verifyNoInteractions(mockListener);
    }

    @Test
    void receive_WithDisconnectType_DoesNotNotifyListeners() throws IOException, InterruptedException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        socketService.addListener(mockListener);

        Respond disconnectRespond = new Respond("disconnect", "goodbye");
        String jsonResponse = gson.toJson(disconnectRespond);

        try (MockedStatic<RespondValidate> mockedValidator = mockStatic(RespondValidate.class)) {
            mockedValidator.when(() -> RespondValidate.decode(any(Respond.class)))
                    .thenReturn(disconnectRespond);

            // Act
            socketService.receive(jsonResponse);

            Thread.sleep(100); // Give time for processing

            // Assert
            verifyNoInteractions(mockListener);
        }
    }

    @Test
    void receive_WithRespondValidateException_HandlesGracefully() throws IOException, InterruptedException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        socketService.addListener(mockListener);

        Respond testRespond = new Respond("test", "payload");
        String jsonResponse = gson.toJson(testRespond);

        try (MockedStatic<RespondValidate> mockedValidator = mockStatic(RespondValidate.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = mockStatic(ErrorHandler.class)) {

            mockedValidator.when(() -> RespondValidate.decode(any(Respond.class)))
                    .thenThrow(new InvalidMessageException("Invalid message"));

            // Act
            socketService.receive(jsonResponse);

            Thread.sleep(100); // Give time for processing

            // Assert
            mockedErrorHandler.verify(() ->
                    ErrorHandler.handleError(any(InvalidMessageException.class), eq("RespondValidate")));
            verifyNoInteractions(mockListener);
        }
    }

    @Test
    void isRunning_InitialState_ReturnsTrue() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());

        // Act & Assert
        assertTrue(socketService.isRunning());
    }

    @Test
    void disconnect_SetsRunningToFalse() throws IOException, InterruptedException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        assertTrue(socketService.isRunning());

        // Act
        socketService.disconnect();

        // Give some time for disconnect to process
        Thread.sleep(100);

        // Assert
        assertFalse(socketService.isRunning());
    }

    @Test
    void notifyListener_WithMultipleListeners_NotifiesAll() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        MessageListener mockListener2 = mock(MessageListener.class);

        socketService.addListener(mockListener);
        socketService.addListener(mockListener2);

        // Act
        socketService.notifyListener("test", "payload");

        // Assert
        verify(mockListener).update("test", "payload");
        verify(mockListener2).update("test", "payload");
    }

    @Test
    void removeListener_RemovesSpecificListener() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        MessageListener mockListener2 = mock(MessageListener.class);

        socketService.addListener(mockListener);
        socketService.addListener(mockListener2);

        // Act
        socketService.removeListener(mockListener);
        socketService.notifyListener("test", "payload");

        // Assert
        verifyNoInteractions(mockListener);
        verify(mockListener2).update("test", "payload");
    }

    @Test
    void removeAllListeners_RemovesAllListeners() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        MessageListener mockListener2 = mock(MessageListener.class);

        socketService.addListener(mockListener);
        socketService.addListener(mockListener2);

        // Act
        socketService.removeAllListeners();
        socketService.notifyListener("test", "payload");

        // Assert
        verifyNoInteractions(mockListener);
        verifyNoInteractions(mockListener2);
    }

    @Test
    void receive_WithObjectPayload_ConvertsToJsonString() throws IOException, InterruptedException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());
        socketService.addListener(mockListener);

        Object complexPayload = new TestPayload("test", 123);
        Respond testRespond = new Respond("test", complexPayload);
        String jsonResponse = gson.toJson(testRespond);

        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mockListener).update(anyString(), anyString());

        try (MockedStatic<RespondValidate> mockedValidator = mockStatic(RespondValidate.class)) {
            mockedValidator.when(() -> RespondValidate.decode(any(Respond.class)))
                    .thenReturn(testRespond);

            // Act
            socketService.receive(jsonResponse);

            // Wait for async processing
            latch.await(1, TimeUnit.SECONDS);

            // Assert
            verify(mockListener).update(eq("test"), contains("test"));
            verify(mockListener).update(eq("test"), contains("123"));
        }
    }

    @Test
    void sendRequest_WithIOException_HandlesGracefully() throws IOException {
        // Arrange
        socketService = new SocketService("localhost", testServer.getPort());

        // Stop the server to cause IO exception
        testServer.stop();

        Request testRequest = new Request("test", "action", gson.toJsonTree("payload"));

        try (MockedStatic<ErrorHandler> mockedErrorHandler = mockStatic(ErrorHandler.class)) {
            // Act
            socketService.sendRequest(testRequest);

            // Give some time for error handling
            Thread.sleep(100);

            // Assert
            mockedErrorHandler.verify(() ->
                    ErrorHandler.handleError(any(IOException.class), eq("SocketService")));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Helper classes for testing
    private static class TestServer {
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private BufferedReader in;
        private BufferedWriter out;
        private Thread serverThread;
        private volatile String lastReceivedMessage;
        private volatile boolean running;

        public void start() throws IOException {
            serverSocket = new ServerSocket(0); // Use any available port
            running = true;

            serverThread = new Thread(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String message;
                    while (running && (message = in.readLine()) != null) {
                        lastReceivedMessage = message;
                    }
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            });
            serverThread.start();
        }

        public void stop() {
            running = false;
            try {
                if (clientSocket != null) clientSocket.close();
                if (serverSocket != null) serverSocket.close();
                if (serverThread != null) serverThread.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int getPort() {
            return serverSocket.getLocalPort();
        }

        public String getLastReceivedMessage() {
            return lastReceivedMessage;
        }
    }

    private static class TestPayload {
        private final String name;
        private final int value;

        public TestPayload(String name, int value) {
            this.name = name;
            this.value = value;
        }

        // Getters needed for JSON serialization
        public String getName() { return name; }
        public int getValue() { return value; }
    }
}