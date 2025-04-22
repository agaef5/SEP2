package server.persistence.racer;

import server.model.Horse;
import server.model.Racer;

import java.sql.SQLException;
import java.util.List;

public interface RacerRepository
{
  Horse create ( String name ) throws SQLException;
  Horse readByID ( int id ) throws SQLException;
  Horse readBySpeed_min ( float speedMin ) throws SQLException;
  Horse readBySpeed_max ( float speedMax ) throws SQLException;
  List<Horse> readByName ( String searchName ) throws SQLException;
  List<Horse> readAll() throws SQLException;
  void updateName ( Horse horse ) throws SQLException;
  void updateSpeedMin ( Horse horse ) throws SQLException;
  void updateSpeedMax ( Horse horse ) throws SQLException;
  void delete ( Horse horse ) throws SQLException;
}
