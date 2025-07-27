import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = "com.example")
@EnableTransactionManagement
public class AppConfig {

   @Autowired
   private Environment env;

   @Bean
   public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
      dataSource.setUrl(env.getRequiredProperty("db.url"));
      dataSource.setUsername(env.getRequiredProperty("db.username"));
      dataSource.setPassword(env.getRequiredProperty("db.password"));
      return dataSource;
   }

   @Bean
   public LocalSessionFactoryBean sessionFactory() {
      LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
      sessionFactory.setDataSource(dataSource());  // Теперь метод доступен
      sessionFactory.setPackagesToScan("model");
      sessionFactory.setHibernateProperties(hibernateProperties());
      return sessionFactory;
   }

   private Properties hibernateProperties() {
      Properties properties = new Properties();
      properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
      properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
      properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
      return properties;
   }

   @Bean
   public HibernateTransactionManager transactionManager() {
      HibernateTransactionManager transactionManager = new HibernateTransactionManager();
      transactionManager.setSessionFactory(sessionFactory().getObject());
      return transactionManager;
   }
}
   @Bean
   public HibernateTransactionManager getTransactionManager() {
      HibernateTransactionManager transactionManager = new HibernateTransactionManager();
      transactionManager.setSessionFactory(getSessionFactory().getObject());
      return transactionManager;
   }
   @Bean
   public LocalSessionFactoryBean sessionFactory() {
      LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
      sessionFactory.setDataSource(dataSource());
      sessionFactory.setPackagesToScan("model");
      sessionFactory.setHibernateProperties(hibernateProperties());
      return sessionFactory;
   }

   private Properties hibernateProperties() {
      Properties properties = new Properties();
      properties.put("hibernate.dialect", "org.hibernate.dialect.YOUR_DIALECT");
      properties.put("hibernate.show_sql", "true");
      properties.put("hibernate.hbm2ddl.auto", "update");
      return properties;
   }
}
