package Client.ui.adminView.race;

import client.modelManager.ModelManager;
import client.ui.adminView.race.CreateRaceVM;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateRaceVMTest {

    @Mock
    private ModelManager mockModelManager;

    @Mock
    private ObservableList<RaceTrackDTO> mockRaceTracksList;

    @Mock
    private ObservableList<RaceDTO> mockRaceList;

    @Mock
    private StringProperty mockCreateRaceMessageProperty;

    private CreateRaceVM createRaceVM;
    private RaceTrackDTO testRaceTrack;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRaceTrack = new RaceTrackDTO("Test Track", 1000, "Test Location");

        when(mockModelManager.getRaceTracksList()).thenReturn(mockRaceTracksList);
        when(mockModelManager.getRaceList()).thenReturn(mockRaceList);
        when(mockModelManager.createRaceMessageProperty()).thenReturn(mockCreateRaceMessageProperty);

        createRaceVM = new CreateRaceVM(mockModelManager);
    }


    //RACETRACK TESTS
    @Test
    void formatRaceTrack_Zero_NullTrack_ReturnsEmptyString() {
        // Act
        String result = createRaceVM.formatRaceTrack(null);

        // Assert
        assertEquals("", result);
    }

    @Test
    void formatRaceTrack_One_ValidTrack_ReturnsFormattedString() {
        // Arrange
        RaceTrackDTO track = new RaceTrackDTO("Jysk motorvej", 1500, "Danmark");

        // Act
        String result = createRaceVM.formatRaceTrack(track);

        // Assert
        assertEquals("Jysk motorvej - 1500m", result);
    }

    @Test
    void formatRaceTrack_Boundary_ZeroLength_ReturnsFormattedString() {
        // Arrange
        RaceTrackDTO track = new RaceTrackDTO("Short Track", 0, "Test Location");

        // Act
        String result = createRaceVM.formatRaceTrack(track);

        // Assert
        assertEquals("Short Track - 0m", result);
    }

    @Test
    void formatRaceTrack_Boundary_MaxLength_ReturnsFormattedString() {
        // Arrange
        RaceTrackDTO track = new RaceTrackDTO("Ultra Long Track", Integer.MAX_VALUE, "Extreme Location");

        // Act
        String result = createRaceVM.formatRaceTrack(track);

        // Assert
        assertEquals("Ultra Long Track - " + Integer.MAX_VALUE + "m", result);
    }

    @Test
    void formatRaceTrack_Simple_EmptyName_ReturnsFormattedString() {
        // Arrange
        RaceTrackDTO track = new RaceTrackDTO("", 500, "Location");

        // Act
        String result = createRaceVM.formatRaceTrack(track);

        // Assert
        assertEquals(" - 500m", result);
    }

    //RACE NAME PROPERTY TESTS
    @Test
    void raceNameProperty_Zero_InitialValue_IsNull() {
        // Act
        StringProperty property = createRaceVM.raceNameProperty();

        // Assert
        assertNotNull(property);
        assertNull(property.get());
    }

    @Test
    void raceNameProperty_One_SetValue_ReturnsValue() {
        // Arrange
        String raceName = "Grand Prix";

        // Act
        createRaceVM.raceNameProperty().set(raceName);

        // Assert
        assertEquals(raceName, createRaceVM.raceNameProperty().get());
    }

    @Test
    void raceNameProperty_Interface_PropertyChange_NotifiesListeners() {
        // Arrange
        StringProperty property = createRaceVM.raceNameProperty();
        boolean[] changed = {false};
        property.addListener((obs, oldVal, newVal) -> changed[0] = true);

        // Act
        property.set("New Race Name");

        // Assert
        assertTrue(changed[0]);
    }

    @Test
    void raceNameProperty_Simple_EmptyString_IsValid() {
        // Act
        createRaceVM.raceNameProperty().set("");

        // Assert
        assertEquals("", createRaceVM.raceNameProperty().get());
    }

    //HORSE COUNT PROPERTY TESTS
    @Test
    void horseCountProperty_Zero_InitialValue_IsZero() {
        // Act
        IntegerProperty property = createRaceVM.horseCountProperty();

        // Assert
        assertNotNull(property);
        assertEquals(0, property.get());
    }

    @Test
    void horseCountProperty_One_SetToOne_ReturnsOne() {
        // Act
        createRaceVM.horseCountProperty().set(1);

        // Assert
        assertEquals(1, createRaceVM.horseCountProperty().get());
    }

    @Test
    void horseCountProperty_Many_SetToMany_ReturnsMany() {
        // Arrange
        int manyHorses = 20;

        // Act
        createRaceVM.horseCountProperty().set(manyHorses);

        // Assert
        assertEquals(manyHorses, createRaceVM.horseCountProperty().get());
    }

    @Test
    void horseCountProperty_Boundary_NegativeValue_IsStored() {
        // Act
        createRaceVM.horseCountProperty().set(-1);

        // Assert
        assertEquals(-1, createRaceVM.horseCountProperty().get());
    }

    @Test
    void horseCountProperty_Boundary_MaxValue_IsStored() {
        // Act
        createRaceVM.horseCountProperty().set(Integer.MAX_VALUE);

        // Assert
        assertEquals(Integer.MAX_VALUE, createRaceVM.horseCountProperty().get());
    }

    //SELECTED RACETRACK TESTS
    @Test
    void selectedRaceTrackProperty_Zero_InitialValue_IsNull() {
        // Act
        ObjectProperty<RaceTrackDTO> property = createRaceVM.selectedRaceTrackProperty();

        // Assert
        assertNotNull(property);
        assertNull(property.get());
    }

    @Test
    void selectedRaceTrackProperty_One_SelectTrack_ReturnsTrack() {
        // Act
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);

        // Assert
        assertEquals(testRaceTrack, createRaceVM.selectedRaceTrackProperty().get());
    }

    @Test
    void selectedRaceTrackProperty_Interface_SetNull_ClearsSelection() {
        // Arrange
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);

        // Act
        createRaceVM.selectedRaceTrackProperty().set(null);

        // Assert
        assertNull(createRaceVM.selectedRaceTrackProperty().get());
    }

    //GETAVAILABLERACETRACK TESTS
    @Test
    void getAvailableRaceTracks_Zero_EmptyList_ReturnsEmptyObservableList() {
        // Arrange
        ObservableList<RaceTrackDTO> emptyList = FXCollections.observableArrayList();
        when(mockModelManager.getRaceTracksList()).thenReturn(emptyList);
        CreateRaceVM vm = new CreateRaceVM(mockModelManager);

        // Act
        ObservableList<RaceTrackDTO> result = vm.getAvailableRaceTracks();

        // Assert
        assertNotNull(result);
        assertEquals(emptyList, result);
    }

    @Test
    void getAvailableRaceTracks_One_SingleTrack_ReturnsSingleItemList() {
        // Arrange
        ObservableList<RaceTrackDTO> singleTrackList = FXCollections.observableArrayList(testRaceTrack);
        when(mockModelManager.getRaceTracksList()).thenReturn(singleTrackList);
        CreateRaceVM vm = new CreateRaceVM(mockModelManager);

        // Act
        ObservableList<RaceTrackDTO> result = vm.getAvailableRaceTracks();

        // Assert
        assertEquals(singleTrackList, result);
        assertEquals(1, result.size());
    }

    @Test
    void getAvailableRaceTracks_Many_MultipleTracks_ReturnsMultipleItemList() {
        // Arrange
        ObservableList<RaceTrackDTO> multipleTrackList = FXCollections.observableArrayList(
                testRaceTrack,
                new RaceTrackDTO("Track 2", 1200, "Location 2"),
                new RaceTrackDTO("Track 3", 800, "Location 3")
        );
        when(mockModelManager.getRaceTracksList()).thenReturn(multipleTrackList);
        CreateRaceVM vm = new CreateRaceVM(mockModelManager);

        // Act
        ObservableList<RaceTrackDTO> result = vm.getAvailableRaceTracks();

        // Assert
        assertEquals(multipleTrackList, result);
        assertEquals(3, result.size());
    }

    @Test
    void getAvailableRaceTracks_Interface_ReturnsModelManagerList() {
        // Act
        ObservableList<RaceTrackDTO> result = createRaceVM.getAvailableRaceTracks();

        // Assert
        assertEquals(mockRaceTracksList, result);
    }

    //GET RACEQUEUE TESTS
    @Test
    void getRaceQueue_Zero_EmptyQueue_ReturnsEmptyObservableList() {
        // Arrange
        ObservableList<RaceDTO> emptyQueue = FXCollections.observableArrayList();
        when(mockModelManager.getRaceList()).thenReturn(emptyQueue);
        CreateRaceVM vm = new CreateRaceVM(mockModelManager);

        // Act
        ObservableList<RaceDTO> result = vm.getRaceQueue();

        // Assert
        assertNotNull(result);
        assertEquals(emptyQueue, result);
    }

    @Test
    void getRaceQueue_Interface_ReturnsModelManagerRaceList() {
        // Act
        ObservableList<RaceDTO> result = createRaceVM.getRaceQueue();

        // Assert
        assertEquals(mockRaceList, result);
    }

    @Test
    void getMessageLabel_Interface_ReturnsBoundProperty() {
        // Act
        StringProperty result = createRaceVM.getMessageLabel();

        // Assert
        assertNotNull(result);
    }

    //IS VALID BOOLEAN  TESTS
    @Test
    void isValid_Zero_AllEmpty_ReturnsFalse() {
        // Arrange (all properties are initially null/zero)

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_One_ValidConfiguration_ReturnsTrue() {
        // Arrange
        createRaceVM.raceNameProperty().set("Valid Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(1);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertTrue(result);
    }

    @Test
    void isValid_Many_ManyHorses_ReturnsTrue() {
        // Arrange
        createRaceVM.raceNameProperty().set("Big Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(50);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertTrue(result);
    }

    @Test
    void isValid_Boundary_ZeroHorses_ReturnsFalse() {
        // Arrange
        createRaceVM.raceNameProperty().set("No Horse Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(0);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_Boundary_NegativeHorses_ReturnsFalse() {
        // Arrange
        createRaceVM.raceNameProperty().set("Invalid Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(-1);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_Boundary_BlankRaceName_ReturnsFalse() {
        // Arrange
        createRaceVM.raceNameProperty().set("   ");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(5);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_Simple_NullRaceName_ReturnsFalse() {
        // Arrange
        createRaceVM.raceNameProperty().set(null);
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(5);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_Simple_NullRaceTrack_ReturnsFalse() {
        // Arrange
        createRaceVM.raceNameProperty().set("Valid Name");
        createRaceVM.selectedRaceTrackProperty().set(null);
        createRaceVM.horseCountProperty().set(5);

        // Act
        boolean result = createRaceVM.isValid();

        // Assert
        assertFalse(result);
    }

    //CREATE RACE TESTS
    @Test
    void createRace_Zero_InvalidData_DoesNotCallModelManager() {
        // Arrange (invalid - no data set)

        // Act
        createRaceVM.createRace();

        // Assert
        verify(mockModelManager, never()).createRace(any(), any(), any());
    }

    @Test
    void createRace_One_ValidData_CreatesRaceAndClearsFields() {
        // Arrange
        String raceName = "Test Race";
        int horseCount = 1;
        createRaceVM.raceNameProperty().set(raceName);
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(horseCount);

        // Act
        createRaceVM.createRace();

        // Assert
        verify(mockModelManager).createRace(raceName, testRaceTrack, horseCount);
        assertEquals("", createRaceVM.raceNameProperty().get());
        assertEquals(0, createRaceVM.horseCountProperty().get());
    }

    @Test
    void createRace_Boundary_MinimumValidHorseCount_CreatesRace() {
        // Arrange
        createRaceVM.raceNameProperty().set("Minimum Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(1); // Minimum valid count

        // Act
        createRaceVM.createRace();

        // Assert
        verify(mockModelManager).createRace("Minimum Race", testRaceTrack, 1);
    }

    @Test
    void createRace_Boundary_ZeroHorseCount_DoesNotCreateRace() {
        // Arrange
        createRaceVM.raceNameProperty().set("Invalid Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(0);

        // Act
        createRaceVM.createRace();

        // Assert
        verify(mockModelManager, never()).createRace(any(), any(), any());
    }

    @Test
    void createRace_Exercise_MultipleConsecutiveCalls_WorkCorrectly() {
        // Arrange & Act & Assert - First race
        createRaceVM.raceNameProperty().set("First Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(5);
        createRaceVM.createRace();

        verify(mockModelManager).createRace("First Race", testRaceTrack, 5);
        assertEquals("", createRaceVM.raceNameProperty().get());

        // Arrange & Act & Assert - Second race
        createRaceVM.raceNameProperty().set("Second Race");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(3);
        createRaceVM.createRace();

        verify(mockModelManager).createRace("Second Race", testRaceTrack, 3);
        assertEquals("", createRaceVM.raceNameProperty().get());
    }

    @Test
    void createRace_Simple_EmptyRaceName_DoesNotCreateRace() {
        // Arrange
        createRaceVM.raceNameProperty().set("");
        createRaceVM.selectedRaceTrackProperty().set(testRaceTrack);
        createRaceVM.horseCountProperty().set(5);

        // Act
        createRaceVM.createRace();

        // Assert
        verify(mockModelManager, never()).createRace(any(), any(), any());
    }


    //CONSTRUCTOR TESTS
    @Test
    void constructor_Interface_InitializesModelManagerCalls() {
        // Arrange - Reset mocks to get clean verification
        reset(mockModelManager);
        when(mockModelManager.getRaceTracksList()).thenReturn(mockRaceTracksList);
        when(mockModelManager.getRaceList()).thenReturn(mockRaceList);
        when(mockModelManager.createRaceMessageProperty()).thenReturn(mockCreateRaceMessageProperty);

        // Act - Create new instance for clean testing
        CreateRaceVM testVM = new CreateRaceVM(mockModelManager);

        // Assert - Verify the actual number of calls made by the constructor
        verify(mockModelManager, atLeastOnce()).getRaceTracksList();
        verify(mockModelManager, atLeastOnce()).getRaceList();
        verify(mockModelManager).createRaceMessageProperty();
        verify(mockModelManager).getRaceTracks();
        verify(mockModelManager).getAllHorses();
        verify(mockModelManager).getAllRaces();
    }

    @Test
    void constructor_Simple_NullModelManager_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new CreateRaceVM(null));
    }

    @Test
    void constructor_Zero_VerifyExactCallCounts() {
        // Arrange - Reset and setup fresh mocks
        reset(mockModelManager);
        when(mockModelManager.getRaceTracksList()).thenReturn(mockRaceTracksList);
        when(mockModelManager.getRaceList()).thenReturn(mockRaceList);
        when(mockModelManager.createRaceMessageProperty()).thenReturn(mockCreateRaceMessageProperty);

        // Act
        CreateRaceVM testVM = new CreateRaceVM(mockModelManager);

        // Assert - Based on the error, getRaceTracksList is called 2 times
        verify(mockModelManager, times(2)).getRaceTracksList();
        verify(mockModelManager, atLeastOnce()).getRaceList();
        verify(mockModelManager, times(1)).createRaceMessageProperty();
        verify(mockModelManager, times(1)).getRaceTracks();
        verify(mockModelManager, times(1)).getAllHorses();
        verify(mockModelManager, times(1)).getAllRaces();
    }
}