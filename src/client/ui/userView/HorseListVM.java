//package client.ui.racerList.userView;
//
//import client.networking.horses.HorsesClient;
//import client.ui.MessageListener;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import server.model.Horse;
//import shared.HorseListResponse;
//import shared.Respond;
//
//import java.util.ArrayList;
//
//public class HorseListVM implements MessageListener
//{
//  private HorsesClient horsesClient;
//  private ObservableList<Horse> horses = FXCollections.observableArrayList();
//
//  public HorseListVM(HorsesClient horsesClient)
//  {
//    this.horsesClient = horsesClient;
//    loadHorsesFromDatabase();
//  }
//
//  private void loadHorsesFromDatabase()
//  {
//   horsesClient.getHorseList();
//  }
//
//  public ObservableList<Horse> getHorses()
//  {
//    return horses;
//  }
//
//
//  @Override public void update(Object message) {
//    if (message instanceof Respond respond && "horse".equals(respond.type())) {
//      Object payload = respond.payload();
//      if (payload instanceof HorseListResponse racerListResponse) {
//        Platform.runLater(() -> {
//          ArrayList<Horse> horseList = new ArrayList<>();
//          for (Horse horse : HorseListResponse.horseList()) {
//            if (horse instanceof Horse) {
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
