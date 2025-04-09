package server.persistence.user;

import server.model.user.Admin;
import server.model.user.Player;
import server.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepositoryImpl implements UserRepository
{
  private final static ArrayList<User> users = new ArrayList<>(Arrays.asList(
      new Player("user1","trmo@via.dk", "1234"),
      new Player("user2","jaja@gmail.com", "1234"),
      new Player("user3","pepe@gmail.com", "1234"),
      new Player("user4","jeje@gmail.com", "1234"),
      new Player("user5","momo@gmail.com", "1234"),
      new Player("user6","anan@gmail.com", "1234"),
      new Admin("admin", "admin@gamil.com", "1234")
  ));

  @Override public void add(User user)
  {

  }

  @Override public User getSingle(String email)
  {
    User user = new Player("username", "email", "password");
    return user;
  }

  @Override public void delete(String email)
  {

  }

  @Override public void save(User user)
  {

  }

  @Override public ArrayList<User> getMany(int pageIndex, int pageSize,
      String string)
  {
        ArrayList<User> result = new ArrayList<>();
        for (int i = 0; pageIndex * pageSize + i < users.size(); i++)
        {
            // this is an attempt at implementing paging, as seen in dbs, maybe it works, maybe it doesn't
            User user = users.get(i);

            if (!string.isEmpty() && (user.getUsername().contains(string) || user.getEmail().contains(string)))  // if argument firstNameContains is not empty string, we filter by this.
            {
                result.add(user);
            }
        }
        return result;
  }
}

