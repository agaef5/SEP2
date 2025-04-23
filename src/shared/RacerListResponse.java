package shared;

import server.model.Horse;
import java.util.List;

public record RacerListResponse(List<Horse> horseList) {
}
