package hello;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  @Bean
  TopicExchange exchange() {
    return new TopicExchange("spring-boot-exchange", true, false);
  }

  @Bean
  ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost", 5672);
    final String user = "impersonator1";
    connectionFactory.setUsername(user);
    final String vhost = "impersonator";
    connectionFactory.setPassword(vhost);
    connectionFactory.setVirtualHost("/" + vhost);
    connectionFactory.addChannelListener((channel, transactional) -> System.out.println("Channel created"));
    connectionFactory.addConnectionListener(new ConnectionListener() {
      @Override
      public void onCreate(Connection connection) {
        System.out.println("Connection created");
      }

      @Override
      public void onClose(Connection connection) {
        System.out.println("Connection closed");
      }
    });
    return connectionFactory;
  }

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Application.class, args);
  }

}