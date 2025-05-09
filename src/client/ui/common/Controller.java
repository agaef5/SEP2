package client.ui.common;

import client.ui.navigation.MainWindowController;
import javafx.event.ActionEvent;

/**
 * The {@code Controller} interface defines the contract for UI controllers that manage navigation
 * between different pages in an application. Implementing this interface allows controllers to change
 * pages or views within the UI by invoking the {@code changePage} method.
 * <p>
 * This is typically used in applications with multiple views where the controller is responsible
 * for managing transitions between them.
 */
public interface Controller
{

  void initialize(ViewModel viewModel);

  void setWindowController(MainWindowController mainWindowController);
}
