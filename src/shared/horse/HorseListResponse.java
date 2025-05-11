package shared.horse;

import shared.DTO.HorseDTO;

import java.util.List;

public record HorseListResponse(List<HorseDTO> horseList) {
}
