package com.example.transaction;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2019/11/25 21:20
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TransactionGroup");
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        consumer.subscribe("TransactionTopic", "*");
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt ext : msg) {
                System.out.println(new String(ext.getBody(), StandardCharsets.UTF_8));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("消费者启动");
    }
}
