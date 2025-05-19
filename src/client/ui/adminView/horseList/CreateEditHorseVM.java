// client/ui/adminView/horseList/HorseListViewModel.java
package client.ui.adminView.horseList;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;

import javafx.beans.property.*;

import javafx.collections.ObservableList;

import shared.DTO.HorseDTO;


public class CreateEditHorseVM implements ViewModel {
  private final ModelManager model;

  // –– backing lists & props ––
  private final ObservableList<HorseDTO> horseList;
  private final StringProperty horseName        = new SimpleStringProperty();
  private final IntegerProperty speedMin        = new SimpleIntegerProperty();
  private final IntegerProperty speedMax        = new SimpleIntegerProperty();
  private final BooleanProperty createButtonDisableProperty = new SimpleBooleanProperty();
  private final BooleanProperty editButtonDisableProperty = new SimpleBooleanProperty();
  private final BooleanProperty removeButtonDisableProperty = new SimpleBooleanProperty();
  private HorseDTO selectedHorse;
  private boolean creationMode;



  public CreateEditHorseVM(ModelManager model) {
    this.model      = model;
    this.horseList  = model.getHorseList();

    // bootstrap the list
    model.getAllHorses();
    creationMode = false;
  }

  // — property getters for binding —

  public ObservableList<HorseDTO> getHorseList()     { return horseList; }
  public StringProperty horseNameProp()             { return horseName; }
  public IntegerProperty speedMinProp()             { return speedMin; }
  public IntegerProperty speedMaxProp()             { return speedMax; }
  public BooleanProperty editButtonDisableProperty () {return createButtonDisableProperty;}
  public BooleanProperty removeButtonDisableProperty() {return removeButtonDisableProperty; }

  /**
   * Sets the selected horse and updates form fields and button states accordingly.
   *
   * @param newVal The newly selected horse, or null if no selection
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

  public void setNull() {
    this.selectedHorse = null;
    horseName.set(null);
    speedMin.set(0);
    speedMax.set(0);

    editButtonDisableProperty().set(true);
    removeButtonDisableProperty().set(true);
  }

  public void addHorse() {
    if (!creationMode) return;

    model.createHorse(horseName.get(),speedMin.get(),speedMax.get());

    setReadMode();
  }


  public void setReadMode() {
    creationMode = false;
  }

  /**
   * Updates the currently selected horse with the form values.
   * Only executes if a horse is selected.
   */
  public void updateHorse() {
    if (selectedHorse != null) {
      model.updateHorse(selectedHorse.id(),horseName.get(),speedMin.get(),speedMax.get());
    }
  }
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
    if(creationMode) addHorse();

    setNull();
    creationMode = true;
  }


}
