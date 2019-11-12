package com.example.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author LinYongJin
 * @date 2019/11/12 23:03
 */
public class BalanceConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("custom_group");
        consumer.setNamesrvAddr("192.168.0.110:9876");
        consumer.subscribe("topic_test", "*");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            System.out.println(msg.size());
            for (MessageExt ext : msg) {
                System.out.println(new String(ext.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
