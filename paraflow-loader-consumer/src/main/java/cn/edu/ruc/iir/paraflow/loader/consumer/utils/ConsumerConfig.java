package cn.edu.ruc.iir.paraflow.loader.consumer.utils;
import cn.edu.ruc.iir.paraflow.commons.exceptions.ConfigFileNotFoundException;
import cn.edu.ruc.iir.paraflow.commons.utils.ParaFlowConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsumerConfig
{
    private ParaFlowConfig paraflowConfig;
    private ConsumerConfig()
    {}

    private static class MetaConfigHolder
    {
        private static final ConsumerConfig instance = new ConsumerConfig();
    }

    public static final ConsumerConfig INSTANCE()
    {
        return MetaConfigHolder.instance;
    }

    public void init(String configPath) throws ConfigFileNotFoundException
    {
        paraflowConfig = new ParaFlowConfig(configPath);
        paraflowConfig.build();
    }

    public boolean validate()
    {
        // todo alice: validate configuration content
        try {
            String str = getMetaServerHost();
            String regEx = "^[a-zA-Z]+://[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}:[0-9]{1,5}(/[a-zA-Z]+)+$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            boolean result = m.find();
            if (!result) {
                return false;
            }
            getMetaServerPort();
            getGroupId();
            getKafkaBootstrapServers();
            getKafkaAcks();
            getKafkaRetries();
            getKafkaBatchSize();
            getKafkaLingerMs();
            getKafkaBufferMem();
            getKafkaKeyDeserializerClass();
            getKafkaValueDeserializerClass();
            getKafkaPartitionerClass();
            getConsumerShutdownTimeout();
            getMetaClientShutdownTimeout();
            getKafkaThreadNum();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getKafkaBootstrapServers()
    {
        return paraflowConfig.getProperty("kafka.bootstrap.servers");
    }

    public int getMetaServerPort()
    {
        return Integer.parseInt(paraflowConfig.getProperty("meta.server.port"));
    }

    public String getMetaServerHost()
    {
        return paraflowConfig.getProperty("meta.server.host");
    }

    public String getGroupId()
    {
        return paraflowConfig.getProperty("consumer.group.id");
    }

    public String getKafkaAcks()
    {
        return paraflowConfig.getProperty("kafka.acks");
    }

    public int getKafkaRetries()
    {
        return Integer.parseInt(paraflowConfig.getProperty("kafka.retries"));
    }

    public int getKafkaBatchSize()
    {
        return Integer.parseInt(paraflowConfig.getProperty("kafka.batch.size"));
    }

    public int getKafkaLingerMs()
    {
        return Integer.parseInt(paraflowConfig.getProperty("kafka.linger.ms"));
    }

    public int getKafkaBufferMem()
    {
        return Integer.parseInt(paraflowConfig.getProperty("kafka.buffer.memory"));
    }

    public String getKafkaKeyDeserializerClass()
    {
        return paraflowConfig.getProperty("kafka.key.deserializer");
    }

    public String getKafkaValueDeserializerClass()
    {
        return paraflowConfig.getProperty("kafka.value.deserializer");
    }

    public String getKafkaPartitionerClass()
    {
        return paraflowConfig.getProperty("kafka.partitioner");
    }

    public int getKafkaThreadNum()
    {
        return Integer.parseInt(paraflowConfig.getProperty("consumer.thread.num"));
    }

    public int getConsumerShutdownTimeout()
    {
        return Integer.parseInt(paraflowConfig.getProperty("consumer.shutdown.timeout"));
    }

    public String getHDFSWarehouse()
    {
        if (paraflowConfig.getProperty("hdfs.warehouse").endsWith("/")) {
            return paraflowConfig.getProperty("hdfs.warehouse").substring(
                    0, paraflowConfig.getProperty("hdfs.warehouse").length() - 2);
        }
        else {
            return paraflowConfig.getProperty("hdfs.warehouse");
        }
    }

    public int getBufferOfferBlockSize()
    {
        return Integer.parseInt(paraflowConfig.getProperty("consumer.buffer.poll.block.size"));
    }

    public long getBufferPollTimeout()
    {
        return Long.parseLong(paraflowConfig.getProperty("consumer.buffer.poll.timeout"));
    }

    public int getMetaClientShutdownTimeout()
    {
        return Integer.parseInt(paraflowConfig.getProperty("meta.client.shutdown.timeout"));
    }

    public long getConsumerPollTimeout()
    {
        return Long.parseLong(paraflowConfig.getProperty("consumer.poll.timeout.ms"));
    }

    public long getOrcFileStripeSize()
    {
        return Long.parseLong(paraflowConfig.getProperty("orc.file.stripe.size"));
    }

    public int getOrcFileBufferSize()
    {
        return Integer.parseInt(paraflowConfig.getProperty("orc.file.buffer.size"));
    }

    public long getOrcFileBlockSize()
    {
        return Long.parseLong(paraflowConfig.getProperty("orc.file.block.size"));
    }
}
