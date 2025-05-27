package client.startup;

import client.modelManager.ModelManager;
import client.modelManager.ModelManagerImpl;
import client.networking.SocketService;
import client.networking.authentication.SocketAuthenticationClient;
import client.networking.bet.SocketBetClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.SocketRaceClient;

import java.io.IOException;

/**
 * Initializes the client application components.
 *
 * Sets up the networking layer and creates an instance of the ModelManager
 * that the rest of the application can use to access and modify state.
 */
public class ClientAppInitializer {

    private final ModelManager modelManager;
    private final SocketService socketService;

    /**
     * Constructs the application initializer and establishes a socket connection.
     *
     * @param host the hostname or IP address of the server
     * @param port the port number to connect to
     * @throws IOException if the socket connection fails
     */
    public ClientAppInitializer(String host, int port) throws IOException {
        socketService = new SocketService(host, port);

        var authClient = new SocketAuthenticationClient(socketService);
        var horsesClient = new SocketHorsesClient(socketService);
        var raceClient = new SocketRaceClient(socketService);
        var betClient = new SocketBetClient(socketService);

        modelManager = new ModelManagerImpl(authClient, raceClient, horsesClient, betClient, socketService);
    }

    /**
     * Returns the initialized ModelManager instance.
     *
     * @return the model manager
     */
    public ModelManager getModelManager() {
        return modelManager;
    }

    /**
     * Returns the initialized SocketService instance.
     *
     * @return the socket service
     */
    public SocketService getSocketService() {
        return socketService;
    }
}
