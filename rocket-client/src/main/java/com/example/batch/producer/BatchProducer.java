package com.example.batch.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:09
 * @description 同步生产者
 */
public class BatchProducer {

    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("BatchProducerGroup");
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("BatchTopic", "BatchTag", "Batch Message 1".getBytes()));
        messages.add(new Message("BatchTopic", "BatchTag", "Batch Message 2".getBytes()));
        messages.add(new Message("BatchTopic", "BatchTag", "Batch Message 3".getBytes()));
        producer.start();
        SendResult sendResult = producer.send(messages);
        System.out.println(sendResult);
        producer.shutdown();
    }
}
