package shared;

import server.model.Horse;
import shared.DTO.HorseDTO;

import java.util.List;

public record HorseListResponse(List<HorseDTO> horseList) {
}
