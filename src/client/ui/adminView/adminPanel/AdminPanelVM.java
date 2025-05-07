package client.ui.adminView.adminPanel;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Race;
import shared.GetRaceListResponse;

import java.util.List;

public class AdminPanelVM implements MessageListener
{
  private RaceClient raceClient;
  private SocketService socketService;
  private Gson gson;

  private StringProperty nextRaceInfo = new SimpleStringProperty("No planned races");

  public AdminPanelVM(RaceClient raceClient, SocketService socketService)
  {
    this.raceClient = raceClient;
    this.socketService = socketService;
    this.gson = new Gson();

    //register as listener
    this.socketService.addListener(this);

    refreshRaceList();
  }

  private void refreshRaceList()
  {
    raceClient.getRaceList();
  }

  public StringProperty getNextRaceInfo()
  {
    return nextRaceInfo;
  }

  @Override public void update(String type, String payload)
  {
    if ("getRaceList".equals(type))
    {
      GetRaceListResponse response = gson.fromJson(payload, GetRaceListResponse.class);
      updateRaceInfo(response.races());
    }
  }

  private void updateRaceInfo(List<Race> races)
  {
    Platform.runLater(() -> {
      if (races != null && !races.isEmpty()) {
        // Get the first race in the queue (assume it's sorted by time)
        Race nextRace = races.get(0);

        // Format the race information
        String info = String.format("Next race: %s - Track: %s - Horses: %d",
            nextRace.getName(),
            nextRace.getRaceTrack().getName(),
            nextRace.getHorseList().getList().size());

        nextRaceInfo.set(info);
      } else {
        nextRaceInfo.set("No upcoming races");
      }
    });
  }
}

