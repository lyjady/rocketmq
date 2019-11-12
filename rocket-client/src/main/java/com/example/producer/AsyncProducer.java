package com.example.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2019/11/12 22:56
 */
public class AsyncProducer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("custom_group");
        producer.setNamesrvAddr("192.168.0.110:9876");
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < 5; i++) {
            Message message = new Message("topic_test", "tagA", ("RocketMQ Test" + i).getBytes(StandardCharsets.UTF_8));
            producer.send(message);
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送成功");
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送失败");
                }
            });
        }
        producer.shutdown();
    }
}
