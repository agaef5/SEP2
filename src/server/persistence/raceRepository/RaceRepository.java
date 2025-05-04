package server.persistence.raceRepository;

import server.model.Race;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface RaceRepository {
    Race create (String name, Date time, String status ) throws SQLException;
    Race readByID (int id ) throws SQLException;
    List<Race> readByName (String name) throws SQLException;
    Race readByTime (Date time ) throws SQLException;
    Race readByStatus (String status) throws SQLException;
    void updateRacer(Race race) throws SQLException;
    void delete (Race race) throws SQLException;
}
