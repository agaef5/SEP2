package server.persistence.racer;

import server.model.Horse;
import server.model.Racer;

import java.sql.SQLException;
import java.util.List;

public interface RacerRepository
{
  Racer create (String racerType, String name, int speedMin, int speedMax) throws SQLException;
  Racer readByID (String racerType, int id ) throws SQLException;
  Racer readBySpeed_min (String racerType, int speedMin ) throws SQLException;
  Racer readBySpeed_max (String racerType, int speedMax ) throws SQLException;
  List<Racer> readByName (String racerType, String searchName ) throws SQLException;
  List<Racer> readAll (String racerType) throws SQLException;
  void updateRacer(Racer racer) throws SQLException;
  void delete ( Racer racer ) throws SQLException;
}
