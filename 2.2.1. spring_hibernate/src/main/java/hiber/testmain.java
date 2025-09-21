package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class testmain {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(AppConfig.class);


            UserService userService = context.getBean(UserService.class);


            createTestData(userService);


            demonstrateApplication(userService);

        } catch (Exception e) {
            System.err.println("Произошла ошибка в работе приложения:");
            e.printStackTrace();
        } finally {

            if (context != null) {
                context.close();
            }
        }
    }

    private static void createTestData(UserService userService) {

        Car car1 = new Car("Toyota Camry", 50);
        Car car2 = new Car("BMW X5", 30);
        Car car3 = new Car("Audi A4", 45);


        User user1 = new User("Иван", "Иванов", "ivan@example.com");
        User user2 = new User("Петр", "Петров", "petr@example.com");
        User user3 = new User("Сергей", "Сергеев", "sergey@example.com");

        user1.setCar(car1);
        car1.setUser(user1);

        user2.setCar(car2);
        car2.setUser(user2);


        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
    }

    private static void demonstrateApplication(UserService userService) {
        System.out.println("=== Все пользователи ===");
        userService.listUsers().forEach(user -> {
            String carInfo = (user.getCar() != null)
                    ? String.format("%s %d", user.getCar().getModel(), user.getCar().getSeries())
                    : "нет автомобиля";

            System.out.printf("%s %s (%s) - %s%n",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    carInfo);
        });



        System.out.println("\n=== Поиск по автомобилю ===");
        try {
            User foundByCar = userService.findUserByCarModelAndSeries("BMW X5", 30);
            System.out.println("Владелец автомобиля: " +
                    foundByCar.getFirstName() + " " + foundByCar.getLastName());
        } catch (Exception e) {
            System.out.println("Автомобиль или владелец не найдены");
        }

        System.out.println("\n=== Попытка найти несуществующий автомобиль ===");
        try {
            User notFound = userService.findUserByCarModelAndSeries("Ferrari", 100);
            System.out.println(notFound != null ? "Найден" : "Не найден");
        } catch (Exception e) {
            System.out.println("Ошибка при поиске: " + e.getMessage());
        }
    }
}
