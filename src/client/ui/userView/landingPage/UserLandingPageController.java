package client.ui.userView.landingPage;


import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import shared.DTO.RaceDTO;


/**
 * Controller for the user landing page FXML view.
 * Handles UI interactions and binds UI elements to the corresponding ViewModel properties.
 */
public class UserLandingPageController implements Controller {

    @FXML private Label raceLabel;
    @FXML private Button enterBettingStage;
    @FXML private Button quitButton;
    @FXML private Label balance;

    private UserLandingPageVM viewModel;
    private MainWindowController mainWindowController;

    /**
     * Default constructor.
     */
    public UserLandingPageController() {}

    /**
     * Initializes the controller and binds UI components to the ViewModel properties.
     *
     * @param userLandingPageVM The associated {@link ViewModel} instance.
     */
    @Override
    public void initialize(ViewModel userLandingPageVM) {
        this.viewModel = (UserLandingPageVM) userLandingPageVM;

        // Bind properties to UI components
        raceLabel.textProperty().bind(viewModel.raceInfoProperty());
        balance.textProperty().bind(viewModel.balanceInfoProperty().asString("$%d"));

        // Bind the disabled property of the enterBettingStage button to the ViewModel property
        enterBettingStage.disableProperty().bind(viewModel.bettingButtonDisabledProperty());

        // Set up button actions
        enterBettingStage.setOnAction(e -> handleButtonClick());
        quitButton.setOnAction(e -> handleQuitButton());

        // Listen for navigation requests
        viewModel.navigateToBettingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                RaceDTO selectedRace = viewModel.getSelectedRace();
                if (selectedRace != null) {
                    mainWindowController.loadBettingPage(selectedRace);
                }
                viewModel.resetNavigation();
            }
        });
    }

    /**
     * Sets the main window controller used for navigation.
     *
     * @param mainWindowController The main window controller to be used for navigation.
     */
    @Override
    public void setWindowController(MainWindowController mainWindowController) {
        if(mainWindowController != null){
            this.mainWindowController = mainWindowController;
        }
    }

    /**
     * Handles the action when the betting button is clicked.
     * Triggers the navigation flag in the ViewModel.
     */
    public void handleButtonClick() {
        viewModel.enterBettingStage();
    }

    /**
     * Handles the quit button click by shutting down the application.
     */
    public void handleQuitButton() {
        mainWindowController.shutdown();
    }
}
