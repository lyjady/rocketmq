package com.example.transaction;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.*;

/**
 * @author LinYongJin
 * @date 2019/11/25 21:13
 */
public class Producer {

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        TransactionMQProducer producer = new TransactionMQProducer("TransactionProducer");
        producer.setNamesrvAddr("192.168.0.110:9876;192.168.0.105:9876");
        //设置事务回调
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 本地事务回调
             * @param msg 消息
             * @param arg
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                System.out.println("执行本地事务");
                if ("tagA".equalsIgnoreCase(msg.getTags())) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if ("tagB".equalsIgnoreCase(msg.getTags())) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else {
                    return LocalTransactionState.UNKNOW;
                }
            }

            /**
             * 检查消息状态
             * @param msg 消息
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                System.out.println("检查本地事务状态");
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), r -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });
        producer.setExecutorService(executorService);
        producer.start();
        String[] tags = {"tagA", "tagB", "tagC"};
        for (int i = 0; i < 3; i++) {
            Message message = new Message("TransactionTopic", tags[i], ("Transaction Message" + i).getBytes());
            SendResult sendResult = producer.sendMessageInTransaction(message, null);
            System.out.println(sendResult);
        }
        TimeUnit.SECONDS.sleep(1);
    }
}
