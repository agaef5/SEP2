package client.ui.racerList.userView;

import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import shared.RacerListResponse;
import shared.Respond;

public class RacerListVM implements MessageListener
{
  private RacersClient racersClient;
  private ObservableList<Horse> horses = FXCollections.observableArrayList();

  public RacerListVM(RacersClient racersClient){
    this.racersClient = racersClient;
    loadHorsesFromDatabase();
  }

  private void loadHorsesFromDatabase(){
   racersClient.getRacerList();
     }

  public ObservableList<Horse> getHorses()
  {
    return horses;
  }


  @Override
  public void update(Object message) {
    if (message instanceof Respond respond && "racer".equals(respond.type())) {
      Object payload = respond.payload();
      if (payload instanceof RacerListResponse racerListResponse) {
        Platform.runLater(() -> horses.setAll(racerListResponse.horseList()));
      }
    }
  }

}
