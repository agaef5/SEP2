package shared;

import server.model.Racer;

import java.util.List;

public record RacerListResponse(List<Racer> racerList) {
}
