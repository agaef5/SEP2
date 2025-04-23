package shared;

import server.model.Horse;
import server.model.Racer;

import java.util.List;

public record RacerListResponse(List<Horse> horseList) {
  public Racer racers()
  {
    return racers();
  }
}
