package client.ui.userView.gameView;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import shared.DTO.HorseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Controller for the race game view.
 * Responsible for rendering the race animation using JavaFX Canvas and synchronizing with the ViewModel.
 */
public class GameViewController implements Controller {

    @FXML private Canvas raceCanvas;
    @FXML private Label statusLabel;

    private GameViewVM viewModel;
    private MainWindowController mainWindowController;
    private GraphicsContext graphicsContext;
    private AnimationTimer animationTimer;

    // Map horse IDs to their assigned colors for rendering
    private final Map<Integer, Color> horseColors = new HashMap<>();
    private final Random random = new Random();

    // Color palette used to differentiate horses
    private final Color[] colorPalette = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.PURPLE, Color.BROWN, Color.PINK, Color.CYAN
    };

    /**
     * Called when the FXML is initialized.
     * Binds UI components, prepares canvas rendering, and starts the animation loop.
     *
     * @param viewModel The ViewModel passed from the main window controller.
     */
    @Override
    public void initialize(ViewModel viewModel) {
        this.viewModel = (GameViewVM) viewModel;
        this.graphicsContext = raceCanvas.getGraphicsContext2D();

        // Initialize the view with the current race data
        setupRaceVisualization();

        // Bind UI elements to viewModel properties
        statusLabel.textProperty().bind(this.viewModel.statusTextProperty());

        // Set up animation timer for rendering
        setupAnimationTimer();

        // Start the animation timer immediately
        animationTimer.start();
    }

    /**
     * Assigns a unique color to each horse and draws the initial track.
     */
    private void setupRaceVisualization() {
        // Assign colors to horses
        List<HorseDTO> horses = viewModel.getHorses();
        for (int i = 0; i < horses.size(); i++) {
            HorseDTO horse = horses.get(i);
            horseColors.put(horse.id(), colorPalette[i % colorPalette.length]);
        }

        // Draw initial race track
        drawRaceTrack();
    }

    /**
     * Initializes the animation loop that continuously redraws the race canvas.
     */
    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Redraw canvas every frame (JavaFX handles frame rate)
                drawRaceTrack();
            }
        };
    }

    /**
     * Draws the full race track, horses, and their positions based on ViewModel state.
     */
    private void drawRaceTrack() {
        double canvasWidth = raceCanvas.getWidth();
        double canvasHeight = raceCanvas.getHeight();
        int trackLength = viewModel.getTrackLength();

        List<HorseDTO> horses = viewModel.getHorses();
        Map<Integer, Integer> horsePositions = viewModel.getHorsePositions();

        // Clear canvas
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

        // Draw background
        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);

        // Draw finish line
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(canvasWidth - 5, 0, 2, canvasHeight);

        // Calculate lane height
        double laneHeight = canvasHeight / Math.max(1, horses.size());

        // Draw lanes and horses
        for (int i = 0; i < horses.size(); i++) {
            HorseDTO horse = horses.get(i);
            double y = i * laneHeight;

            // Draw lane separators
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(1);
            graphicsContext.strokeLine(0, y, canvasWidth, y);

            // Draw lane background
            graphicsContext.setFill(Color.web("#f0f0f0"));
            graphicsContext.fillRect(0, y, canvasWidth, laneHeight);

            // Get horse position
            int position = horsePositions.getOrDefault(horse.id(), 0);
            double xPos = (position / (double) trackLength) * (canvasWidth - 50);

            // Draw horse
            graphicsContext.setFill(horseColors.get(horse.id()));
            graphicsContext.fillOval(xPos, y + 5, 40, laneHeight - 10);

            // Draw horse name
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillText(horse.name(), xPos + 10, y + laneHeight / 2 + 5);
        }
    }

    /**
     * Sets the main window controller, used for navigation or shutdown if needed later.
     *
     * @param mainWindowController Reference to the main window controller.
     */
    @Override
    public void setWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}