package server.persistence.raceRepository;

import server.model.Racer;

import java.sql.SQLException;
import java.util.List;

public interface RaceRepository {
    Racer create () throws SQLException;
    Racer readByID ( ) throws SQLException;
    Racer readBySpeed_min () throws SQLException;
    Racer readBySpeed_max ( ) throws SQLException;
    List<Racer> readByName ( ) throws SQLException;
    List<Racer> readAll () throws SQLException;
    void updateRacer() throws SQLException;
    void delete (  ) throws SQLException;
}
