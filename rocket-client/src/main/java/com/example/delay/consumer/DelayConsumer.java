package com.example.delay.consumer;

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
public class DelayConsumer {

    public static void main(String[] args) throws Exception{
        //1.创建消费者实例并指定消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("OrderConsumerGroup");
        //2.设置NameServer地址
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        //3.订阅topic,指定tag(*表示所有的tag)
        consumer.subscribe("delay-topic", "*");
        //5.注册回调函数
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt ext : msg) {
                System.out.println(new String(ext.getBody(), StandardCharsets.UTF_8) + ", 延迟时间" + (System.currentTimeMillis() - ext.getStoreTimestamp()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
        System.out.println("延迟消费者启动");
    }
}
