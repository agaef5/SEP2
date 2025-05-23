// client/ui/adminView/horseList/HorseListController.java
package client.ui.adminView.horseList;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;
import shared.DTO.HorseDTO;

public class CreateEditHorseController implements Controller {
  @FXML private ListView<HorseDTO>   listView;
  @FXML private TextField            horseName;
  @FXML private TextField            speedMin;
  @FXML private TextField            speedMax;

  @FXML private Button               create;
  @FXML private Button               edit;
  @FXML private Button               remove;

  private CreateEditHorseVM viewModel;
  private MainWindowController mainWindowController;

  public CreateEditHorseController(){};
  

  @Override
  public void initialize(ViewModel viewModel) {
    this.viewModel = (CreateEditHorseVM)viewModel;

    // — bind the list —
    listView.setItems(this.viewModel.getHorseList());


    // — bind the form fields —
    Bindings.bindBidirectional(horseName.textProperty(),  this.viewModel.horseNameProp(), new DefaultStringConverter());
    Bindings.bindBidirectional(
            speedMin.textProperty(),
            this.viewModel.speedMinProp(),
            new NumberStringConverter()
    );
    Bindings.bindBidirectional(
            speedMax.textProperty(),
            this.viewModel.speedMaxProp(),
            new NumberStringConverter()
    );

    // Configure cell rendering for the horse list
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(HorseDTO horse, boolean empty) {
        super.updateItem(horse, empty);
        if (empty || horse == null) {
          setText(null);
        } else {
          setText(horse.name() + " (speed: " + horse.speedMin() + " - " + horse.speedMax() + ")");
        }
      }
    });


    // Update the selected horse in the ViewModel when selection changes in the ListView
    listView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> this.viewModel.setSelectedHorse(newVal)
    );

    // — bind button enable/disable —

    // Bind button disabled states to ViewModel properties
    Bindings.bindBidirectional(edit.disableProperty(), ((CreateEditHorseVM) viewModel).editButtonDisableProperty());
    Bindings.bindBidirectional(remove.disableProperty(), ((CreateEditHorseVM) viewModel).removeButtonDisableProperty());

//    create.disableProperty()
//            .bind(this.viewModel.createButtonDisableProperty());
//    edit.disableProperty()
//            .bind(this.viewModel.editButtonDisableProperty());
//    remove.disableProperty()
//            .bind(this.viewModel.removeButtonDisableProperty());

    // — actions —
//    newBtn.setOnAction(e -> this.viewModel.enterCreateMode());
    create.setOnAction(e -> this.viewModel.setHorseCreationMode());
    edit.setOnAction(e -> this.viewModel.updateHorse());
    remove.setOnAction(e -> this.viewModel.removeHorse());

    // — status message —
//    messageLabel.textProperty()
//            .bind(this.viewModel.messageProp());
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }
}
