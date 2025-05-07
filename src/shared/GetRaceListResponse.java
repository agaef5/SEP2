package shared;

import server.model.Race;

import java.util.ArrayList;
import java.util.List;

public record GetRaceListResponse(List<Race> races)
{
}
