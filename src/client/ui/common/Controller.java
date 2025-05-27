package client.ui.common;

import client.ui.navigation.MainWindowController;
import javafx.event.ActionEvent;

/**
 * Interface for UI controllers that support initialization and view navigation.
 *
 * Controllers implementing this interface can be initialized with a ViewModel
 * and connected to the main window controller to enable view switching.
 */
public interface Controller {

  /**
   * Initializes the controller with the provided ViewModel.
   *
   * @param viewModel the ViewModel used to bind data and handle logic
   */
  void initialize(ViewModel viewModel);

  /**
   * Sets the reference to the main window controller.
   * Used to control view changes within the application.
   *
   * @param mainWindowController the main window controller instance
   */
  void setWindowController(MainWindowController mainWindowController);
}
