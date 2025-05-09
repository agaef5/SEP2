package client.ui.adminView;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.networking.horses.HorsesClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.RaceClient;
import client.networking.race.SocketRaceClient;
import client.ui.adminView.adminPanel.AdminPanelController;
import client.ui.adminView.adminPanel.AdminPanelVM;
import client.ui.adminView.base.AdminViewBaseController;
import client.ui.common.ViewModel;
import client.ui.adminView.horseList.CreateEditHorseController;
import client.ui.adminView.horseList.CreateEditHorseVM;
import client.ui.adminView.race.CreateRaceController;
import client.ui.adminView.race.CreateRaceVM;
import client.ui.util.ErrorHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewController {
    @FXML
    private StackPane mainPane;
    private Stage stage;

    private SocketService socketService;
    private AuthenticationClient authenticationClient;
    private HorsesClient horsesClient;
    private RaceClient raceClient;

    public void initialize(SocketService socketService, AuthenticationClient authenticationClient, AdminPanelController adminPanelController) {
        this.socketService = socketService;
        this.authenticationClient = authenticationClient;
        horsesClient = new SocketHorsesClient(socketService);
        raceClient = new SocketRaceClient(socketService);

        if (adminPanelController != null) {
            adminPanelController.setAdminViewController(this);
        }
        try
        {
            loadAdminPanel();
        }catch (IOException e){
            ErrorHandler.handleError(e,  "Issue with loading the pages");
            System.out.println(e.getMessage());
        }

        mainPane.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            if (stage != null) {
                stage.sizeToScene();
            }
        });
    }

    private void loadPage(String fxmlFile) throws IOException {
        try {
            mainPane.getChildren().clear();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFile));
            Pane newContent = loader.load();

            ViewModel viewModel = null;
            Object controller = loader.getController();

            if (controller instanceof AdminPanelController) {
                viewModel = new AdminPanelVM(raceClient, socketService);
                ((AdminPanelController) controller).setAdminViewController(this);
            }

            if (controller instanceof CreateEditHorseController)
            {
                viewModel = new CreateEditHorseVM(horsesClient, socketService);
                ((CreateEditHorseController) controller).setAdminViewController(this);}

            if (controller instanceof CreateRaceController) {
                viewModel = new CreateRaceVM(raceClient, socketService);
                ((CreateRaceController) controller).setAdminViewController(this);
            }

            if(controller instanceof AdminViewBaseController){
                ((AdminViewBaseController)controller).initialize(viewModel);
            }

            mainPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAdminPanel() throws IOException{
            loadPage("client/ui/adminView/adminPanel/AdminPanel.fxml");
    }

    public void loadHorsePage() throws IOException{
        loadPage("client/ui/adminView/horseList/CreateEditHorse.fxml");
    }

    public void loadRacePage() throws IOException{
        loadPage("client/ui/adminView/race/CreateRace.fxml");
    }

}
