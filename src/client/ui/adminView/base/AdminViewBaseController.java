package client.ui.adminView.base;

import client.ui.adminView.AdminViewController;
import client.ui.common.ViewModel;

public interface AdminViewBaseController {
    void initialize(ViewModel viewModel);
    void setAdminViewController(AdminViewController adminViewController);
}
