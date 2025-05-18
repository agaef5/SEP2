package client.ui.userView.gameView;

import client.ui.userView.gameView.GameViewController;
import client.ui.userView.gameView.GameViewVM;
import client.ui.userView.gameView.TestModelManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.DTO.*;

import java.util.Arrays;
import java.util.List;

public class GameViewVisualTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create test data
        List<HorseDTO> testHorses = Arrays.asList(
                new HorseDTO(1, "Thunder", 10, 20),
                new HorseDTO(2, "Lightning", 8, 18),
                new HorseDTO(3, "Storm", 12, 22)
        );

        RaceTrackDTO testTrack = new RaceTrackDTO("Test Track", 1000, "Test Location");
        RaceDTO testRace = new RaceDTO("Test Race", null, testHorses, testTrack, RaceState.IN_PROGRESS);

        // Create test ModelManager with animation
        TestModelManager testModelManager = new TestModelManager();

        // Load GameView FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/userView/gameView/gameView.fxml"));
        Parent root = loader.load();

        // Get controller and create ViewModel
        GameViewController controller = loader.getController();
        GameViewVM viewModel = new GameViewVM(testModelManager, testRace);

        // Initialize controller with ViewModel
        controller.initialize(viewModel);

        // Create scene and show
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("GameView Visual Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}