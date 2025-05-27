package client.ui.navigation;

import client.modelManager.ModelManager;
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

/**
 * Main window controller responsible for managing page transitions and setting up views.
 *
 * Handles login, registration, admin and user page loading. It also determines access
 * control based on the user role and binds ViewModels to their controllers.
 */
public class MainWindowController {

    public StackPane mainPane;
    public HBox adminMenu;

    private ModelManager modelManager;
    private Stage stage;
    private boolean isAdminView = false;
    private String username;

    /**
     * Initializes the main window with a model and first controller.
     *
     * @param modelManager the shared model manager
     * @param loginController the login controller to inject navigation ability
     */
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

    /** @param stage the primary application stage */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Generic page loading logic with optional data passing.
     *
     * @param fxmlFile path to the FXML file
     */
    private void loadPage(String fxmlFile) {
        loadPage(fxmlFile, null);
    }

    /**
     * Loads an FXML page into the main window and attaches a suitable ViewModel.
     *
     * @param fxmlFile path to the FXML file
     * @param additionalData optional extra data to pass to the ViewModel
     */
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

                // Bind appropriate ViewModel based on controller type
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
                    if (additionalData instanceof RaceDTO race) {
                        viewModel = new UserBettingViewVM(modelManager, race);
                    } else {
                        throw new IllegalArgumentException("UserBettingView requires a RaceDTO");
                    }
                } else if (controller instanceof GameViewController) {
                    if (additionalData instanceof RaceDTO race) {
                        viewModel = new GameViewVM(modelManager, race);
                    } else {
                        viewModel = new GameViewVM(modelManager, null);
                    }
                }

                controller.initialize(viewModel);
                mainPane.getChildren().add(newContent);

            } catch (IOException e) {
                e.printStackTrace();
                ErrorHandler.handleError(new IllegalArgumentException(e), "Error loading page");
            }
        });
    }

    /** Loads the registration view. */
    public void loadRegisterPage() {
        loadPage("client/ui/authentication/register/Register.fxml");
    }

    /** Loads the login view. */
    public void loadLoginPage() {
        loadPage("client/ui/authentication/login/Login.fxml");
    }

    /** Loads the user landing page. */
    public void loadUserLandingPage() {
        loadPage("client/ui/userView/landingPage/userLandingPage.fxml");
    }

    /**
     * Loads the betting page and sets the window title based on the race.
     *
     * @param race the race selected for betting
     */
    public void loadBettingPage(RaceDTO race) {
        stage.setTitle(race != null ? "Betting - " + race.name() : "Betting");
        stage.setHeight(600);
        loadPage("client/ui/userView/bettingPage/UserBettingView.fxml", race);
    }

    /**
     * Loads the game view for the given race.
     *
     * @param race the race to display in the game view
     */
    public void loadGameView(RaceDTO race) {
        loadPage("client/ui/userView/gameView/GameView.fxml", race);
    }

    /** Loads the admin panel view (only if user is admin). */
    public void loadAdminPanel() {
        if (isAdminView) loadPage("client/ui/adminView/adminPanel/AdminPanel.fxml");
    }

    /** Loads the horse list and creation view (admin only). */
    public void loadHorsePage() {
        if (isAdminView) loadPage("client/ui/adminView/horseList/CreateEditHorse.fxml");
    }

    /** Loads the create race view (admin only). */
    public void loadRacePage() {
        if (isAdminView) loadPage("client/ui/adminView/race/CreateRace.fxml");
    }

    /**
     * Called after login/registration to determine which view to show based on user role.
     */
    public void authorizeUser() {
        Platform.runLater(() -> {
            UserDTO userDTO = modelManager.getCurrentUser();
            if (userDTO.username() != null) setUsername(userDTO.username());
            authenticateAdmin(userDTO);
            if (isAdminView) loadAdminPanel();
            else loadUserLandingPage();
        });
    }

    /**
     * Sets internal state to indicate whether the logged-in user is an admin.
     *
     * @param userDTO the user to evaluate
     */
    public void authenticateAdmin(UserDTO userDTO) {
        isAdminView = userDTO.isAdmin();
    }

    /** @return the currently logged-in username */
    public String getUsername() {
        return username;
    }

    /** @param username the username to store for the session */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Triggers application shutdown by firing a close event.
     */
    public void shutdown() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
