package hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ChannelListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

  public static void main(final String... args) throws Exception {

    AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("context2.xml");
    RabbitTemplate template = ctx.getBean(RabbitTemplate.class);

    final CachingConnectionFactory connectionFactory = (CachingConnectionFactory) ctx.getBean("connectionFactory");
    connectionFactory.addConnectionListener(
        new ConnectionListener() {

          @Override
          public void onCreate(Connection connection) {
            System.out.println("Connection Created");
          }

          @Override
          public void onShutDown(ShutdownSignalException signal) {
            System.out.println("Connection Shut down");
          }

        }
    );

    connectionFactory.addChannelListener(new ChannelListener() {
      @Override
      public void onCreate(Channel channel, boolean transactional) {
        System.out.println("Channel created");
      }

      @Override
      public void onShutDown(ShutdownSignalException signal) {
        System.out.println("Channel shut down");
      }
    });

    template.convertAndSend("Hello, world!");

    Thread.sleep(1000);
    ctx.close();
  }

}