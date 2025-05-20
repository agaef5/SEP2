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

public class GameViewController implements Controller {

    @FXML private Canvas raceCanvas;
    @FXML private Label statusLabel;

    private GameViewVM viewModel;
    private MainWindowController mainWindowController;
    private GraphicsContext gc;
    private AnimationTimer animationTimer;
    private final Map<Integer, Color> horseColors = new HashMap<>();
    private final Random random = new Random();

    private final Color[] colorPalette = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.PURPLE, Color.BROWN, Color.PINK, Color.CYAN
    };

    @Override
    public void initialize(ViewModel viewModel) {
        this.viewModel = (GameViewVM) viewModel;
        this.gc = raceCanvas.getGraphicsContext2D();

        // Initialize the view with the current race data
        setupRaceVisualization();

        // Bind UI elements to viewModel properties
        statusLabel.textProperty().bind(this.viewModel.statusTextProperty());

        // Set up animation timer for rendering
        setupAnimationTimer();

        // Start the animation timer immediately
        animationTimer.start();
    }

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

    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawRaceTrack();
            }
        };
    }

    private void drawRaceTrack() {
        double canvasWidth = raceCanvas.getWidth();
        double canvasHeight = raceCanvas.getHeight();
        int trackLength = viewModel.getTrackLength();
        List<HorseDTO> horses = viewModel.getHorses();
        Map<Integer, Integer> horsePositions = viewModel.getHorsePositions();

        // Clear canvas
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        // Draw background
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Draw finish line
        gc.setFill(Color.BLACK);
        gc.fillRect(canvasWidth - 5, 0, 2, canvasHeight);

        // Calculate lane height
        double laneHeight = canvasHeight / Math.max(1, horses.size());

        // Draw lanes and horses
        for (int i = 0; i < horses.size(); i++) {
            HorseDTO horse = horses.get(i);
            double y = i * laneHeight;

            // Draw lane separators
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.strokeLine(0, y, canvasWidth, y);

            // Draw lane background
            gc.setFill(Color.web("#f0f0f0"));
            gc.fillRect(0, y, canvasWidth, laneHeight);

            // Get horse position
            int position = horsePositions.getOrDefault(horse.id(), 0);
            double xPos = (position / (double) trackLength) * (canvasWidth - 50);

            // Draw horse
            gc.setFill(horseColors.get(horse.id()));
            gc.fillOval(xPos, y + 5, 40, laneHeight - 10);

            // Draw horse name
            gc.setFill(Color.BLACK);
            gc.fillText(horse.name(), xPos + 10, y + laneHeight / 2 + 5);
        }
    }

    @Override
    public void setWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}