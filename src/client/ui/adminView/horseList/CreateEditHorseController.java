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
  @FXML private TextField            horseName;
  @FXML private TextField            speedMin;
  @FXML private TextField            speedMax;

  @FXML private Button               newBtn;
  @FXML private Button               create;
  @FXML private Button               edit;
  @FXML private Button               remove;
  @FXML private Label                messageLabel;

  private CreateEditHorseVM viewModel;
  private MainWindowController window;

  @Override
  public void initialize(ViewModel viewModel) {
    this.viewModel = (CreateEditHorseVM)viewModel;

    // — bind the list —
    listView.setItems(this.viewModel.getHorseList());
    listView.getSelectionModel()
            .selectedItemProperty();

    // — bind the form fields —
    horseName.textProperty()
            .bindBidirectional(this.viewModel.horseNameProp());
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

    // — bind button enable/disable —
//    newBtn.disableProperty()
//            .bind(this.viewModel.creationModeProp());
    create.disableProperty()
            .bind(this.viewModel.canCreate().not());
    edit.disableProperty()
            .bind(this.viewModel.canUpdate().not());
    remove.disableProperty()
            .bind(this.viewModel.canDelete().not());

    // — actions —
//    newBtn.setOnAction(e -> this.viewModel.enterCreateMode());
    create.setOnAction(e -> this.viewModel.createHorse());
    edit.setOnAction(e -> this.viewModel.updateHorse());
    remove.setOnAction(e -> this.viewModel.deleteHorse());

    // — status message —
//    messageLabel.textProperty()
//            .bind(this.viewModel.messageProp());
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.window = mainWindowController;
  }
}
