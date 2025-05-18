package shared.updates;

import java.util.List;

public record HorsePositionsUpdate(String raceName, List<Integer> positions) {
}
