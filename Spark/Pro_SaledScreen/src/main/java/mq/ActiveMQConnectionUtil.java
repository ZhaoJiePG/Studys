package mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

public class ActiveMQConnectionUtil {

    public static Connection getConnection(String url) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        Connection connection = factory.createConnection();
        connection.start();
        return connection;
    }

    private static final String QUEUENAME = "test-queue";
    // private static final   String URL ="tcp://10.0.0.190:61616";
    ///失效转移 生产者为高可用
    private static final String FAILOVERURL = "failover:(tcp://10.149.1.150:61616,tcp://10.149.1.30:61616,tcp://10.149.1.31:61616)?randomize=true";

    //队列模式
    public void procedureTest() throws JMSException {
        Connection connection = getConnection(FAILOVERURL);
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUENAME);
        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage;
        for (int i = 0; i < 100; i++) {
            textMessage = session.createTextMessage("测试数据：" + i);
            producer.send(textMessage);
            session.commit();
        }
        producer.close();
        session.close();
        connection.close();

    }
//
//    public void consumerTest() throws JMSException {
//        Connection connection = getConnection(FAILOVERURL);
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Queue queue = session.createQueue(QUEUENAME);
//        MessageConsumer consumer = session.createConsumer(queue);
//        consumer.setMessageListener((Message message) -> {
//            TextMessage textMessage = (TextMessage) message;
//            try {
//                System.out.println("这是接收到数据： =  " + textMessage.getText());
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    public static void main(String[] args) throws JMSException {
//        ActiveMQConnectionUtil test = new ActiveMQConnectionUtil();
//        test.procedureTest();
        //test.consumerTest();


        Map<String,String> tmp = new HashMap<String,String>();
        tmp.put("a","a1");
//        tmp.forEach((k,b) -> System.out.println(k+b));
    }

}
