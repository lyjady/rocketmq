package com.example.filter.tag.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:09
 * @description 同步生产者
 */
public class TagProducer {

    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("TagProducerGroup");
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("tag-topic", "tag2", ("this is a tag message" + i).getBytes(StandardCharsets.UTF_8));
            SendResult result = producer.send(message);
            System.out.println(result);
        }
        producer.shutdown();
    }
}
