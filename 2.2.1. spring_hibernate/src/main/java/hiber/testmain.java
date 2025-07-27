import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class MainApp {
    public static void main(String[] args) {
        // 1. Инициализация Spring контекста
        AnnotationConfigApplicationContext context = null;

        try {
            context = new AnnotationConfigApplicationContext(AppConfig.class);

            // 2. Получаем сервис из контекста
            UserService userService = context.getBean(UserService.class);

            // 3. Создаем и сохраняем тестовые данные
            createTestData(userService);

            // 4. Демонстрация работы приложения
            demonstrateApplication(userService);

        } catch (Exception e) {
            System.err.println("Произошла ошибка в работе приложения:");
            e.printStackTrace();
        } finally {
            // 5. Закрываем контекст
            if (context != null) {
                context.close();
            }
        }
    }

    private static void createTestData(UserService userService) {
        // Создаем автомобили
        Car car1 = new Car("Toyota Camry", 50);
        Car car2 = new Car("BMW X5", 30);
        Car car3 = new Car("Audi A4", 45);

        // Создаем пользователей и связываем с автомобилями
        User user1 = new User("Иван", "Иванов", "ivan@example.com");
        User user2 = new User("Петр", "Петров", "petr@example.com");
        User user3 = new User("Сергей", "Сергеев", "sergey@example.com");

        user1.setCar(car1);
        car1.setUser(user1);

        user2.setCar(car2);
        car2.setUser(user2);

        // user3 без автомобиля для демонстрации

        // Сохраняем пользователей (каскадно сохранятся и машины)
        userService.add(user1);
        userService.add(user2);
        userService.add(user3);
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

        System.out.println("\n=== Поиск по email ===");
        Optional<User> foundByEmail = userService.findByEmail("ivan@example.com");
        if (foundByEmail.isPresent()) {
            System.out.println("Найден: " + foundByEmail.get().getFirstName() + " " + foundByEmail.get().getLastName());
        } else {
            System.out.println("Пользователь с таким email не найден");
        }

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
