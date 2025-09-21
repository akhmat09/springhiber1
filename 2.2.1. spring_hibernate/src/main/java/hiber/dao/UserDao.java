package hiber.dao;

import hiber.model.User;

import java.util.List;

public interface UserDao {
   User getUserByCarModelAndSeries(String model, int series);
   void addUser(User user);
   List<User> listUsers();
}
