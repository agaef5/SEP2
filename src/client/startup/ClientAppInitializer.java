package client.startup;

import client.modelManager.ModelManager;
import client.modelManager.ModelManagerImpl;
import client.networking.SocketService;
import client.networking.authentication.SocketAuthenticationClient;
import client.networking.bet.SocketBetClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.SocketRaceClient;

import java.io.IOException;

public class ClientAppInitializer {
    private final ModelManager modelManager;
    private final SocketService socketService;

    public ClientAppInitializer(String host, int port) throws IOException {
        socketService = new SocketService(host, port);

        var authClient = new SocketAuthenticationClient(socketService);
        var horsesClient = new SocketHorsesClient(socketService);
        var raceClient = new SocketRaceClient(socketService);
        var betClient = new SocketBetClient(socketService);

        modelManager = new ModelManagerImpl(authClient, raceClient, horsesClient, betClient, socketService);
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    public SocketService getSocketService() {
        return socketService;
    }
}
