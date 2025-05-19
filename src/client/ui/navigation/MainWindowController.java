package client.ui.navigation;

import client.modelManager.ModelManager;
import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.networking.authentication.SocketAuthenticationClient;
import client.networking.horses.HorsesClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.RaceClient;
import client.networking.race.SocketRaceClient;
import client.ui.adminView.adminPanel.AdminPanelController;
import client.ui.adminView.adminPanel.AdminPanelVM;
import client.ui.adminView.horseList.CreateEditHorseController;
import client.ui.adminView.horseList.CreateEditHorseVM;
import client.ui.adminView.race.CreateRaceVM;
import client.ui.adminView.race.CreateRaceController;
import client.ui.authentication.login.LoginController;
import client.ui.authentication.login.LoginVM;
import client.ui.authentication.register.RegisterController;
import client.ui.authentication.register.RegisterVM;
import client.ui.common.Controller;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import client.ui.userView.bettingPage.UserBettingViewController;
import client.ui.userView.bettingPage.UserBettingViewVM;
import client.ui.userView.gameView.GameViewController;
import client.ui.userView.gameView.GameViewVM;
import client.ui.userView.landingPage.UserLandingPageController;
import client.ui.userView.landingPage.UserLandingPageVM;
import client.ui.util.ErrorHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shared.DTO.UserDTO;
import shared.DTO.RaceDTO;


import java.io.IOException;

public class MainWindowController {
    public StackPane mainPane;
    public HBox adminMenu;

    private Stage stage;

    ModelManager modelManager;
    private boolean isAdminView = false;
    private String username;

    public void initialize(ModelManager modelManager, LoginController loginController) {
        this.modelManager = modelManager;

        if (loginController != null) {
            loginController.setWindowController(this);
        }
        loadLoginPage();

        mainPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            if (stage != null) {
                stage.sizeToScene();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadPage(String fxmlFile) {
        loadPage(fxmlFile, null);
    }

    private void loadPage(String fxmlFile, Object additionalData) {
        Platform.runLater(() -> {
            try {
                mainPane.getChildren().clear();

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
                Parent newContent = loader.load();

                if (isAdminView) {
                    adminMenu.setPrefHeight(25.0);
                    adminMenu.setVisible(true);
                    adminMenu.setDisable(false);
                }

                ViewModel viewModel = null;
                Controller controller = loader.getController();
                controller.setWindowController(this);

                // Create the appropriate ViewModel based on controller type
                if (controller instanceof LoginController) {
                    viewModel = new LoginVM(modelManager);
                } else if (controller instanceof RegisterController) {
                    viewModel = new RegisterVM(modelManager);
                } else if (controller instanceof AdminPanelController) {
                    viewModel = new AdminPanelVM(modelManager);
                } else if (controller instanceof CreateEditHorseController) {
                    viewModel = new CreateEditHorseVM(modelManager);
                } else if (controller instanceof CreateRaceController) {
                    viewModel = new CreateRaceVM(modelManager);
                } else if (controller instanceof UserLandingPageController) {
                    viewModel = new UserLandingPageVM(modelManager);
                } else if (controller instanceof UserBettingViewController) {
                    // Pass the race data if provided
                    if (additionalData instanceof RaceDTO race) {
                        viewModel = new UserBettingViewVM(modelManager, race);
                    } else {
                        throw new IllegalArgumentException("UserBettingView requires a RaceDTO");
                    }
                } else if (controller instanceof GameViewController) {
                    // Pass the race data for the game view
                    if (additionalData instanceof RaceDTO race) {
                        viewModel = new GameViewVM(modelManager, race);
                    } else {
                        // Create with default (may want to throw an error instead)
                        viewModel = new GameViewVM(modelManager, null);
                    }
                }

                controller.initialize(viewModel);
                mainPane.getChildren().add(newContent);
            } catch (IOException e) {
                ErrorHandler.handleError(new IllegalArgumentException(e), "Error loading page");
            }
        });
    }

    public void loadRegisterPage() {
        loadPage("client/ui/authentication/register/Register.fxml");
    }

    public void loadLoginPage() {
        loadPage("client/ui/authentication/login/Login.fxml");
    }

    public void loadUserLandingPage() {
        loadPage("client/ui/userView/landingPage/userLandingPage.fxml");
    }

    public void loadBettingPage(RaceDTO race) {
        stage.setTitle(race != null ? "Betting - " + race.name() : "Betting");
        loadPage("client/ui/userView/bettingPage/UserBettingView.fxml", race);
    }

    public void loadGameView(RaceDTO race) {
        loadPage("client/ui/userView/gameView/GameView.fxml", race);
    }

    public void loadAdminPanel() {
        if (isAdminView) loadPage("client/ui/adminView/adminPanel/AdminPanel.fxml");
    }

    public void loadHorsePage() {
        if (isAdminView) loadPage("client/ui/adminView/horseList/CreateEditHorse.fxml");
    }

    public void loadRacePage() {
        if (isAdminView) loadPage("client/ui/adminView/race/CreateRace.fxml");
    }


    public void authorizeUser() {
        Platform.runLater(() -> {

            UserDTO userDTO = modelManager.getCurrentUser();

            if (userDTO.username() != null) setUsername(userDTO.username());
            authenticateAdmin(userDTO);
            if (isAdminView) loadAdminPanel();
            else loadUserLandingPage();
        });
    }

    public void authenticateAdmin(UserDTO userDTO) {
        isAdminView = userDTO.isAdmin();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void shutdown() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)
        );
    }


}


