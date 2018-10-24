package com.github.spafka;

import kafka.server.KafkaServerStartable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class kafkaServer {


    public static void main(String[] args) throws IOException {

        FileUtils.deleteDirectory(new File("/data"));


        InputStream is = kafkaServer.class.getResourceAsStream("/server.properties");
        Properties p = new Properties();
        p.load(is);
        is.close();
        KafkaServerStartable kafkaServerStartable = KafkaServerStartable.fromProps(p);
        kafkaServerStartable.startup();
        kafkaServerStartable.awaitShutdown();
    }
}
