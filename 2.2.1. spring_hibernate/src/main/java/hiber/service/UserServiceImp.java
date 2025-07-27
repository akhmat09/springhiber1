package hiber.service;

import hiber.dao.UserDao;
import hiber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

   @Autowired
   private UserDao userDao;

   @Transactional
   @Override
   public void add(User user) {
      userDao.add(user);
   }

   @Transactional(readOnly = true)
   @Override
   public List<User> listUsers() {
      return userDao.listUsers();
   }

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public User findUserByCarModelAndSeries(String model, int series) {
      String hql = "SELECT u FROM User u WHERE u.car.model = :model AND u.car.series = :series";

      TypedQuery<User> query = entityManager.createQuery(hql, User.class)
              .setParameter("model", model)
              .setParameter("series", series);

      try {
         return query.getSingleResult();
      } catch (NoResultException e) {
         return null;
      }
      @Override
      public Optional<User> findByEmail(String email) {
         String hql = "SELECT u FROM User u WHERE u.email = :email";

         TypedQuery<User> query = entityManager.createQuery(hql, User.class)
                 .setParameter("email", email);

         try {
            return Optional.of(query.getSingleResult());
         } catch (NoResultException e) {
            return Optional.empty();
         }
      }
   }

}
