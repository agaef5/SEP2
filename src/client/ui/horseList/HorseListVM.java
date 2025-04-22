package client.ui.horseList;

import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import shared.HorseListResponse;
import shared.Respond;

import java.util.List;

public class HorseListVM implements MessageListener
{
  private RacersClient racersClient;
  private ObservableList<Horse> horses = FXCollections.observableArrayList();

  public HorseListVM(RacersClient racersClient){
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
    if (message instanceof Respond respond && "horseList".equals(respond.type())) {
      Object payload = respond.payload();
      if (payload instanceof HorseListResponse horseListResponse) {
        Platform.runLater(() -> horses.setAll(horseListResponse.horseList()));
      }
    }
  }

}
