//package client.ui.userView.landingPage;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.image.ImageView;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class UserLandingPageController {
//    @FXML private Label raceLabel;
//    @FXML private Button mainButton;
//    @FXML private ImageView imageView;
//
//    private UserLandingPageVM viewModel;
//
//    public UserLandingPageController(){}
//
//    public void initialize(UserLandingPageVM viewModel)
//    {
//        this.viewModel = viewModel;
//
//        // Bind the race info label to the ViewModel property
//        raceLabel.textProperty().bind(viewModel.getNextRaceInfo());
//
//        // Set up navigation actions for buttons
//        mainButton.setOnAction(e -> loadScene(
//                "/client/ui/userView/bettingPage/HorseListView.fxml"));
//    }
//
//    private void loadScene(String fxmlPath)
//    {
//        try
//        {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//            Parent root = loader.load();
//            Stage stage = (Stage) mainButton.getScene().getWindow();
//            stage.setScene(new Scene(root));
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
