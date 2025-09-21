package hiber.service;

import hiber.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void addUser(User user);
    List<User> listUsers();
    User findUserByCarModelAndSeries(String model, int series);
   // Optional<User> findByEmail(String email);
}
