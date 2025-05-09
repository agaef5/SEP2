package client.ui.adminView.base;

import client.ui.adminView.AdminViewController;

public interface BaseAdminController {
    void initialize(BaseViewModel viewModel);
    void setTabbedWindowController(AdminViewController adminViewController);
}
