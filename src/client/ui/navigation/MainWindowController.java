package client.ui.navigation;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.networking.authentication.SocketAuthenticationClient;
import client.networking.horses.HorsesClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.RaceClient;
import client.networking.race.SocketRaceClient;
import client.ui.adminView.race.CreateRaceVM;
import client.ui.adminView.race.CreateRaceController;
import client.ui.authentication.login.LoginController;
import client.ui.authentication.login.LoginVM;
import client.ui.authentication.register.RegisterController;
import client.ui.authentication.register.RegisterVM;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.userView.landingPage.UserLandingPageController;
import client.ui.userView.landingPage.UserLandingPageVM;
import client.ui.util.ErrorHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {
    public StackPane mainPane;

    private Stage stage;

    private SocketService socketService;
    private AuthenticationClient authenticationClient;
    private HorsesClient horsesClient;
    private RaceClient raceClient;

    public void initialize(SocketService socketService, RegisterController registerController) {
        this.socketService = socketService;
        authenticationClient = new SocketAuthenticationClient(socketService);
        horsesClient = new SocketHorsesClient(socketService);
        raceClient = new SocketRaceClient(socketService);

        if (registerController != null) {
            registerController.setWindowController(this);
        }
        loadRegisterPage();

        mainPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            if (stage != null) {
                stage.sizeToScene();
            }
        });
    }

    private void loadPage(String fxmlFile) {
        try {
            mainPane.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
            Pane newContent = loader.load();

            ViewModel viewModel = null;
            Controller controller = loader.getController();
            controller.setWindowController(this);

            if (controller instanceof LoginController) {
                viewModel = new LoginVM(authenticationClient, socketService);
            }

            if (controller instanceof RegisterController)
            {
                viewModel = new RegisterVM(authenticationClient, socketService);
            }

            if (controller instanceof UserLandingPageController) {
                viewModel = new UserLandingPageVM(raceClient, socketService);
            }

            controller.initialize(viewModel);

            mainPane.getChildren().add(newContent);
        } catch (IOException e) {
            ErrorHandler.handleError(new IllegalArgumentException(e), "Error loading page");
        }
    }

    public void loadRegisterPage(){
        loadPage("client/ui/authentication/register/Register.fxml");
    }

    public void loadLoginPage(){
        loadPage("client/ui/authentication/login/Login.fxml");
    }

    public void loadUserLandingPage(){
        loadPage("client/ui/userView/landingPage/UserLandingPageController.java");
    }

    public void loadAdminPanel(){
        loadPage("client/ui/adminView/adminPanel/AdminPanel.fxml");
    }

    public void loadHorsePage(){
        loadPage("client/ui/adminView/horseList/CreateEditHorse.fxml");
    }

    public void loadRacePage(){
        loadPage("client/ui/adminView/race/CreateRace.fxml");
    }

}
