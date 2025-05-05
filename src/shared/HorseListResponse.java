package shared;

import server.model.Horse;

import java.util.List;

public record HorseListResponse(List<Horse> horseList) {
}
