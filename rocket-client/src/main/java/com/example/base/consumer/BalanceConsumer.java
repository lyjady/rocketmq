package com.example.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:33
 * @description 负载均衡消费者
 */
public class BalanceConsumer {

    public static void main(String[] args) throws Exception{
        //1.创建消费者实例并指定消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("OrderConsumerGroup");
        //2.设置NameServer地址
        consumer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        //3.订阅topic,指定tag(*表示所有的tag)
        consumer.subscribe("java-topic", "sync-tag");
        //4.设置负载均衡模式(默认的消费模式)
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        //设置广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        //5.注册回调函数
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            for (MessageExt ext : msg) {
                System.out.println(new String(ext.getBody(), StandardCharsets.UTF_8));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
    }
}
