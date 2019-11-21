package com.example.order.producer;

import com.alibaba.fastjson.JSON;
import com.example.order.OrderEntry;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LinYongJin
 * @date 2019/11/21 21:10
 */
public class OrderProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("orderProducerGroup");
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        producer.start();
        List<OrderEntry> orderList = OrderEntry.getOrderList();
        orderList.forEach(orderEntry -> {
            String json = JSON.toJSONString(orderEntry);
            Message message = new Message("OrderTopic", "OrderTag", json.getBytes());
            try {
                //实现消息队列选择器
                SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                    /**
                     *
                     * @param mqs broker中的队列集合
                     * @param msg 发送的消息对象
                     * @param arg 选择队列的业务标识
                     * @return 指定的队列的索引
                     */
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        long id = (long) arg;
                        int index = (int) (id % mqs.size());
                        return mqs.get(index);
                    }
                }, orderEntry.getId());
                System.out.println(sendResult);
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        producer.shutdown();
    }
}
