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
 *
 * Responsible for drawing the race track, updating horse positions,
 * and animating the race using a JavaFX Canvas.
 */
public class GameViewController implements Controller {

    private final Map<Integer, Color> horseColors = new HashMap<>();
    private final Random random = new Random();
    private final Color[] colorPalette = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.PURPLE, Color.BROWN, Color.PINK, Color.CYAN
    };
    @FXML private Canvas raceCanvas;
    @FXML private Label statusLabel;
    private GameViewVM viewModel;
    private MainWindowController mainWindowController;
    private GraphicsContext graphicsContext;
    private AnimationTimer animationTimer;

    /**
     * Initializes the controller with the provided ViewModel.
     * Binds UI properties and sets up the race canvas and animation.
     *
     * @param viewModel the ViewModel passed from the main window controller
     */
    @Override
    public void initialize(ViewModel viewModel) {
        this.viewModel = (GameViewVM) viewModel;
        this.graphicsContext = raceCanvas.getGraphicsContext2D();

        setupRaceVisualization();
        statusLabel.textProperty().bind(this.viewModel.statusTextProperty());

        setupAnimationTimer();
        animationTimer.start();
    }

    /**
     * Prepares the race visualization.
     * Assigns colors to horses and draws the initial track layout.
     */
    private void setupRaceVisualization() {
        List<HorseDTO> horses = viewModel.getHorses();
        for (int i = 0; i < horses.size(); i++) {
            horseColors.put(horses.get(i).id(), colorPalette[i % colorPalette.length]);
        }
        drawRaceTrack();
    }

    /**
     * Creates an animation timer that continuously redraws the race canvas.
     */
    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawRaceTrack();
            }
        };
    }

    /**
     * Draws the race track and all horses based on their current positions.
     * Uses ViewModel data for horse positions and race state.
     */
    private void drawRaceTrack() {
        double canvasWidth = raceCanvas.getWidth();
        double canvasHeight = raceCanvas.getHeight();
        int trackLength = viewModel.getTrackLength();

        List<HorseDTO> horses = viewModel.getHorses();
        Map<Integer, Integer> horsePositions = viewModel.getHorsePositions();

        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(canvasWidth - 5, 0, 2, canvasHeight);

        double laneHeight = canvasHeight / Math.max(1, horses.size());

        for (int i = 0; i < horses.size(); i++) {
            HorseDTO horse = horses.get(i);
            double y = i * laneHeight;

            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(1);
            graphicsContext.strokeLine(0, y, canvasWidth, y);

            graphicsContext.setFill(Color.web("#f0f0f0"));
            graphicsContext.fillRect(0, y, canvasWidth, laneHeight);

            int position = horsePositions.getOrDefault(horse.id(), 0);
            double xPos = (position / (double) trackLength) * (canvasWidth - 50);

            graphicsContext.setFill(horseColors.get(horse.id()));
            graphicsContext.fillOval(xPos, y + 5, 40, laneHeight - 10);

            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillText(horse.name(), xPos + 10, y + laneHeight / 2 + 5);
        }
    }

    /**
     * Sets the reference to the main window controller for navigation purposes.
     *
     * @param mainWindowController the main application controller
     */
    @Override
    public void setWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}
