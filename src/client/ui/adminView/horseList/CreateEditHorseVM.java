package client.ui.adminView.horseList;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;

/**
 * ViewModel for managing horse creation, editing, and deletion in the admin interface.
 *
 * Exposes bindable properties for the form fields and buttons, and
 * communicates with the ModelManager to perform data operations.
 */
public class CreateEditHorseVM implements ViewModel {

  private final ModelManager model;

  // –– backing lists & props ––
  private final ObservableList<HorseDTO> horseList;
  private final StringProperty horseName = new SimpleStringProperty();
  private final IntegerProperty speedMin = new SimpleIntegerProperty();
  private final IntegerProperty speedMax = new SimpleIntegerProperty();
  private final BooleanProperty createButtonDisableProperty = new SimpleBooleanProperty();
  private final BooleanProperty editButtonDisableProperty = new SimpleBooleanProperty();
  private final BooleanProperty removeButtonDisableProperty = new SimpleBooleanProperty();
  private HorseDTO selectedHorse;
  private boolean creationMode;

  /**
   * Constructs the ViewModel and loads the initial horse list from the model.
   *
   * @param model the shared ModelManager for accessing and modifying horse data
   */
  public CreateEditHorseVM(ModelManager model) {
    this.model = model;
    this.horseList = model.getHorseList();

    // bootstrap the list
    model.getAllHorses();
    creationMode = false;
  }

  // — property getters for binding —

  /** @return the observable list of horses */
  public ObservableList<HorseDTO> getHorseList() { return horseList; }

  /** @return bindable property for horse name input */
  public StringProperty horseNameProp() { return horseName; }

  /** @return bindable property for minimum speed input */
  public IntegerProperty speedMinProp() { return speedMin; }

  /** @return bindable property for maximum speed input */
  public IntegerProperty speedMaxProp() { return speedMax; }

  /** @return bindable property to control the edit button's enabled state */
  public BooleanProperty editButtonDisableProperty() { return createButtonDisableProperty; }

  /** @return bindable property to control the remove button's enabled state */
  public BooleanProperty removeButtonDisableProperty() { return removeButtonDisableProperty; }

  /**
   * Sets the selected horse and updates form fields and button states accordingly.
   *
   * @param newVal the newly selected horse, or null if no selection
   */
  public void setSelectedHorse(HorseDTO newVal) {
    this.selectedHorse = newVal;
    if (newVal != null) {
      horseName.set(newVal.name());
      speedMin.set(newVal.speedMin());
      speedMax.set(newVal.speedMax());

      editButtonDisableProperty().set(false);
      removeButtonDisableProperty().set(false);
    }
  }

  /**
   * Clears the selected horse and resets form fields and buttons to default state.
   */
  public void setNull() {
    this.selectedHorse = null;
    horseName.set(null);
    speedMin.set(0);
    speedMax.set(0);

    editButtonDisableProperty().set(true);
    removeButtonDisableProperty().set(true);
  }

  /**
   * Adds a new horse using the current form values.
   * Only executes if creation mode is active.
   */
  public void addHorse() {
    if (!creationMode) return;

    model.createHorse(horseName.get(), speedMin.get(), speedMax.get());

    setReadMode();
  }

  /**
   * Sets the view to read-only mode by disabling creation actions.
   */
  public void setReadMode() {
    creationMode = false;
  }

  /**
   * Updates the currently selected horse with the form values.
   * Only executes if a horse is selected.
   */
  public void updateHorse() {
    if (selectedHorse != null) {
      model.updateHorse(selectedHorse.id(), horseName.get(), speedMin.get(), speedMax.get());
    }
  }

  /**
   * Removes the currently selected horse.
   * Clears the selection afterward.
   */
  public void removeHorse() {
    if (selectedHorse != null) {
      model.deleteHorse(selectedHorse);
      setNull();
    }
  }

  /**
   * Activates horse creation mode.
   * If already in creation mode, finalizes the current horse creation first.
   */
  public void setHorseCreationMode() {
    if (creationMode) addHorse();

    setNull();
    creationMode = true;
  }
}
