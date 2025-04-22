package server.model;

import java.sql.SQLException;
import java.util.List;

public interface HorseDAO
{
  Horse create ( String name ) throws SQLException;
  Horse readByID ( int id ) throws SQLException;
  Horse readBySpeed_min ( float speedMin ) throws SQLException;
  Horse readBySpeed_max ( float speedMax ) throws SQLException;
  List<Horse> readByName ( String searchName ) throws SQLException;
  void updateName ( Horse horse ) throws SQLException;
  void updateSpeedMin ( Horse horse ) throws SQLException;
  void updateSpeedMax ( Horse horse ) throws SQLException;
  void delete ( Horse horse ) throws SQLException;
}
