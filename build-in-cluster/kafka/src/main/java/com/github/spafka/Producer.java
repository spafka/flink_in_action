/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.spafka;


import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

public class Producer extends Thread {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;


    public Producer(String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "iovdc13" + ":" + 9092);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "test");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        producer = new KafkaProducer(props);
        this.topic = topic;
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer("reissue2sh");
        new Thread(producer).start();
        producer.join();


    }



    @Override
    public void run() {


        BitAndByteUtil b2b = new BitAndByteUtil();


        try {
            List<String> strings = FileUtils.readLines(new File("D:\\OpenAi\\spafka\\flink_in_action\\build-in-cluster\\kafka\\src\\main\\resources\\13171.txt"), Charset.defaultCharset());
            strings.forEach(x -> {
                byte[] bytes = b2b.hexStringToBytes(x);



                if (bytes[2]==1||bytes[2]==4) return;
                    bytes[2]=3;
                producer.send(new ProducerRecord(topic, bytes));
            });


        } catch (IOException e) {

        }


        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

