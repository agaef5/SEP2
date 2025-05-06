package client.ui.userView;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;
import server.model.Horse;

public class HorseListViewController
{
  @FXML private ListView<Horse> listView;
  @FXML private TextField horseName;
  @FXML private TextField speedMin;
  @FXML private TextField speedMax;

  private HorseListVM viewModel;

  public void initialize(HorseListVM viewModel)
  {
    this.viewModel = viewModel;

    // ListView binding
    listView.setItems(viewModel.getHorseList());

    listView.setCellFactory(param -> new ListCell<>()
    {
      @Override protected void updateItem(Horse horse, boolean empty)
      {
        super.updateItem(horse, empty);
        if (empty || horse == null)
        {
          setText(null);
        }
        else
        {
          setText(horse.getName() + " (speed: " + horse.getSpeedMin() + " - "
              + horse.getSpeedMax() + ")");
        }
      }
    });

    listView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedHorse(newVal));

    Bindings.bindBidirectional(horseName.textProperty(),
        viewModel.horseNameProperty(), new DefaultStringConverter());

    // Integer binding with NumberStringConverter
    Bindings.bindBidirectional(speedMin.textProperty(),
        viewModel.speedMinProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(speedMax.textProperty(),
        viewModel.speedMaxProperty(), new NumberStringConverter());

  }
}
