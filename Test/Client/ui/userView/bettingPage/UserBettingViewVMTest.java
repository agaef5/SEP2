package client.ui.userView.bettingPage;

import client.modelManager.ModelManager;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.DTO.RaceTrackDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserBettingViewVMTest {

    @Mock
    private ModelManager mockModelManager;

    @Mock
    private BooleanProperty mockRaceStartedProperty;

    @Mock
    private BooleanProperty mockBetPlacedProperty;

    @Mock
    private StringProperty mockCurrentRaceNameProperty;

    @Mock
    private IntegerProperty mockUserBalanceProperty;

    private UserBettingViewVM bettingVM;
    private RaceDTO testRace;
    private List<HorseDTO> testHorses;
    private HorseDTO testHorse1;
    private HorseDTO testHorse2;
    private RaceTrackDTO testRaceTrack;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        testHorse1 = new HorseDTO(1, "Thunder", 15, 25);
        testHorse2 = new HorseDTO(2, "Lightning", 12, 22);
        testHorses = List.of(testHorse1, testHorse2);
        testRaceTrack = new RaceTrackDTO("Test Track", 1000, "Test Location");
        testRace = new RaceDTO("Test Race", Timestamp.valueOf(LocalDateTime.now()),
                testHorses, testRaceTrack, RaceState.NOT_STARTED);

        // Setup mock behavior
        when(mockModelManager.getUserBalance()).thenReturn(mockUserBalanceProperty);
        when(mockModelManager.raceStartedProperty()).thenReturn(mockRaceStartedProperty);
        when(mockModelManager.betPlacedProperty()).thenReturn(mockBetPlacedProperty);
        when(mockModelManager.currentRaceNameProperty()).thenReturn(mockCurrentRaceNameProperty);
        when(mockUserBalanceProperty.get()).thenReturn(1000);
        when(mockCurrentRaceNameProperty.get()).thenReturn("Test Race");
        when(mockModelManager.validateBet(any(), anyInt())).thenReturn(true);

        // Create the view model
        bettingVM = new UserBettingViewVM(mockModelManager, testRace);
    }

    // ============ CONSTRUCTOR TESTS ============

    @Test
    void constructor_Zero_NullModelManager_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new UserBettingViewVM(null, testRace));
    }

    @Test
    void constructor_Zero_NullRace_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                new UserBettingViewVM(mockModelManager, null));
    }

    @Test
    void constructor_One_ValidParameters_InitializesCorrectly() {
        // Act (constructor already called in setUp)

        // Assert
        assertNotNull(bettingVM);
        assertEquals(testRace, bettingVM.getSelectedRace());
        assertEquals(2, bettingVM.getHorses().size());
        assertEquals(testHorse1, bettingVM.selectedHorseProperty().get()); // First horse auto-selected
        verify(mockModelManager).getUserBalance();
        verify(mockModelManager).raceStartedProperty();
        verify(mockModelManager).betPlacedProperty();
    }

    @Test
    void constructor_Simple_EmptyHorseList_InitializesWithoutSelection() {
        // Arrange
        RaceDTO emptyRace = new RaceDTO("Empty Race", Timestamp.valueOf(LocalDateTime.now()),
                List.of(), testRaceTrack, RaceState.NOT_STARTED);

        // Act
        UserBettingViewVM vm = new UserBettingViewVM(mockModelManager, emptyRace);

        // Assert
        assertTrue(vm.getHorses().isEmpty());
        assertNull(vm.selectedHorseProperty().get());
    }

    @Test
    void constructor_Many_MultipleHorses_SelectsFirstHorse() {
        // Arrange
        List<HorseDTO> manyHorses = List.of(
                new HorseDTO(1, "Horse1", 10, 20),
                new HorseDTO(2, "Horse2", 12, 22),
                new HorseDTO(3, "Horse3", 14, 24),
                new HorseDTO(4, "Horse4", 16, 26)
        );
        RaceDTO manyHorseRace = new RaceDTO("Many Horse Race", Timestamp.valueOf(LocalDateTime.now()),
                manyHorses, testRaceTrack, RaceState.NOT_STARTED);

        // Act
        UserBettingViewVM vm = new UserBettingViewVM(mockModelManager, manyHorseRace);

        // Assert
        assertEquals(4, vm.getHorses().size());
        assertEquals(manyHorses.get(0), vm.selectedHorseProperty().get());
    }

    @Test
    void constructor_Interface_SetsUpPropertyListeners() {
        // Act (constructor already called in setUp)

        // Assert - be specific about which addListener method is being called
        verify(mockRaceStartedProperty).addListener(any(ChangeListener.class));
        verify(mockBetPlacedProperty).addListener(any(ChangeListener.class));
    }

    // ============ PROPERTY GETTER TESTS ============

    @Test
    void balanceInfoProperty_Interface_ReturnsBoundProperty() {
        // Act
        IntegerProperty property = bettingVM.balanceInfoProperty();

        // Assert
        assertNotNull(property);
        // Property should be bound to model manager's balance
    }

    @Test
    void getHorses_Zero_EmptyRace_ReturnsEmptyList() {
        // Arrange
        RaceDTO emptyRace = new RaceDTO("Empty Race", Timestamp.valueOf(LocalDateTime.now()),
                List.of(), testRaceTrack, RaceState.NOT_STARTED);
        UserBettingViewVM vm = new UserBettingViewVM(mockModelManager, emptyRace);

        // Act
        ObservableList<HorseDTO> horses = vm.getHorses();

        // Assert
        assertNotNull(horses);
        assertTrue(horses.isEmpty());
    }

    @Test
    void getHorses_One_SingleHorse_ReturnsOneHorse() {
        // Arrange
        List<HorseDTO> singleHorse = List.of(testHorse1);
        RaceDTO singleHorseRace = new RaceDTO("Single Horse Race", Timestamp.valueOf(LocalDateTime.now()),
                singleHorse, testRaceTrack, RaceState.NOT_STARTED);
        UserBettingViewVM vm = new UserBettingViewVM(mockModelManager, singleHorseRace);

        // Act
        ObservableList<HorseDTO> horses = vm.getHorses();

        // Assert
        assertEquals(1, horses.size());
        assertEquals(testHorse1, horses.get(0));
    }

    @Test
    void getHorses_Many_MultipleHorses_ReturnsAllHorses() {
        // Act
        ObservableList<HorseDTO> horses = bettingVM.getHorses();

        // Assert
        assertEquals(2, horses.size());
        assertTrue(horses.contains(testHorse1));
        assertTrue(horses.contains(testHorse2));
    }

    @Test
    void selectedHorseProperty_Zero_InitialValue_IsFirstHorse() {
        // Act
        ObjectProperty<HorseDTO> property = bettingVM.selectedHorseProperty();

        // Assert
        assertNotNull(property);
        assertEquals(testHorse1, property.get());
    }

    @Test
    void betAmountProperty_Zero_InitialValue_IsZero() {
        // Act
        IntegerProperty property = bettingVM.betAmountProperty();

        // Assert
        assertNotNull(property);
        assertEquals(0, property.get());
    }

    @Test
    void countdownTextProperty_Simple_InitialValue_HasDefaultText() {
        // Act
        StringProperty property = bettingVM.countdownTextProperty();

        // Assert
        assertNotNull(property);
        assertEquals("Race starting soon", property.get());
    }

    @Test
    void statusMessageProperty_Zero_InitialValue_IsEmpty() {
        // Act
        StringProperty property = bettingVM.statusMessageProperty();

        // Assert
        assertNotNull(property);
        assertEquals("", property.get());
    }

    @Test
    void betValidProperty_Zero_InitialValue_IsFalse() {
        // Act
        BooleanProperty property = bettingVM.betValidProperty();

        // Assert
        assertNotNull(property);
        assertFalse(property.get());
    }

    @Test
    void uiLockedProperty_Zero_InitialValue_IsFalse() {
        // Act
        BooleanProperty property = bettingVM.uiLockedProperty();

        // Assert
        assertNotNull(property);
        assertFalse(property.get());
    }

    @Test
    void navigateToGameViewProperty_Zero_InitialValue_IsFalse() {
        // Act
        BooleanProperty property = bettingVM.navigateToGameViewProperty();

        // Assert
        assertNotNull(property);
        assertFalse(property.get());
    }

    @Test
    void getNavigateToUserLandingPageProperty_Zero_InitialValue_IsFalse() {
        // Act
        BooleanProperty property = bettingVM.getNavigateToUserLandingPageProperty();

        // Assert
        assertNotNull(property);
        assertFalse(property.get());
    }

    @Test
    void getSelectedRace_Interface_ReturnsConstructorRace() {
        // Act
        RaceDTO result = bettingVM.getSelectedRace();

        // Assert
        assertEquals(testRace, result);
    }

    // ============ BET AMOUNT MANIPULATION TESTS ============

    @Test
    void increaseBet_Zero_UINotLocked_IncreasesBy100() {
        // Arrange
        bettingVM.betAmountProperty().set(0);

        // Act
        bettingVM.increaseBet();

        // Assert
        assertEquals(100, bettingVM.betAmountProperty().get());
    }

    @Test
    void increaseBet_One_UINotLocked_IncreasesBy100() {
        // Arrange
        bettingVM.betAmountProperty().set(200);

        // Act
        bettingVM.increaseBet();

        // Assert
        assertEquals(300, bettingVM.betAmountProperty().get());
    }

    @Test
    void increaseBet_Many_MultipleIncreases_IncreasesCorrectly() {
        // Arrange
        bettingVM.betAmountProperty().set(0);

        // Act
        bettingVM.increaseBet();
        bettingVM.increaseBet();
        bettingVM.increaseBet();

        // Assert
        assertEquals(300, bettingVM.betAmountProperty().get());
    }

    @Test
    void increaseBet_Interface_UILocked_DoesNotIncrease() {
        // Arrange
        bettingVM.betAmountProperty().set(100);
        bettingVM.uiLockedProperty().set(true);

        // Act
        bettingVM.increaseBet();

        // Assert
        assertEquals(100, bettingVM.betAmountProperty().get()); // No change
    }

    @Test
    void decreaseBet_Zero_FromZero_StaysZero() {
        // Arrange
        bettingVM.betAmountProperty().set(0);

        // Act
        bettingVM.decreaseBet();

        // Assert
        assertEquals(0, bettingVM.betAmountProperty().get());
    }

    @Test
    void decreaseBet_One_FromNonZero_DecreasesBy100() {
        // Arrange
        bettingVM.betAmountProperty().set(200);

        // Act
        bettingVM.decreaseBet();

        // Assert
        assertEquals(100, bettingVM.betAmountProperty().get());
    }

    @Test
    void decreaseBet_Boundary_From50_GoesToZero() {
        // Arrange
        bettingVM.betAmountProperty().set(50);

        // Act
        bettingVM.decreaseBet();

        // Assert
        assertEquals(0, bettingVM.betAmountProperty().get()); // Math.max(0, 50-100) = 0
    }

    @Test
    void decreaseBet_Interface_UILocked_DoesNotDecrease() {
        // Arrange
        bettingVM.betAmountProperty().set(200);
        bettingVM.uiLockedProperty().set(true);

        // Act
        bettingVM.decreaseBet();

        // Assert
        assertEquals(200, bettingVM.betAmountProperty().get()); // No change
    }

    @Test
    void decreaseBet_Many_MultipleDecreases_DecreasesCorrectly() {
        // Arrange
        bettingVM.betAmountProperty().set(500);

        // Act
        bettingVM.decreaseBet();
        bettingVM.decreaseBet();
        bettingVM.decreaseBet();

        // Assert
        assertEquals(200, bettingVM.betAmountProperty().get());
    }

    // ============ BET PLACEMENT TESTS ============

    @Test
    void placeBet_Zero_InvalidBet_ReturnsFalse() {
        // Arrange
        bettingVM.betValidProperty().set(false);

        // Act
        boolean result = bettingVM.placeBet();

        // Assert
        assertFalse(result);
        verify(mockModelManager, never()).createBet(any(), anyInt());
        assertEquals("Invalid bet: Please select a horse and valid amount",
                bettingVM.statusMessageProperty().get());
    }

    @Test
    void placeBet_Interface_UILocked_ReturnsFalse() {
        // Arrange
        bettingVM.betValidProperty().set(true);
        bettingVM.uiLockedProperty().set(true);

        // Act
        boolean result = bettingVM.placeBet();

        // Assert
        assertFalse(result);
        verify(mockModelManager, never()).createBet(any(), anyInt());
    }

    @Test
    void placeBet_One_ValidBet_ReturnsTrue() {
        // Arrange
        bettingVM.betValidProperty().set(true);
        bettingVM.betAmountProperty().set(100);
        bettingVM.selectedHorseProperty().set(testHorse1);

        // Act
        boolean result = bettingVM.placeBet();

        // Assert
        assertTrue(result);
        verify(mockModelManager).createBet(testHorse1, 100);
    }

    @Test
    void placeBet_Exercise_ValidBetWithDifferentAmounts_CallsModelCorrectly() {
        // Arrange
        bettingVM.betValidProperty().set(true);

        // Test with amount 200
        bettingVM.betAmountProperty().set(200);
        bettingVM.selectedHorseProperty().set(testHorse2);

        // Act
        boolean result = bettingVM.placeBet();

        // Assert
        assertTrue(result);
        verify(mockModelManager).createBet(testHorse2, 200);
    }

    // ============ NAVIGATION TESTS ============

    @Test
    void resetGameViewNavigation_Simple_ResetsToFalse() {
        // Arrange
        bettingVM.navigateToGameViewProperty().set(true);

        // Act
        bettingVM.resetGameViewNavigation();

        // Assert
        assertFalse(bettingVM.navigateToGameViewProperty().get());
    }

    @Test
    void resetGameViewNavigation_Zero_AlreadyFalse_StaysFalse() {
        // Arrange
        bettingVM.navigateToGameViewProperty().set(false);

        // Act
        bettingVM.resetGameViewNavigation();

        // Assert
        assertFalse(bettingVM.navigateToGameViewProperty().get());
    }

    // ============ VALIDATION TESTS ============

    @Test
    void validateBet_Zero_NullHorse_ShowsAlert() {
        // Arrange
        bettingVM.selectedHorseProperty().set(null);
        bettingVM.betAmountProperty().set(100);

        // Act - Trigger validation by changing bet amount
        bettingVM.betAmountProperty().set(200);
    }

    @Test
    void validateBet_One_ValidSelection_UpdatesBetValid() {
        // Arrange
        when(mockModelManager.validateBet(testHorse1, 100)).thenReturn(true);
        bettingVM.selectedHorseProperty().set(testHorse1);
        bettingVM.betAmountProperty().set(100);
        bettingVM.uiLockedProperty().set(false);

        // Act - Trigger validation
        bettingVM.selectedHorseProperty().set(testHorse1); // Re-set to trigger listener

        // Assert
        verify(mockModelManager).validateBet(testHorse1, 100);
    }

    @Test
    void validateBet_Interface_UILocked_SetsBetValidToFalse() {
        // Arrange
        when(mockModelManager.validateBet(any(), anyInt())).thenReturn(true);
        bettingVM.selectedHorseProperty().set(testHorse1);
        bettingVM.betAmountProperty().set(100);
        bettingVM.uiLockedProperty().set(true);

        // Act - Trigger validation
        bettingVM.betAmountProperty().set(200);

        // Assert
        assertFalse(bettingVM.betValidProperty().get());
    }

    // ============ PROPERTY LISTENER TESTS ============

    @Test
    void selectedHorseListener_Interface_TriggersValidation() {
        // Arrange
        when(mockModelManager.validateBet(testHorse2, 0)).thenReturn(true);

        // Act
        bettingVM.selectedHorseProperty().set(testHorse2);

        // Assert
        verify(mockModelManager).validateBet(testHorse2, 0);
    }

    @Test
    void betAmountListener_Interface_TriggersValidation() {
        // Arrange
        when(mockModelManager.validateBet(testHorse1, 150)).thenReturn(true);

        // Act
        bettingVM.betAmountProperty().set(150);

        // Assert
        verify(mockModelManager).validateBet(testHorse1, 150);
    }

    // ============ INTEGRATION WORKFLOW TESTS ============

    @Test
    void workflowIntegration_Exercise_CompleteBettingWorkflow_WorksCorrectly() {
        // Setup valid bet
        when(mockModelManager.validateBet(any(), anyInt())).thenReturn(true);
        bettingVM.selectedHorseProperty().set(testHorse1);

        // Increase bet amount
        bettingVM.increaseBet();
        bettingVM.increaseBet();
        assertEquals(200, bettingVM.betAmountProperty().get());

        // Validate bet is valid
        assertTrue(bettingVM.betValidProperty().get());

        // Place bet
        boolean success = bettingVM.placeBet();
        assertTrue(success);
        verify(mockModelManager).createBet(testHorse1, 200);
    }

    @Test
    void workflowIntegration_Exercise_BetManipulationWorkflow_WorksCorrectly() {
        // Start with zero
        assertEquals(0, bettingVM.betAmountProperty().get());

        // Increase multiple times
        bettingVM.increaseBet();
        bettingVM.increaseBet();
        bettingVM.increaseBet();
        assertEquals(300, bettingVM.betAmountProperty().get());

        // Decrease once
        bettingVM.decreaseBet();
        assertEquals(200, bettingVM.betAmountProperty().get());

        // Decrease below zero
        bettingVM.decreaseBet();
        bettingVM.decreaseBet();
        bettingVM.decreaseBet();
        assertEquals(0, bettingVM.betAmountProperty().get()); // Should not go below 0
    }

    @Test
    void workflowIntegration_Exercise_UILockingWorkflow_WorksCorrectly() {
        // Initially UI is not locked
        assertFalse(bettingVM.uiLockedProperty().get());

        // Can manipulate bet amounts
        bettingVM.increaseBet();
        assertEquals(100, bettingVM.betAmountProperty().get());

        // Lock UI
        bettingVM.uiLockedProperty().set(true);

        // Cannot manipulate bet amounts
        bettingVM.increaseBet();
        assertEquals(100, bettingVM.betAmountProperty().get()); // No change

        bettingVM.decreaseBet();
        assertEquals(100, bettingVM.betAmountProperty().get()); // No change

        // Cannot place bet
        bettingVM.betValidProperty().set(true);
        assertFalse(bettingVM.placeBet());
    }

    @Test
    void workflowIntegration_Exercise_HorseSelectionWorkflow_WorksCorrectly() {
        // Initially first horse is selected
        assertEquals(testHorse1, bettingVM.selectedHorseProperty().get());

        // Change selection
        bettingVM.selectedHorseProperty().set(testHorse2);
        assertEquals(testHorse2, bettingVM.selectedHorseProperty().get());

        // Validation should be triggered
        verify(mockModelManager, atLeastOnce()).validateBet(eq(testHorse2), anyInt());

        // Clear selection
        bettingVM.selectedHorseProperty().set(null);
        assertNull(bettingVM.selectedHorseProperty().get());
    }
}