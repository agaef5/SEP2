package server.persistence.horses;

import server.model.Horse;

import java.sql.SQLException;
import java.util.List;

public interface HorseRepository
{
  Horse create ( String name, int speedMin, int speedMax) throws SQLException;
  Horse readByID ( int id ) throws SQLException;
  Horse readBySpeed_min ( int speedMin ) throws SQLException;
  Horse readBySpeed_max ( int speedMax ) throws SQLException;
  List<Horse> readByName ( String searchName ) throws SQLException;
  List<Horse> readAll () throws SQLException;
  void updateHorse(Horse horse) throws SQLException;
  void delete (Horse horse) throws SQLException;
}
