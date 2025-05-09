package client.ui.userView.landingPage;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.userView.bettingPage.HorseListViewController;
import client.ui.userView.bettingPage.HorseListVM;
import client.networking.horses.HorsesClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the user landing page view.
 * Manages user interactions and displays upcoming race information
 * following the MVVM pattern.
 */
public class UserLandingPageController {

    /** Label displaying the next race information */
    @FXML private Label raceLabel;

    /** Button to navigate to the betting stage */
    @FXML private Button enterBettingStage;

    /** ViewModel that provides data and operations for this view */
    private UserLandingPageVM viewModel;

    /** The horse client needed for the betting view */
    private HorsesClient horsesClient;

    /** Socket service for communication with the server */
    private SocketService socketService;

    /**
     * Default empty constructor required by FXML loader.
     */
    public UserLandingPageController() {}

    /**
     * Initializes the controller with the provided dependencies.
     * Sets up bindings between UI components and ViewModel properties,
     * and configures action handlers for buttons.
     *
     * @param raceClient The client for race-related server operations
     * @param horsesClient The client for horse-related server operations
     * @param socketService The service for socket communication
     */
    public void initialize(RaceClient raceClient, HorsesClient horsesClient, SocketService socketService) {
        this.horsesClient = horsesClient;
        this.socketService = socketService;  // Store socketService as a field

        // Initialize the ViewModel with required dependencies
        this.viewModel = new UserLandingPageVM(raceClient, socketService);

        // Bind UI components to ViewModel properties
        raceLabel.textProperty().bind(viewModel.raceInfoProperty());

        // Set up action handlers
        enterBettingStage.setOnAction(e -> handleButtonClick());

        // Listen for navigation requests from the ViewModel
        viewModel.navigateToBettingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                navigateToBettingPage();
                viewModel.resetNavigation();
            }
        });
    }

    /**
     * Handles the click event on the "Enter Betting Stage" button.
     * Delegates to the ViewModel to initiate navigation.
     */
    @FXML
    public void handleButtonClick() {
        viewModel.enterBettingStage();
    }

    /**
     * Navigates to the betting page by loading the appropriate FXML
     * and initializing its controller.
     */
    /**
     * Navigates to the betting page by loading the appropriate FXML
     * and initializing its controller.
     */
    private void navigateToBettingPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/client/ui/userView/bettingPage/HorseListView.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize it with the ViewModel
            HorseListViewController controller = loader.getController();

            // Create the ViewModel with the current constructor signature
            HorseListVM horseListVM = new HorseListVM(horsesClient, socketService);

            // Initialize the controller with the ViewModel
            controller.initialize(horseListVM);

            // Update the scene
            Stage stage = (Stage) enterBettingStage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Betting - " + (viewModel.getSelectedRace() != null ?
                    viewModel.getSelectedRace().name() : "No Race Selected"));

        } catch (IOException e) {
            e.printStackTrace();
            // In a production app, you would show an error dialog here
        }
    }

}