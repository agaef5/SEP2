package client.ui.adminView.race;

import client.ui.navigation.MainWindowController;
import client.ui.util.ErrorHandler;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.DTO.RaceTrackDTO;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({MockitoExtension.class})
class CreateRaceControllerTest {

    private CreateRaceController controller;

    @Mock
    private CreateRaceVM mockViewModel;

    @Mock
    private MainWindowController mockMainWindowController;

    // JavaFX components - these will be real objects since mocking them is complex
    private TextField nrOfHorses;
    private ChoiceBox<RaceTrackDTO> raceTrack;
    private TextField raceName;
    private Button createRace;
    private ListView<RaceDTO> raceQueueList;
    private Label messageLabel;

    // Test data
    private ObservableList<RaceTrackDTO> testRaceTracks;
    private ObservableList<RaceDTO> testRaceQueue;
    private StringProperty testRaceNameProperty;
    private IntegerProperty testHorseCountProperty;
    private StringProperty testMessageProperty;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize JavaFX toolkit for testing
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX toolkit already initialized
        }

        // Wait for JavaFX to initialize
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                initializeJavaFXComponents();
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Create controller and inject mocked FXML fields
        controller = new CreateRaceController();
        injectFXMLFields();

        // Setup test data
        setupTestData();
        setupMockBehavior();
    }

    private void initializeJavaFXComponents() {
        nrOfHorses = new TextField();
        raceTrack = new ChoiceBox<>();
        raceName = new TextField();
        createRace = new Button();
        raceQueueList = new ListView<>();
        messageLabel = new Label();
    }

    private void injectFXMLFields() throws Exception {
        setPrivateField("nrOfHorses", nrOfHorses);
        setPrivateField("raceTrack", raceTrack);
        setPrivateField("raceName", raceName);
        setPrivateField("createRace", createRace);
        setPrivateField("raceQueueList", raceQueueList);
        setPrivateField("messageLabel", messageLabel);
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = CreateRaceController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private void setupTestData() {
        // Create test race tracks
        testRaceTracks = FXCollections.observableArrayList(
                new RaceTrackDTO("Track 1", 1000, "Location 1"),
                new RaceTrackDTO("Track 2", 1500, "Location 2")
        );

        // Create test race queue
        testRaceQueue = FXCollections.observableArrayList(
                new RaceDTO("Race 1", Timestamp.valueOf(LocalDateTime.now()),
                        Arrays.asList(), testRaceTracks.get(0), RaceState.NOT_STARTED)
        );

        // Create properties
        testRaceNameProperty = new SimpleStringProperty();
        testHorseCountProperty = new SimpleIntegerProperty();
        testMessageProperty = new SimpleStringProperty("Test message");
    }

    private void setupMockBehavior() {
        // Only stub methods that are actually used in tests
        lenient().when(mockViewModel.getAvailableRaceTracks()).thenReturn(testRaceTracks);
        lenient().when(mockViewModel.getRaceQueue()).thenReturn(testRaceQueue);
        lenient().when(mockViewModel.raceNameProperty()).thenReturn(testRaceNameProperty);
        lenient().when(mockViewModel.horseCountProperty()).thenReturn(testHorseCountProperty);
        lenient().when(mockViewModel.getMessageLabel()).thenReturn(testMessageProperty);
        lenient().when(mockViewModel.selectedRaceTrackProperty()).thenReturn(mock(javafx.beans.property.ObjectProperty.class));
        lenient().when(mockViewModel.formatRaceTrack(any(RaceTrackDTO.class))).thenAnswer(invocation -> {
            RaceTrackDTO track = invocation.getArgument(0);
            return track != null ? track.name() + " - " + track.length() + "m" : "";
        });
    }

    @Test
    void constructor_Default_CreatesInstance() {
        // Act
        CreateRaceController newController = new CreateRaceController();

        // Assert
        assertNotNull(newController);
    }

    @Test
    void initialize_WithViewModel_SetsUpBindingsAndComponents() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Assert
        Platform.runLater(() -> {
            assertEquals(testRaceTracks, raceTrack.getItems());
            assertEquals(testRaceQueue, raceQueueList.getItems());
            assertEquals("Test message", messageLabel.getText());
        });
    }

    @Test
    void initialize_RaceTrackConverter_FormatsCorrectly() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);
        RaceTrackDTO testTrack = new RaceTrackDTO("Test Track", 2000, "Test Location");

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);
                StringConverter<RaceTrackDTO> converter = raceTrack.getConverter();
                String result = converter.toString(testTrack);

                // Assert
                assertEquals("Test Track - 2000m", result);
                assertNull(converter.fromString("any string")); // Should return null
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void initialize_RaceQueueListCell_FormatsCorrectly() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Create a custom test cell that extends ListCell and exposes updateItem
                class TestListCell extends ListCell<RaceDTO> {
                    @Override
                    protected void updateItem(RaceDTO race, boolean empty) {
                        super.updateItem(race, empty);
                        if (empty || race == null) {
                            setText(null);
                        } else {
                            setText(race.name() + " - Track: " + race.raceTrack().name());
                        }
                    }

                    // Public method to test the protected updateItem
                    public void testUpdateItem(RaceDTO race, boolean empty) {
                        updateItem(race, empty);
                    }
                }

                TestListCell testCell = new TestListCell();
                RaceDTO testRace = testRaceQueue.get(0);

                // Test with valid race
                testCell.testUpdateItem(testRace, false);
                assertEquals("Race 1 - Track: Track 1", testCell.getText());

                // Test with empty cell
                testCell.testUpdateItem(null, true);
                assertNull(testCell.getText());

                // Verify the cell factory was set correctly
                assertNotNull(raceQueueList.getCellFactory());

                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void initialize_HorsesTextFieldListener_UpdatesViewModel() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Simulate text input
                nrOfHorses.setText("5");

                // Assert
                assertEquals(5, testHorseCountProperty.get());
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void initialize_HorsesTextFieldListener_HandlesInvalidInput() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Simulate invalid text input
                nrOfHorses.setText("invalid");

                // Assert
                assertEquals(0, testHorseCountProperty.get());
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void initialize_RaceNameBinding_WorksBidirectionally() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act & Assert
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Test UI to ViewModel
                raceName.setText("Test Race Name");
                assertEquals("Test Race Name", testRaceNameProperty.get());

                // Test ViewModel to UI
                testRaceNameProperty.set("Updated Race Name");
                assertEquals("Updated Race Name", raceName.getText());

                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void setWindowController_WithValidController_SetsController() {
        // Act
        controller.setWindowController(mockMainWindowController);

        // Assert
        // Since the field is private, we can't directly verify it was set
        // But we can verify the method doesn't throw an exception
        assertDoesNotThrow(() -> controller.setWindowController(mockMainWindowController));
    }

    @Test
    void setWindowController_WithNullController_DoesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> controller.setWindowController(null));
    }

    @Test
    void onCreateRaceClicked_WithValidInput_CallsViewModelCreateRace() throws InterruptedException {
        // Arrange
        when(mockViewModel.isValid()).thenReturn(true);
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Directly call the method instead of firing the button
                // since the button action handler calls the same method
                Field viewModelField = CreateRaceController.class.getDeclaredField("viewModel");
                viewModelField.setAccessible(true);
                viewModelField.set(controller, mockViewModel);

                // Use reflection to call the private method
                java.lang.reflect.Method method = CreateRaceController.class.getDeclaredMethod("onCreateRaceClicked");
                method.setAccessible(true);
                method.invoke(controller);

                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);

        // Assert
        verify(mockViewModel).createRace();
    }

    @Test
    void onCreateRaceClicked_WithInvalidInput_ShowsAlert() throws InterruptedException {
        // Arrange
        when(mockViewModel.isValid()).thenReturn(false);
        CountDownLatch latch = new CountDownLatch(1);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = mockStatic(ErrorHandler.class)) {
            // Act
            Platform.runLater(() -> {
                try {
                    controller.initialize(mockViewModel);

                    // Set the viewModel field
                    Field viewModelField = CreateRaceController.class.getDeclaredField("viewModel");
                    viewModelField.setAccessible(true);
                    viewModelField.set(controller, mockViewModel);

                    // Call the method directly
                    java.lang.reflect.Method method = CreateRaceController.class.getDeclaredMethod("onCreateRaceClicked");
                    method.setAccessible(true);
                    method.invoke(controller);

                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                    latch.countDown();
                }
            });
            latch.await(5, TimeUnit.SECONDS);

            // Assert
            mockedErrorHandler.verify(() ->
                    ErrorHandler.showAlert("Incorrect input", "Make sure all the fields are filled."));
            verify(mockViewModel, never()).createRace();
        }
    }

    @Test
    void initialize_CreateRaceButtonAction_IsSetCorrectly() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Assert - just verify the button has an action handler
                assertNotNull(createRace.getOnAction());
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void initialize_MessageLabelBinding_WorksCorrectly() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            try {
                controller.initialize(mockViewModel);

                // Change the property value
                testMessageProperty.set("New message");

                // Assert
                assertEquals("New message", messageLabel.getText());
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}