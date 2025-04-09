package server.persistence.user;

import server.model.user.User;
import java.util.List;

public interface UserRepository
{
    void add(User user);
    User getSingle(String email);
    void delete(String email);
    void save(User user);
    List<User> getMany(int pageIndex, int pageSize, String string);
}
