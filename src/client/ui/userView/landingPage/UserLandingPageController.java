package client.ui.userView.landingPage;

import client.networking.SocketService;
import client.networking.race.RaceClient;
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

public class UserLandingPageController {

    @FXML private Label raceLabel;
    @FXML private Button enterBettingStage;
    @FXML private Button quitButton;
    @FXML private Label balance;
    @FXML private ListView<String> betStats;

    private UserLandingPageVM viewModel;
    private HorsesClient horsesClient;
    private SocketService socketService;
    private RaceClient raceClient;

    public UserLandingPageController() {}

    public void initialize(UserLandingPageVM viewModel, HorsesClient horsesClient, SocketService socketService, RaceClient raceClient) {
        this.viewModel = viewModel;
        this.horsesClient = horsesClient;
        this.socketService = socketService;
        this.raceClient = raceClient;

        // Bind properties to UI components
        raceLabel.textProperty().bind(viewModel.raceInfoProperty());
        balance.textProperty().bind(viewModel.balanceInfoProperty());
        betStats.setItems(viewModel.getBetHistory());

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

            // Belangrijk: geef zowel horsesClient als socketService door
            UserBettingViewVM userBettingViewVM = new UserBettingViewVM(horsesClient, socketService);
            controller.initialize(userBettingViewVM);

            Stage stage = (Stage) enterBettingStage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Betting - " + (viewModel.getSelectedRace() != null ?
                    viewModel.getSelectedRace().name() : "No Race Selected"));

        } catch (IOException e) {
            e.printStackTrace();
            // Toon een foutmelding in een productieprogramma
        }
    }
}