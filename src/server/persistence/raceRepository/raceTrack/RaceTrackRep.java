package server.persistence.raceRepository.raceTrack;

import server.model.RaceTrack;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface RaceTrackRep {
    RaceTrack create (String name, int length, String location) throws SQLException;
    List<RaceTrack> readByName (String name) throws SQLException;
    RaceTrack readByLength (int length ) throws SQLException;
    RaceTrack readByLocation (String location) throws SQLException;
    void updateRacer(RaceTrack raceTrack) throws SQLException;
    void delete (RaceTrack raceTrack) throws SQLException;
}
