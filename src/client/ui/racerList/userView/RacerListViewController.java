//package client.ui.racerList.userView;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import server.model.Horse;
//
//
//public class RacerListViewController
//{
//  @FXML TableView<Horse> tableView;
//  @FXML TableColumn <Horse, Integer> horseId;
//  @FXML TableColumn <Horse, String> horseName;
//  @FXML TableColumn <Horse, Integer> speedMin;
//  @FXML TableColumn <Horse, Integer> speedMax;
//
//  private RacerListVM racerListVM;
//
//  public RacerListViewController(RacerListVM racerListVM){
//    this.racerListVM = racerListVM;
//  }
//
//  public void initialize(){
//    horseId.setCellValueFactory(new PropertyValueFactory<>("id"));
//    horseName.setCellValueFactory(new PropertyValueFactory<>("name"));
//    speedMin.setCellValueFactory(new PropertyValueFactory<>("speedMin"));
//    speedMax.setCellValueFactory(new PropertyValueFactory<>("speedMax"));
//
//    tableView.setItems(racerListVM.getHorses());
//  }
//
//}
