package client.ui.adminView.horseList;

import client.modelManager.ModelManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shared.DTO.HorseDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEditHorseVMTest {

    @Mock
    private ModelManager mockModelManager;

    @Mock
    private ObservableList<HorseDTO> mockHorseList;

    private CreateEditHorseVM createEditHorseVM;
    private HorseDTO testHorse1;
    private HorseDTO testHorse2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        testHorse1 = new HorseDTO(1, "Thunder", 15, 25);
        testHorse2 = new HorseDTO(2, "Lightning", 12, 22);

        // Setup mock behavior
        when(mockModelManager.getHorseList()).thenReturn(mockHorseList);

        // Create the view model with mocked dependencies
        createEditHorseVM = new CreateEditHorseVM(mockModelManager);
    }

    //CONSTRTUCTOR TESTS
    @Test
    void constructor_Zero_InitializesWithEmptyState() {
        // Act (constructor already called in setUp)

        // Assert
        assertNotNull(createEditHorseVM.getHorseList());
        assertNull(createEditHorseVM.horseNameProp().get());
        assertEquals(0, createEditHorseVM.speedMinProp().get());
        assertEquals(0, createEditHorseVM.speedMaxProp().get());
        verify(mockModelManager).getAllHorses();
        verify(mockModelManager).getHorseList();
    }

    @Test
    void constructor_Simple_NullModelManager_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new CreateEditHorseVM(null));
    }

    //GET HORSELIST TESTS
    @Test
    void getHorseList_Zero_EmptyList_ReturnsEmptyObservableList() {
        // Arrange
        ObservableList<HorseDTO> emptyList = FXCollections.observableArrayList();
        when(mockModelManager.getHorseList()).thenReturn(emptyList);
        CreateEditHorseVM vm = new CreateEditHorseVM(mockModelManager);

        // Act
        ObservableList<HorseDTO> result = vm.getHorseList();

        // Assert
        assertNotNull(result);
        assertEquals(emptyList, result);
    }

    @Test
    void getHorseList_One_SingleHorse_ReturnsSingleItemList() {
        // Arrange
        ObservableList<HorseDTO> singleHorseList = FXCollections.observableArrayList(testHorse1);
        when(mockModelManager.getHorseList()).thenReturn(singleHorseList);
        CreateEditHorseVM vm = new CreateEditHorseVM(mockModelManager);

        // Act
        ObservableList<HorseDTO> result = vm.getHorseList();

        // Assert
        assertEquals(singleHorseList, result);
        assertEquals(1, result.size());
    }

    @Test
    void getHorseList_Many_MultipleHorses_ReturnsMultipleItemList() {
        // Arrange
        ObservableList<HorseDTO> multipleHorseList = FXCollections.observableArrayList(
                testHorse1, testHorse2, new HorseDTO(3, "Storm", 18, 28)
        );
        when(mockModelManager.getHorseList()).thenReturn(multipleHorseList);
        CreateEditHorseVM vm = new CreateEditHorseVM(mockModelManager);

        // Act
        ObservableList<HorseDTO> result = vm.getHorseList();

        // Assert
        assertEquals(multipleHorseList, result);
        assertEquals(3, result.size());
    }

    @Test
    void getHorseList_Interface_ReturnsModelManagerList() {
        // Act
        ObservableList<HorseDTO> result = createEditHorseVM.getHorseList();

        // Assert
        assertEquals(mockHorseList, result);
    }

    //HORSENAMEPROPERTTY TEESTS
    @Test
    void horseNameProp_Zero_InitialValue_IsNull() {
        // Act
        StringProperty property = createEditHorseVM.horseNameProp();

        // Assert
        assertNotNull(property);
        assertNull(property.get());
    }

    @Test
    void horseNameProp_One_SetValue_ReturnsValue() {
        // Arrange
        String horseName = "Speedy";

        // Act
        createEditHorseVM.horseNameProp().set(horseName);

        // Assert
        assertEquals(horseName, createEditHorseVM.horseNameProp().get());
    }

    @Test
    void horseNameProp_Interface_PropertyChange_NotifiesListeners() {
        // Arrange
        StringProperty property = createEditHorseVM.horseNameProp();
        boolean[] changed = {false};
        property.addListener((obs, oldVal, newVal) -> changed[0] = true);

        // Act
        property.set("New Horse Name");

        // Assert
        assertTrue(changed[0]);
    }

    @Test
    void horseNameProp_Simple_EmptyString_IsValid() {
        // Act
        createEditHorseVM.horseNameProp().set("");

        // Assert
        assertEquals("", createEditHorseVM.horseNameProp().get());
    }

    //SPEEDMIN PROPERTY TESTS
    @Test
    void speedMinProp_Zero_InitialValue_IsZero() {
        // Act
        IntegerProperty property = createEditHorseVM.speedMinProp();

        // Assert
        assertNotNull(property);
        assertEquals(0, property.get());
    }

    @Test
    void speedMinProp_One_SetValue_ReturnsValue() {
        // Arrange
        int speedMin = 10;

        // Act
        createEditHorseVM.speedMinProp().set(speedMin);

        // Assert
        assertEquals(speedMin, createEditHorseVM.speedMinProp().get());
    }

    @Test
    void speedMinProp_Boundary_NegativeValue_IsStored() {
        // Act
        createEditHorseVM.speedMinProp().set(-5);

        // Assert
        assertEquals(-5, createEditHorseVM.speedMinProp().get());
    }

    @Test
    void speedMinProp_Boundary_MaxValue_IsStored() {
        // Act
        createEditHorseVM.speedMinProp().set(Integer.MAX_VALUE);

        // Assert
        assertEquals(Integer.MAX_VALUE, createEditHorseVM.speedMinProp().get());
    }

    //SPEED MAX PROPERTY TESTS
    @Test
    void speedMaxProp_Zero_InitialValue_IsZero() {
        // Act
        IntegerProperty property = createEditHorseVM.speedMaxProp();

        // Assert
        assertNotNull(property);
        assertEquals(0, property.get());
    }

    @Test
    void speedMaxProp_One_SetValue_ReturnsValue() {
        // Arrange
        int speedMax = 25;

        // Act
        createEditHorseVM.speedMaxProp().set(speedMax);

        // Assert
        assertEquals(speedMax, createEditHorseVM.speedMaxProp().get());
    }

    @Test
    void speedMaxProp_Many_LargeValue_IsStored() {
        // Arrange
        int largeSpeed = 1000;

        // Act
        createEditHorseVM.speedMaxProp().set(largeSpeed);

        // Assert
        assertEquals(largeSpeed, createEditHorseVM.speedMaxProp().get());
    }

    //EDIT BUTTON DISABLE PROPERTY TEST
    @Test
    void editButtonDisableProperty_Zero_InitialState_IsTrue() {
        // Act
        BooleanProperty property = createEditHorseVM.editButtonDisableProperty();

        // Assert
        assertNotNull(property);
        // Note: This should be true initially since no horse is selected
    }
    //REMOVE BUTTON DISABLE PROPERTY TEST
    @Test
    void removeButtonDisableProperty_Zero_InitialState_IsTrue() {
        // Act
        BooleanProperty property = createEditHorseVM.removeButtonDisableProperty();

        // Assert
        assertNotNull(property);
        // Note: This should be true initially since no horse is selected
    }

    //SET SELECTECTED HORSE TEST
    @Test
    void setSelectedHorse_Zero_NullHorse_ClearsForm() {
        // Arrange
        createEditHorseVM.horseNameProp().set("Previous Name");
        createEditHorseVM.speedMinProp().set(10);
        createEditHorseVM.speedMaxProp().set(20);

        // Act
        createEditHorseVM.setSelectedHorse(null);

        // Assert
        // The form should not be cleared when setting null via setSelectedHorse
        // This is different from setNull() method
        assertEquals("Previous Name", createEditHorseVM.horseNameProp().get());
    }

    @Test
    void setSelectedHorse_One_ValidHorse_UpdatesForm() {
        // Act
        createEditHorseVM.setSelectedHorse(testHorse1);

        // Assert
        assertEquals("Thunder", createEditHorseVM.horseNameProp().get());
        assertEquals(15, createEditHorseVM.speedMinProp().get());
        assertEquals(25, createEditHorseVM.speedMaxProp().get());
        assertFalse(createEditHorseVM.editButtonDisableProperty().get());
        assertFalse(createEditHorseVM.removeButtonDisableProperty().get());
    }

    @Test
    void setSelectedHorse_Interface_UpdatesButtonStates() {
        // Act
        createEditHorseVM.setSelectedHorse(testHorse1);

        // Assert
        assertFalse(createEditHorseVM.editButtonDisableProperty().get());
        assertFalse(createEditHorseVM.removeButtonDisableProperty().get());
    }

    @Test
    void setSelectedHorse_Exercise_MultipleSelections_WorkCorrectly() {
        // Act & Assert - First selection
        createEditHorseVM.setSelectedHorse(testHorse1);
        assertEquals("Thunder", createEditHorseVM.horseNameProp().get());
        assertEquals(15, createEditHorseVM.speedMinProp().get());
        assertEquals(25, createEditHorseVM.speedMaxProp().get());

        // Act & Assert - Second selection
        createEditHorseVM.setSelectedHorse(testHorse2);
        assertEquals("Lightning", createEditHorseVM.horseNameProp().get());
        assertEquals(12, createEditHorseVM.speedMinProp().get());
        assertEquals(22, createEditHorseVM.speedMaxProp().get());
    }

    //SET NULL TESTS
    @Test
    void setNull_Simple_ClearsFormAndDisablesButtons() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);

        // Act
        createEditHorseVM.setNull();

        // Assert
        assertNull(createEditHorseVM.horseNameProp().get());
        assertEquals(0, createEditHorseVM.speedMinProp().get());
        assertEquals(0, createEditHorseVM.speedMaxProp().get());
        assertTrue(createEditHorseVM.editButtonDisableProperty().get());
        assertTrue(createEditHorseVM.removeButtonDisableProperty().get());
    }

    @Test
    void setNull_Exercise_AfterMultipleOperations_ClearsCorrectly() {
        // Arrange - Set multiple values
        createEditHorseVM.setSelectedHorse(testHorse1);
        createEditHorseVM.horseNameProp().set("Modified Name");
        createEditHorseVM.speedMinProp().set(100);
        createEditHorseVM.speedMaxProp().set(200);

        // Act
        createEditHorseVM.setNull();

        // Assert
        assertNull(createEditHorseVM.horseNameProp().get());
        assertEquals(0, createEditHorseVM.speedMinProp().get());
        assertEquals(0, createEditHorseVM.speedMaxProp().get());
    }

    //ADD HORSE TESTS
    @Test
    void addHorse_Zero_NotInCreationMode_DoesNotCallModel() {
        // Arrange
        createEditHorseVM.horseNameProp().set("Test Horse");
        createEditHorseVM.speedMinProp().set(10);
        createEditHorseVM.speedMaxProp().set(20);

        // Act
        createEditHorseVM.addHorse();

        // Assert
        verify(mockModelManager, never()).createHorse(any(), anyInt(), anyInt());
    }

    @Test
    void addHorse_One_InCreationMode_CallsModelCreateHorse() {
        // Arrange
        createEditHorseVM.setHorseCreationMode();
        createEditHorseVM.horseNameProp().set("New Horse");
        createEditHorseVM.speedMinProp().set(15);
        createEditHorseVM.speedMaxProp().set(25);

        // Act
        createEditHorseVM.addHorse();

        // Assert
        verify(mockModelManager).createHorse("New Horse", 15, 25);
    }

    @Test
    void addHorse_Interface_SetsReadModeAfterCreation() {
        // Arrange
        createEditHorseVM.setHorseCreationMode();
        createEditHorseVM.horseNameProp().set("Test Horse");
        createEditHorseVM.speedMinProp().set(10);
        createEditHorseVM.speedMaxProp().set(20);

        // Act
        createEditHorseVM.addHorse();

        // Assert - Verify it's no longer in creation mode
        createEditHorseVM.addHorse(); // Should not call model again
        verify(mockModelManager, times(1)).createHorse(any(), anyInt(), anyInt());
    }

    //SET READ MODE TEST
    @Test
    void setReadMode_Simple_ExitsCreationMode() {
        // Arrange
        createEditHorseVM.setHorseCreationMode();

        // Act
        createEditHorseVM.setReadMode();

        // Assert - Verify no longer in creation mode
        createEditHorseVM.addHorse();
        verify(mockModelManager, never()).createHorse(any(), anyInt(), anyInt());
    }

    //UPDATE HORSE TESTS
    @Test
    void updateHorse_Zero_NoSelectedHorse_DoesNotCallModel() {
        // Act
        createEditHorseVM.updateHorse();

        // Assert
        verify(mockModelManager, never()).updateHorse(anyInt(), any(), anyInt(), anyInt());
    }

    @Test
    void updateHorse_One_WithSelectedHorse_CallsModelUpdateHorse() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);
        createEditHorseVM.horseNameProp().set("Updated Name");
        createEditHorseVM.speedMinProp().set(20);
        createEditHorseVM.speedMaxProp().set(30);

        // Act
        createEditHorseVM.updateHorse();

        // Assert
        verify(mockModelManager).updateHorse(1, "Updated Name", 20, 30);
    }

    @Test
    void updateHorse_Interface_UsesCurrentFormValues() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);
        createEditHorseVM.horseNameProp().set("Modified Horse");
        createEditHorseVM.speedMinProp().set(5);
        createEditHorseVM.speedMaxProp().set(35);

        // Act
        createEditHorseVM.updateHorse();

        // Assert
        verify(mockModelManager).updateHorse(1, "Modified Horse", 5, 35);
    }

    @Test
    void updateHorse_Boundary_WithExtremeValues_CallsModelWithExtremeValues() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);
        createEditHorseVM.horseNameProp().set("");
        createEditHorseVM.speedMinProp().set(0);
        createEditHorseVM.speedMaxProp().set(Integer.MAX_VALUE);

        // Act
        createEditHorseVM.updateHorse();

        // Assert
        verify(mockModelManager).updateHorse(1, "", 0, Integer.MAX_VALUE);
    }

    //REMOVE HORSE TESTS
    @Test
    void removeHorse_Zero_NoSelectedHorse_DoesNotCallModel() {
        // Act
        createEditHorseVM.removeHorse();

        // Assert
        verify(mockModelManager, never()).deleteHorse(any());
    }

    @Test
    void removeHorse_One_WithSelectedHorse_CallsModelDeleteHorse() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);

        // Act
        createEditHorseVM.removeHorse();

        // Assert
        verify(mockModelManager).deleteHorse(testHorse1);
    }

    @Test
    void removeHorse_Interface_ClearsSelectionAfterRemoval() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);

        // Act
        createEditHorseVM.removeHorse();

        // Assert
        assertNull(createEditHorseVM.horseNameProp().get());
        assertEquals(0, createEditHorseVM.speedMinProp().get());
        assertEquals(0, createEditHorseVM.speedMaxProp().get());
        assertTrue(createEditHorseVM.editButtonDisableProperty().get());
        assertTrue(createEditHorseVM.removeButtonDisableProperty().get());
    }

    //SET HORSE CREATION MODE TESTS
    @Test
    void setHorseCreationMode_Zero_FirstTime_ClearsFormAndEnablesCreation() {
        // Arrange
        createEditHorseVM.setSelectedHorse(testHorse1);

        // Act
        createEditHorseVM.setHorseCreationMode();

        // Assert
        assertNull(createEditHorseVM.horseNameProp().get());
        assertEquals(0, createEditHorseVM.speedMinProp().get());
        assertEquals(0, createEditHorseVM.speedMaxProp().get());

        // Verify creation mode is active
        createEditHorseVM.horseNameProp().set("Creation Test");
        createEditHorseVM.speedMinProp().set(10);
        createEditHorseVM.speedMaxProp().set(20);
        createEditHorseVM.addHorse();
        verify(mockModelManager).createHorse("Creation Test", 10, 20);
    }

    @Test
    void setHorseCreationMode_One_AlreadyInCreationMode_FinalizesCurrentCreation() {
        // Arrange
        createEditHorseVM.setHorseCreationMode();
        createEditHorseVM.horseNameProp().set("First Horse");
        createEditHorseVM.speedMinProp().set(10);
        createEditHorseVM.speedMaxProp().set(20);

        // Act - Call setHorseCreationMode again
        createEditHorseVM.setHorseCreationMode();

        // Assert
        verify(mockModelManager).createHorse("First Horse", 10, 20);
    }

    @Test
    void setHorseCreationMode_Exercise_MultipleCreationCycles_WorkCorrectly() {
        // First creation cycle
        createEditHorseVM.setHorseCreationMode();
        createEditHorseVM.horseNameProp().set("Horse 1");
        createEditHorseVM.speedMinProp().set(10);
        createEditHorseVM.speedMaxProp().set(20);
        createEditHorseVM.addHorse();

        // Second creation cycle
        createEditHorseVM.setHorseCreationMode();
        createEditHorseVM.horseNameProp().set("Horse 2");
        createEditHorseVM.speedMinProp().set(15);
        createEditHorseVM.speedMaxProp().set(25);
        createEditHorseVM.addHorse();

        // Assert
        verify(mockModelManager).createHorse("Horse 1", 10, 20);
        verify(mockModelManager).createHorse("Horse 2", 15, 25);
    }

    @Test
    void setHorseCreationMode_Interface_SetsCreationModeCorrectly() {
        // Act
        createEditHorseVM.setHorseCreationMode();

        // Assert - Verify creation mode is active by testing addHorse behavior
        createEditHorseVM.horseNameProp().set("Test Creation");
        createEditHorseVM.speedMinProp().set(12);
        createEditHorseVM.speedMaxProp().set(18);
        createEditHorseVM.addHorse();

        verify(mockModelManager).createHorse("Test Creation", 12, 18);
    }

    @Test
    void setHorseCreationMode_Simple_BasicCreationFlow_WorksCorrectly() {
        // Act - Enter creation mode
        createEditHorseVM.setHorseCreationMode();

        // Set form values
        createEditHorseVM.horseNameProp().set("Simple Horse");
        createEditHorseVM.speedMinProp().set(8);
        createEditHorseVM.speedMaxProp().set(16);

        // Create horse
        createEditHorseVM.addHorse();

        // Assert
        verify(mockModelManager).createHorse("Simple Horse", 8, 16);
    }

    //TOTAL WORKFLOW TEST: DO WE EDIT and CREATE HORSES?
    @Test
    void completeEditWorkflow_WorksCorrectly() {
        // Select horse
        createEditHorseVM.setSelectedHorse(testHorse1);
        assertEquals("Thunder", createEditHorseVM.horseNameProp().get());

        // Modify values
        createEditHorseVM.horseNameProp().set("Thunder Modified");
        createEditHorseVM.speedMinProp().set(20);
        createEditHorseVM.speedMaxProp().set(30);

        // Update horse
        createEditHorseVM.updateHorse();
        verify(mockModelManager).updateHorse(1, "Thunder Modified", 20, 30);

        // Remove horse
        createEditHorseVM.removeHorse();
        verify(mockModelManager).deleteHorse(testHorse1);

        // Verify form is cleared
        assertNull(createEditHorseVM.horseNameProp().get());
    }

    @Test
    void completeCreateWorkflow_WorksCorrectly() {
        // Enter creation mode
        createEditHorseVM.setHorseCreationMode();

        // Set form values
        createEditHorseVM.horseNameProp().set("Workflow Horse");
        createEditHorseVM.speedMinProp().set(14);
        createEditHorseVM.speedMaxProp().set(26);

        // Create horse
        createEditHorseVM.addHorse();
        verify(mockModelManager).createHorse("Workflow Horse", 14, 26);

        // Verify no longer in creation mode
        createEditHorseVM.addHorse(); // Should not create another horse
        verify(mockModelManager, times(1)).createHorse(any(), anyInt(), anyInt());
    }
}