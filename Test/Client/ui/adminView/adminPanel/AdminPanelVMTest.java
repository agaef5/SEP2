package Client.ui.adminView.adminPanel;

import client.modelManager.ModelManager;
import client.ui.adminView.adminPanel.AdminPanelVM;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.DTO.RaceTrackDTO;
import shared.DTO.HorseDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminPanelVMTest {

    @Mock
    private ModelManager mockModelManager;

    @Mock
    private ObservableList<RaceDTO> mockRaceList;

    private ObjectProperty<RaceDTO> nextRaceProperty;
    private AdminPanelVM adminPanelVM;
    private RaceTrackDTO testRaceTrack;
    private List<HorseDTO> testHorses;
    private RaceDTO testRace;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        testRaceTrack = new RaceTrackDTO("Test Track", 1000, "Test Location");
        testHorses = List.of(
                new HorseDTO(1, "Horse1", 10, 20),
                new HorseDTO(2, "Horse2", 12, 18)
        );
        testRace = new RaceDTO(
                "Test Race",
                Timestamp.valueOf(LocalDateTime.now()),
                testHorses,
                testRaceTrack,
                RaceState.NOT_STARTED
        );

        // Setup nextRace property
        nextRaceProperty = new SimpleObjectProperty<>();

        // Setup mock behavior
        when(mockModelManager.getNextRace()).thenReturn(nextRaceProperty);
        when(mockModelManager.getRaceList()).thenReturn(mockRaceList);

        // Create the view model with mocked dependencies
        adminPanelVM = new AdminPanelVM(mockModelManager);
    }

    //ZERO
    @Test
    void raceInfoTextProperty_Zero_InitialValue_HasDefaultMessage() {
        // Act
        StringProperty property = adminPanelVM.raceInfoTextProperty();

        // Assert
        assertNotNull(property);
        assertEquals("Info on coming race", property.get());
    }

    //ONE
    @Test
    void raceInfoTextProperty_One_SingleInstance_ReturnsSameProperty() {
        // Act
        StringProperty property1 = adminPanelVM.raceInfoTextProperty();
        StringProperty property2 = adminPanelVM.raceInfoTextProperty();

        // Assert
        assertSame(property1, property2);
    }

    @Test
    void raceInfoTextProperty_Interface_PropertyChange_NotifiesListeners() {
        // Arrange
        StringProperty property = adminPanelVM.raceInfoTextProperty();
        boolean[] changed = {false};
        String[] newValue = {null};
        property.addListener((obs, oldVal, newVal) -> {
            changed[0] = true;
            newValue[0] = newVal;
        });

        // Act - Trigger change by setting next race
        nextRaceProperty.set(testRace);

        // Assert
        assertTrue(changed[0]);
        assertNotNull(newValue[0]);
    }

    @Test
    void raceInfoTextProperty_Simple_Binding_WorksCorrectly() {
        // Arrange
        StringProperty property = adminPanelVM.raceInfoTextProperty();

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            String value = property.get();
            assertNotNull(value);
        });
    }

    @Test
    void updateRaceInfoText_Zero_NoUpcomingRace_DisplaysNoRaceMessage() {
        // Act
        adminPanelVM.updateRaceInfoText(false);

        // Assert
        assertEquals("No race in queue", adminPanelVM.raceInfoTextProperty().get());
    }

    @Test
    void updateRaceInfoText_One_UpcomingRace_DisplaysRaceInfo() {
        // Arrange
        nextRaceProperty.set(testRace);

        // Act
        adminPanelVM.updateRaceInfoText(true);

        // Assert
        String expectedText = "Upcoming race:\nTest Race\nTest Track, Test Location";
        assertEquals(expectedText, adminPanelVM.raceInfoTextProperty().get());
    }

    @Test
    void updateRaceInfoText_Boundary_NotStartedRace_ShowsAsUpcoming() {
        // Arrange
        RaceDTO notStartedRace = new RaceDTO(
                "Not Started Race",
                Timestamp.valueOf(LocalDateTime.now()),
                testHorses,
                testRaceTrack,
                RaceState.NOT_STARTED
        );
        nextRaceProperty.set(notStartedRace);

        // Act
        adminPanelVM.updateRaceInfoText(true);

        // Assert
        String raceInfoText = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(raceInfoText.contains("Upcoming race:"));
        assertTrue(raceInfoText.contains("Not Started Race"));
    }

    @Test
    void updateRaceInfoText_Boundary_InProgressRace_ShowsAsOngoing() {
        // Arrange
        RaceDTO inProgressRace = new RaceDTO(
                "Ongoing Race",
                Timestamp.valueOf(LocalDateTime.now()),
                testHorses,
                testRaceTrack,
                RaceState.IN_PROGRESS
        );
        nextRaceProperty.set(inProgressRace);

        // Act
        adminPanelVM.updateRaceInfoText(true);

        // Assert
        String raceInfoText = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(raceInfoText.contains("Ongoing race:"));
        assertTrue(raceInfoText.contains("Ongoing Race"));
    }

    @Test
    void updateRaceInfoText_Boundary_FinishedRace_ShowsAsUpcoming() {
        // Arrange
        RaceDTO finishedRace = new RaceDTO(
                "Finished Race",
                Timestamp.valueOf(LocalDateTime.now()),
                testHorses,
                testRaceTrack,
                RaceState.FINISHED
        );
        nextRaceProperty.set(finishedRace);

        // Act
        adminPanelVM.updateRaceInfoText(true);

        // Assert
        String raceInfoText = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(raceInfoText.contains("Upcoming race:")); // Default case
        assertTrue(raceInfoText.contains("Finished Race"));
    }

    @Test
    void updateRaceInfoText_Interface_MultipleUpdates_WorkCorrectly() {
        // Arrange
        nextRaceProperty.set(testRace);

        // Act & Assert - First update
        adminPanelVM.updateRaceInfoText(true);
        String firstUpdate = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(firstUpdate.contains("Test Race"));

        // Act & Assert - Second update (no race)
        adminPanelVM.updateRaceInfoText(false);
        assertEquals("No race in queue", adminPanelVM.raceInfoTextProperty().get());

        // Act & Assert - Third update (race again)
        adminPanelVM.updateRaceInfoText(true);
        assertEquals(firstUpdate, adminPanelVM.raceInfoTextProperty().get());
    }

    @Test
    void updateRaceInfoText_Exercise_ComplexRaceInfo_FormatsCorrectly() {
        // Arrange
        RaceTrackDTO complexTrack = new RaceTrackDTO(
                "Super Long Race Track Name",
                99999,
                "Very Remote Location With Spaces"
        );
        RaceDTO complexRace = new RaceDTO(
                "Mega long race name with 123456",
                Timestamp.valueOf(LocalDateTime.now()),
                testHorses,
                complexTrack,
                RaceState.NOT_STARTED
        );
        nextRaceProperty.set(complexRace);

        // Act
        adminPanelVM.updateRaceInfoText(true);

        // Assert
        String raceInfoText = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(raceInfoText.contains("Upcoming race:"));
        assertTrue(raceInfoText.contains("Mega long race name with 123456"));
        assertTrue(raceInfoText.contains("Super Long Race Track Name"));
        assertTrue(raceInfoText.contains("Very Remote Location With Spaces"));

        // Verify format structure
        String[] lines = raceInfoText.split("\n");
        assertEquals(3, lines.length);
        assertEquals("Upcoming race:", lines[0]);
        assertEquals("Mega long race name with 123456", lines[1]);
        assertEquals("Super Long Race Track Name, Very Remote Location With Spaces", lines[2]);
    }

    @Test
    void updateRaceInfoText_Simple_HasUpcomingRaceFalse_ShowsNoRaceMessage() {
        // Arrange
        nextRaceProperty.set(testRace); // Set a race but pass false

        // Act
        adminPanelVM.updateRaceInfoText(false);

        // Assert
        assertEquals("No race in queue", adminPanelVM.raceInfoTextProperty().get());
    }

    //edge case scenario: forcing an error
    @Test
    void updateRaceInfoText_Simple_NullRaceButHasUpcomingTrue_ShowsNoRaceMessage() {
        // Arrange
        nextRaceProperty.set(null); //no race available

        // Act
        adminPanelVM.updateRaceInfoText(true); //upcoming race boolean is true

        // Assert
        assertEquals("No race in queue", adminPanelVM.raceInfoTextProperty().get()); //should return race info
    }

    @Test
    void constructor_Zero_NullModelManager_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new AdminPanelVM(null));
    }

    @Test
    void constructor_One_ValidModelManager_InitializesCorrectly() {
        // Arrange
        reset(mockModelManager);
        when(mockModelManager.getNextRace()).thenReturn(new SimpleObjectProperty<>());
        when(mockModelManager.getRaceList()).thenReturn(FXCollections.observableArrayList());

        // Act
        AdminPanelVM vm = new AdminPanelVM(mockModelManager);

        // Assert
        assertNotNull(vm);
        assertNotNull(vm.raceInfoTextProperty());
        verify(mockModelManager).getNextRace();
        verify(mockModelManager).getAllRaces();
    }

    @Test
    void constructor_Interface_SetupPropertyBindings() {
        // Arrange
        ObjectProperty<RaceDTO> testProperty = new SimpleObjectProperty<>();
        when(mockModelManager.getNextRace()).thenReturn(testProperty);

        // Act
        AdminPanelVM vm = new AdminPanelVM(mockModelManager);

        // Assert - Property binding should work
        testProperty.set(testRace);
        String raceInfoText = vm.raceInfoTextProperty().get();
        assertTrue(raceInfoText.contains("Test Race"));
    }

    @Test
    void constructor_Exercise_FullWorkflow_WorksCorrectly() {
        // Arrange
        ObjectProperty<RaceDTO> raceProperty = new SimpleObjectProperty<>();
        when(mockModelManager.getNextRace()).thenReturn(raceProperty);

        // Act - Create VM
        AdminPanelVM vm = new AdminPanelVM(mockModelManager);

        // Assert - Initial state
        assertEquals("Info on coming race", vm.raceInfoTextProperty().get());

        // Act - Add race
        raceProperty.set(testRace);

        // Assert - Race information updated
        String raceInfo = vm.raceInfoTextProperty().get();
        assertTrue(raceInfo.contains("Test Race"));
        assertTrue(raceInfo.contains("Test Track"));

        // Act - Remove race
        raceProperty.set(null);

        // Assert - Back to no race message
        assertEquals("No race in queue", vm.raceInfoTextProperty().get());
    }

    @Test
    void constructor_Boundary_CallsRequiredMethods() {
        // Arrange
        reset(mockModelManager);
        when(mockModelManager.getNextRace()).thenReturn(new SimpleObjectProperty<>());
        when(mockModelManager.getRaceList()).thenReturn(FXCollections.observableArrayList());

        // Act
        AdminPanelVM vm = new AdminPanelVM(mockModelManager);

        // Assert
        verify(mockModelManager, times(1)).getNextRace();
        verify(mockModelManager, times(1)).getAllRaces();
        verify(mockModelManager, times(1)).getRaceList();
    }

    @Test
    void constructor_Simple_PropertyInitialization() {
        // Act
        AdminPanelVM vm = adminPanelVM;

        // Assert
        assertNotNull(vm.raceInfoTextProperty());
        assertEquals("Info on coming race", vm.raceInfoTextProperty().get());
    }

    @Test
    void raceStateTransitions_Zero_NoChanges_MaintainDefaultText() {
        // Act (no changes made)

        // Assert
        assertEquals("Info on coming race", adminPanelVM.raceInfoTextProperty().get());
    }

    @Test
    void raceStateTransitions_One_SingleTransition_UpdatesText() {
        // Act
        nextRaceProperty.set(testRace);

        // Assert
        String raceInfoText = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(raceInfoText.contains("Test Race"));
    }

    @Test
    void raceStateTransitions_Many_MultipleTransitions_WorkCorrectly() {
        // Arrange
        RaceDTO race1 = new RaceDTO("Race 1", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.NOT_STARTED);
        RaceDTO race2 = new RaceDTO("Race 2", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.IN_PROGRESS);

        // Act & Assert - First race
        nextRaceProperty.set(race1);
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Race 1"));
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Upcoming race:"));

        // Act & Assert - Second race
        nextRaceProperty.set(race2);
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Race 2"));
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Ongoing race:"));

        // Act & Assert - No race
        nextRaceProperty.set(null);
        assertEquals("No race in queue", adminPanelVM.raceInfoTextProperty().get());
    }

    @Test
    void raceStateTransitions_Boundary_AllStates_HandledCorrectly() {
        // Test NOT_STARTED
        RaceDTO notStartedRace = new RaceDTO("Not Started", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.NOT_STARTED);
        nextRaceProperty.set(notStartedRace);
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Upcoming race:"));

        // Test IN_PROGRESS
        RaceDTO inProgressRace = new RaceDTO("In Progress", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.IN_PROGRESS);
        nextRaceProperty.set(inProgressRace);
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Ongoing race:"));

        // Test FINISHED
        RaceDTO finishedRace = new RaceDTO("Finished", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.FINISHED);
        nextRaceProperty.set(finishedRace);
        assertTrue(adminPanelVM.raceInfoTextProperty().get().contains("Upcoming race:"));
    }

    @Test
    void raceStateTransitions_Interface_PropertyChangeListener_TriggersCorrectly() {
        // Arrange
        int[] changeCount = {0};
        adminPanelVM.raceInfoTextProperty().addListener((obs, oldVal, newVal) -> changeCount[0]++);

        // Act
        nextRaceProperty.set(testRace);
        nextRaceProperty.set(null);
        nextRaceProperty.set(testRace);

        // Assert
        assertEquals(3, changeCount[0]);
    }

    @Test
    void raceStateTransitions_Simple_BasicRaceInfo_DisplaysCorrectly() {
        // Arrange
        RaceDTO simpleRace = new RaceDTO("Simple Race", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.NOT_STARTED);

        // Act
        nextRaceProperty.set(simpleRace);

        // Assert
        String raceInfo = adminPanelVM.raceInfoTextProperty().get();
        assertTrue(raceInfo.contains("Simple Race"));
        assertTrue(raceInfo.contains("Test Track"));
        assertTrue(raceInfo.contains("Test Location"));
    }
}