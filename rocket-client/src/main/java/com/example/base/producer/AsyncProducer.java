package com.example.base.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2019/11/17 14:17
 * @description 异步发送者
 */
public class AsyncProducer {

    public static void main(String[] args) throws Exception {
        //1.创建生产者实例并指定生产者组
        DefaultMQProducer producer = new DefaultMQProducer("OrderProducerGroup");
        //2.设置NameServer地址
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        //3.启动producer
        producer.start();
        //4.设置异步发送失败的时候重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //5.发送消息
        for (int i = 0; i < 5; i++) {
            //6.创建消息对象,并指定topic、tag和消息体
            Message message = new Message("java-topic", "async-tag", ("this is a async message" + i).getBytes(StandardCharsets.UTF_8));
            //7.发送消息到broker,异步回调
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
            //8.打印同步结果
            TimeUnit.SECONDS.sleep(1);
        }
        //9.关闭生产者
        producer.shutdown();
    }
}
