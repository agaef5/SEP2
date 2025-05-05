package client.ui.adminView.horseList;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;
import server.model.Horse;

public class CreateEditHorseController
{

  @FXML private ListView<Horse> listView;
  @FXML private TextField speedMin;
  @FXML private TextField speedMax;
  @FXML private Button create;
  @FXML private Button edit;
  @FXML private Button remove;

  private CreateEditHorseVM viewModel;

  public void init(CreateEditHorseVM viewModel) {
    this.viewModel = viewModel;

    // ListView binding
    listView.setItems(viewModel.getHorseList());
    listView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedHorse(newVal)
    );

    // Integer binding with NumberStringConverter
    Bindings.bindBidirectional(speedMin.textProperty(), viewModel.speedMinProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(speedMax.textProperty(), viewModel.speedMaxProperty(), new NumberStringConverter());

        // chain actions
    create.setOnAction(e -> viewModel.addHorse());
    edit.setOnAction(e -> viewModel.updateHorse());
    remove.setOnAction(e -> viewModel.removeHorse());
  }
}
