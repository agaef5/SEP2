package client.ui.navigation;

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
import client.ui.userView.landingPage.UserLandingPageController;
import client.ui.userView.landingPage.UserLandingPageVM;
import client.ui.util.ErrorHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import shared.DTO.UserDTO;
import shared.DTO.RaceDTO;


import java.io.IOException;

public class MainWindowController {
    public StackPane mainPane;
    public HBox adminMenu;

    private Stage stage;

    private SocketService socketService;
    private AuthenticationClient authenticationClient;
    private HorsesClient horsesClient;
    private RaceClient raceClient;
    private boolean isAdminView = false;
    private String username;

    public void initialize(SocketService socketService, LoginController loginController) {
        this.socketService = socketService;
        authenticationClient = new SocketAuthenticationClient(socketService);
        horsesClient = new SocketHorsesClient(socketService);
        raceClient = new SocketRaceClient(socketService);;

        if (loginController != null) {
            loginController.setWindowController(this);
            socketService.addListener(loginController);
        }
        loadLoginPage();

        mainPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            if (stage != null) {
                stage.sizeToScene();
            }
        });
    }

    private void loadPage(String fxmlFile) {
        Platform.runLater(() -> {
        try {
            mainPane.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
            Pane newContent = loader.load();

            if(isAdminView){
                adminMenu.setPrefHeight(25.0);
                adminMenu.setVisible(true);
                adminMenu.setDisable(false);
            }

            ViewModel viewModel = null;
            Controller controller = loader.getController();
            controller.setWindowController(this);

            if (controller instanceof LoginController) {
                viewModel = new LoginVM(authenticationClient, socketService);
            }

            if (controller instanceof RegisterController)
            {
                viewModel = new RegisterVM(authenticationClient, socketService);
                socketService.addListener((MessageListener) controller);
            }

            if (controller instanceof AdminPanelController) {
                viewModel = new AdminPanelVM(raceClient, socketService);
            }

            if(controller instanceof CreateEditHorseController){
                viewModel = new CreateEditHorseVM(horsesClient, socketService);
            }

            if(controller instanceof CreateRaceController){
                viewModel = new CreateRaceVM(raceClient, socketService);
            }

            if (controller instanceof UserLandingPageController) {
                viewModel = new UserLandingPageVM(raceClient, socketService);
            }

            if(controller instanceof UserBettingViewController){
                viewModel = new UserBettingViewVM(horsesClient, socketService);
            }

            controller.initialize(viewModel);
            mainPane.getChildren().add(newContent);
        } catch (IOException e) {
            ErrorHandler.handleError(new IllegalArgumentException(e), "Error loading page");
        }});
    }

    public void loadRegisterPage(){
        loadPage("client/ui/authentication/register/Register.fxml");
    }

    public void loadLoginPage(){
        loadPage("client/ui/authentication/login/Login.fxml");
    }

    public void loadUserLandingPage(){
        loadPage("client/ui/userView/landingPage/userLandingPage.fxml");
    }

    public void loadBettingPage(String windowTitle){
        stage.setTitle(windowTitle);
        loadPage("client/ui/userView/bettingPage/UserBettingView.fxml");}

    public void loadAdminPanel(){
        if(isAdminView) loadPage("client/ui/adminView/adminPanel/AdminPanel.fxml");
    }

    public void loadHorsePage(){
        if(isAdminView) loadPage("client/ui/adminView/horseList/CreateEditHorse.fxml");
    }

    public void loadRacePage(){
        if(isAdminView) loadPage("client/ui/adminView/race/CreateRace.fxml");
    }

    public void authorizeUser(UserDTO userDTO){
        Platform.runLater(() -> {
            if (userDTO.username() != null) setUsername(userDTO.username());
            if (authenticateAdmin(userDTO)) loadAdminPanel();
            else loadUserLandingPage();
        });
    }

    public boolean authenticateAdmin(UserDTO userDTO){
        return isAdminView = userDTO.isAdmin();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public HorsesClient getHorsesClient() {
        return horsesClient;
    }

    public RaceClient getRaceClient() {
        return raceClient;
    }

    public AuthenticationClient getAuthenticationClient() {
        return authenticationClient;
    }

    public SocketService getSocketService() {
        return socketService;
    }
}
