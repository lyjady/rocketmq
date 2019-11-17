package com.example.base.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:27
 * @description 单向发送消息
 */
public class OneWayProducer {

    public static void main(String[] args) throws Exception {
        //1.创建生产者实例并指定生产者组
        DefaultMQProducer producer = new DefaultMQProducer("OrderProducerGroup");
        //2.设置NameServer地址
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        //3.启动生产者实例
        producer.start();
        //4.发送消息
        for (int i = 0; i < 5; i++) {
            //5.创建消息对象,并指定topic、tag和消息体
            Message message = new Message("java-topic", "sync-tag", ("this is a sync message" + i).getBytes(StandardCharsets.UTF_8));
            //6.发送单向消息到broker
            producer.sendOneway(message);
            TimeUnit.SECONDS.sleep(1);
        }
        //8.关闭生产者
        producer.shutdown();
    }
}
