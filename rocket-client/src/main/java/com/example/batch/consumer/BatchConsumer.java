package com.example.batch.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:33
 * @description 负载均衡消费者
 */
public class BatchConsumer {

    public static void main(String[] args) throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("BatchConsumerGroup");
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        consumer.subscribe("BatchTopic", "BatchTag");
        //5.注册回调函数
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt ext : msg) {
                System.out.println(new String(ext.getBody(), StandardCharsets.UTF_8));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
        System.out.println("批量消息消费者启动");
    }
}
