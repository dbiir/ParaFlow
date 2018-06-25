package cn.edu.ruc.iir.paraflow.loader.consumer.example;

import cn.edu.ruc.iir.paraflow.commons.TopicFiber;
import cn.edu.ruc.iir.paraflow.commons.exceptions.ConfigFileNotFoundException;
import cn.edu.ruc.iir.paraflow.commons.func.DeserializableFunction;
import cn.edu.ruc.iir.paraflow.loader.consumer.DefaultConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.LinkedList;
import java.util.List;

public class ExampleConsumer
{
    private void exampleTest(String configPath)
    {
        try {
            final DefaultConsumer consumer;
            List<TopicPartition> topicPartitions = new LinkedList<>();
            TopicPartition topicPartition = new TopicPartition("exampledb.exampletbl", 0);
            topicPartitions.add(topicPartition);
            List<TopicFiber> topicFibers = new LinkedList<>();
            for (int i = 0; i < 1000; i++) {
                topicFibers.add(new TopicFiber("exampledb.exampletbl", i));
            }
            final String dbName = "exampledb";
            final String tblName = "exampletbl";
            try {
                consumer = new DefaultConsumer(configPath, topicPartitions, topicFibers);
            }
            catch (ConfigFileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            consumer.consume();
            DeserializableFunction<String, Integer> func = (v) -> Integer.parseInt(v) % 1000;
            consumer.registerFiberFunc(dbName, tblName, func);
//        try {
//            Thread.sleep(10000);
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        consumer.shutdown();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        ExampleConsumer consumer = new ExampleConsumer();
        consumer.exampleTest(args[0]);
    }
}
