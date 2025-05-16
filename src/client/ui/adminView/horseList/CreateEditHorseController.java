// client/ui/adminView/horseList/HorseListController.java
package client.ui.adminView.horseList;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;
import shared.DTO.HorseDTO;

public class CreateEditHorseController implements Controller {
  @FXML private ListView<HorseDTO>   listView;
  @FXML private TextField            nameField;
  @FXML private TextField            minField;
  @FXML private TextField            maxField;
  @FXML private Button               newBtn;
  @FXML private Button               createBtn;
  @FXML private Button               updateBtn;
  @FXML private Button               deleteBtn;
  @FXML private Label                messageLabel;

  private CreateEditHorseVM vm;
  private MainWindowController window;

  @Override
  public void initialize(ViewModel viewModel) {
    vm = (CreateEditHorseVM)viewModel;

    // — bind the list —
    listView.setItems(vm.getHorseList());
    listView.getSelectionModel()
            .selectedItemProperty()
            ;

    // — bind the form fields —
    nameField.textProperty()
            .bindBidirectional(vm.horseNameProp());
    Bindings.bindBidirectional(
            minField.textProperty(),
            vm.speedMinProp(),
            new NumberStringConverter()
    );
    Bindings.bindBidirectional(
            maxField.textProperty(),
            vm.speedMaxProp(),
            new NumberStringConverter()
    );

    // — bind button enable/disable —
    newBtn.disableProperty()
            .bind(vm.creationModeProp());
    createBtn.disableProperty()
            .bind(vm.canCreate().not());
    updateBtn.disableProperty()
            .bind(vm.canUpdate().not());
    deleteBtn.disableProperty()
            .bind(vm.canDelete().not());

    // — actions —
    newBtn.setOnAction(e -> vm.enterCreateMode());
    createBtn.setOnAction(e -> vm.createHorse());
    updateBtn.setOnAction(e -> vm.updateHorse());
    deleteBtn.setOnAction(e -> vm.deleteHorse());

    // — status message —
    messageLabel.textProperty()
            .bind(vm.messageProp());
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.window = mainWindowController;
  }
}
