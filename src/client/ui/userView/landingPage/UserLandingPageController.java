package client.ui.userView.landingPage;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import client.ui.userView.bettingPage.UserBettingViewController;
import client.ui.userView.bettingPage.UserBettingViewVM;
import client.networking.horses.HorsesClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLandingPageController implements Controller {

    @FXML private Label raceLabel;
    @FXML private Button enterBettingStage;
    @FXML private Button quitButton;
    @FXML private Label balance;
    @FXML private ListView<String> betStats;

    private UserLandingPageVM viewModel;
    private MainWindowController mainWindowController;

    public UserLandingPageController() {}

    @Override
    public void initialize(ViewModel userLandingPageVM) {
        this.viewModel = (UserLandingPageVM) userLandingPageVM;

        // Bind properties to UI components
        raceLabel.textProperty().bind(viewModel.raceInfoProperty());
        balance.textProperty().bind(viewModel.balanceInfoProperty());
        betStats.setItems(viewModel.getBetHistory());

        // Bind the disabled property of the enterBettingStage button to the ViewModel property
        enterBettingStage.disableProperty().bind(viewModel.bettingButtonDisabledProperty());

        // Set up button actions
        enterBettingStage.setOnAction(e -> handleButtonClick());
        quitButton.setOnAction(e -> handleQuitButton());

        // Listen for navigation requests
        viewModel.navigateToBettingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                navigateToBettingPage();
                viewModel.resetNavigation();
            }
        });
    }

    @Override
    public void setWindowController(MainWindowController mainWindowController) {
        if(mainWindowController != null){
            this.mainWindowController = mainWindowController;
        }
    }

    @FXML
    public void handleButtonClick() {
         viewModel.enterBettingStage();
    }

    @FXML
    public void handleQuitButton() {
        // Clean up before exiting
        viewModel.quitApplication();
        // Exit the application
        Platform.exit();
    }

    private void navigateToBettingPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/client/ui/userView/bettingPage/UserBettingView.fxml"));
            Parent root = loader.load();

            UserBettingViewController controller = loader.getController();

            // Pass both horsesClient and socketService
            UserBettingViewVM userBettingViewVM = new UserBettingViewVM(mainWindowController.getHorsesClient(), mainWindowController.getSocketService());

            // If a race is selected, pass it to the betting view
            if (viewModel.getSelectedRace() != null) {
                userBettingViewVM.setRace(viewModel.getSelectedRace());
            }

            controller.initialize(userBettingViewVM);

            Stage stage = (Stage) enterBettingStage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Betting - " + (viewModel.getSelectedRace() != null ?
                    viewModel.getSelectedRace().name() : "No Race Selected"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}