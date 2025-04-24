//package client.ui.racerList.userView;
//
//import client.networking.racers.RacersClient;
//import client.ui.MessageListener;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import server.model.Horse;
//import server.model.Racer;
//import shared.RacerListResponse;
//import shared.Respond;
//
//import java.util.ArrayList;
//
//public class RacerListVM implements MessageListener
//{
//  private RacersClient racersClient;
//  private ObservableList<Horse> horses = FXCollections.observableArrayList();
//
//  public RacerListVM(RacersClient racersClient){
//    this.racersClient = racersClient;
//    loadHorsesFromDatabase();
//  }
//
//  private void loadHorsesFromDatabase(){
//   racersClient.getRacerList();
//     }
//
//  public ObservableList<Horse> getHorses()
//  {
//    return horses;
//  }
//
//
//  @Override
//  public void update(Object message) {
//    if (message instanceof Respond respond && "racer".equals(respond.type())) {
//      Object payload = respond.payload();
//      if (payload instanceof RacerListResponse racerListResponse) {
//        Platform.runLater(() -> {
//          ArrayList<Horse> horseList = new ArrayList<>();
//          for (Racer racer : racerListResponse.racerList()) {
//            if (racer instanceof Horse horse) {
//              horseList.add(horse);
//            }
//          }
//          horses.setAll(horseList);
//        });
//      }
//    }
//  }
//
//}
