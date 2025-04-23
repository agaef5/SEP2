package client.ui.racerList.adminView;

import client.ui.racerList.adminView.CreateEditRacerVM;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;
import server.model.Racer;

public class CreateEditRacerController {

  @FXML private ListView<Racer> listView;
  @FXML private TextField racerType;
  @FXML private TextField racerName;
  @FXML private TextField speedMin;
  @FXML private TextField speedMax;
//  @FXML private TextField misc;
  @FXML private Button create;
  @FXML private Button edit;
  @FXML private Button remove;

  private CreateEditRacerVM viewModel;

  public void init(CreateEditRacerVM viewModel) {
    this.viewModel = viewModel;

    // ListView binding
    listView.setItems(viewModel.getRacerList());
    listView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedRacer(newVal)
    );

    // Binding inputfields to ViewModel-properties
    racerType.textProperty().bindBidirectional(viewModel.racerTypeProperty());
    racerName.textProperty().bindBidirectional(viewModel.racerNameProperty());

    // Integer binding with NumberStringConverter
    Bindings.bindBidirectional(speedMin.textProperty(), viewModel.speedMinProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(speedMax.textProperty(), viewModel.speedMaxProperty(), new NumberStringConverter());

        // chain actions
    create.setOnAction(e -> viewModel.addRacer());
    edit.setOnAction(e -> viewModel.updateRacer());
    remove.setOnAction(e -> viewModel.removeRacer());
  }
}
