package client.ui.userView;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;
import server.model.Horse;

/**
 * Controller for the Horse List view in the user interface.
 * Manages the display of horse information for regular users.
 * Unlike the admin view, this view is read-only and doesn't allow modifications.
 */
public class HorseListViewController
{
  /** ListView displaying all available horses */
  @FXML private ListView<Horse> listView;

  /** Text field displaying the selected horse's name */
  @FXML private TextField horseName;

  /** Text field displaying the selected horse's minimum speed */
  @FXML private TextField speedMin;

  /** Text field displaying the selected horse's maximum speed */
  @FXML private TextField speedMax;

  /** ViewModel that provides data for this view */
  private HorseListVM viewModel;

  /**
   * Initializes the controller with the provided ViewModel.
   * Sets up bindings between UI components and ViewModel properties
   * and configures the cell factory for the horse list.
   *
   * @param viewModel The ViewModel that provides data for this view
   */
  public void initialize(HorseListVM viewModel)
  {
    this.viewModel = viewModel;

    // Bind the ListView to the horse list in the ViewModel
    listView.setItems(viewModel.getHorseList());

    // Configure cell rendering for the horse list
    listView.setCellFactory(param -> new ListCell<>()
    {
      @Override
      protected void updateItem(Horse horse, boolean empty)
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

    // Update the selected horse in the ViewModel when selection changes in the ListView
    listView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedHorse(newVal));

    // Bind text fields to ViewModel properties with appropriate converters
    Bindings.bindBidirectional(horseName.textProperty(),
        viewModel.horseNameProperty(), new DefaultStringConverter());

    // Integer binding with NumberStringConverter for speed values
    Bindings.bindBidirectional(speedMin.textProperty(),
        viewModel.speedMinProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(speedMax.textProperty(),
        viewModel.speedMaxProperty(), new NumberStringConverter());
  }
}