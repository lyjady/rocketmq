package com.example.order.consumer;

import com.alibaba.fastjson.JSON;
import com.example.order.OrderEntry;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author LinYongJin
 * @date 2019/11/21 21:21
 */
public class OrderConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("OrderConsumerGroup");
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        consumer.subscribe("OrderTopic", "*");
        //注册消息监听器(顺序监听)
        consumer.registerMessageListener((MessageListenerOrderly) (msg, context) -> {
            msg.forEach(messageExt -> {
                OrderEntry order = JSON.parseObject(new String(messageExt.getBody(), StandardCharsets.UTF_8), OrderEntry.class);
                System.out.println(order + "queueId "+ messageExt.getQueueId() +", thread-name: " + Thread.currentThread().getName());
            });
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
        System.out.println("消费者启动");
    }
}
