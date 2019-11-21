package com.example.filter.sql.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:33
 * @description 负载均衡消费者
 */
public class SqlConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SqlConsumerGroup");
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        consumer.subscribe("sql-topic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt ext : msg) {
                System.out.println(new String(ext.getBody(), StandardCharsets.UTF_8) + ", " + ext.getUserProperty("i"));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("sql消费者启动");
    }
}
