package server.persistence.user;

import server.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository
{
  void add(User user);
  User getSingle(String string);
  void delete(User user);
  void save(User user);
  ArrayList<User> getMany(int pageIndex, int pageSize, String string);

}