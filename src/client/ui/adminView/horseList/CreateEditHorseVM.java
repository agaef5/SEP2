// client/ui/adminView/horseList/HorseListViewModel.java
package client.ui.adminView.horseList;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import shared.DTO.HorseDTO;

public class CreateEditHorseVM implements ViewModel {
  private final ModelManager model;

  // –– backing lists & props ––
  private final ObservableList<HorseDTO> horseList;
  private final ObjectProperty<HorseDTO> selectedHorse = new SimpleObjectProperty<>();
  private final StringProperty horseName        = new SimpleStringProperty();
  private final IntegerProperty speedMin        = new SimpleIntegerProperty();
  private final IntegerProperty speedMax        = new SimpleIntegerProperty();
  private final BooleanProperty creationMode    = new SimpleBooleanProperty(false);
  private final StringProperty message         = new SimpleStringProperty();

  public CreateEditHorseVM(ModelManager model) {
    this.model      = model;
    this.horseList  = model.getHorseList();

    // when the global list changes, clear message
    model.getHorseList().addListener((ListChangeListener<HorseDTO>) change -> {
      message.set("");
    });

    // keep our form fields in sync with selectedHorse
    selectedHorse.addListener((o, oldH, newH) -> {
      if (newH == null) {
        horseName.set("");
        speedMin.set(0);
        speedMax.set(0);
      } else {
        horseName.set(newH.name());
        speedMin.set(newH.speedMin());
        speedMax.set(newH.speedMax());
      }
      creationMode.set(false);
      message.set("");
    });

    // wire server-side success/failure back into our message text
    model.createHorseSuccessProperty().addListener((o, old, ok) -> {
      if (ok) {
        model.getAllHorses();
      } else {
        message.set(model.createHorseMessageProperty().get());
      }
    });
    model.updateHorseSuccessProperty().addListener((o, old, ok) -> {
      if (ok) {
        model.getAllHorses();
      } else {
        message.set(model.updateHorseMessageProperty().get());
      }
    });
    model.deleteHorseSuccessProperty().addListener((o, old, ok) -> {
      if (ok) {
        model.getAllHorses();
      } else {
        message.set(model.deleteHorseMessageProperty().get());
      }
    });

    // bootstrap the list
    model.getAllHorses();
  }

  // — property getters for binding —

  public ObservableList<HorseDTO> getHorseList()     { return horseList; }
  public ObjectProperty<HorseDTO> selectedHorseProp(){ return selectedHorse; }
  public StringProperty horseNameProp()             { return horseName; }
  public IntegerProperty speedMinProp()             { return speedMin; }
  public IntegerProperty speedMaxProp()             { return speedMax; }
  public BooleanProperty creationModeProp()         { return creationMode; }
  public StringProperty messageProp()               { return message; }

  public BooleanExpression canCreate() {
    return creationMode;
  }

  public BooleanBinding canUpdate()  { return selectedHorse.isNotNull(); }
  public BooleanBinding canDelete()  { return selectedHorse.isNotNull(); }

  // — commands invoked by the Controller —

  /** Switch UI into “new horse” mode */
  public void enterCreateMode() {
    selectedHorse.set(null);
    creationMode.set(true);
    message.set("");
  }

  /** Create a brand‐new horse */
  public void createHorse() {
    if (!creationMode.get()) return;
    model.createHorse(horseName.get(), speedMin.get(), speedMax.get());
  }

  /** Update the selected horse */
  public void updateHorse() {
    HorseDTO h = selectedHorse.get();
    if (h == null) return;
    HorseDTO updated = new HorseDTO(
            h.id(),
            horseName.get(),
            speedMin.get(),
            speedMax.get()
    );
    model.updateHorse(updated);
  }

  /** Delete the selected horse */
  public void deleteHorse() {
    HorseDTO h = selectedHorse.get();
    if (h != null) model.deleteHorse(h);
  }
}
