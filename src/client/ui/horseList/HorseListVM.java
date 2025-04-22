package client.ui.horseList;

import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;

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
    List<Horse> horseList = racersClient.getHorseList();
    Platform.runLater(()->horses.setAll(horseList));
  }

  @Override public void update(String message)
  {

  }

  public ObservableList<Horse> getHorses()
  {
    return horses;
  }
}
