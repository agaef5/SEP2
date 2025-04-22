package client.ui.horseList;

import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;

public class HorseListVM implements MessageListener
{
  private RacersClient racersClient;
  private ObservableList<Horse> horses = FXCollections.observableArrayList();

  public HorseListVM(RacersClient racersClient){
    this.racersClient = racersClient;
    //load horses from database
  }

  //create method to load horses from database

  @Override public void update(String message)
  {

  }
}
