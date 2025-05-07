package client.ui.common;

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
  /**
   * Changes the current page to the specified new page.
   * <p>
   * The method is expected to perform the necessary operations to switch from the current page to the
   * new page. This might include hiding the current page, displaying the new page, and updating any
   * related UI components.
   *
   * @param page the {@code Controller} instance representing the new page to display
   */
  void changePage(Controller page);
}
