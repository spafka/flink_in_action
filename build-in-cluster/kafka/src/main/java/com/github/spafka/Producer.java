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

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer extends Thread {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;


    public Producer(String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost" + ":" + 9092);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "test");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer(props);
        this.topic = topic;
    }

    @Override
    public void run() {
        int messageNo = 1;
        while (true && messageNo <= Integer.MAX_VALUE) {
            try {
                producer.send(new ProducerRecord(topic,messageNo+"",
                        "23 23 03 FE 4C 4B 35 41 31 43 31 4B 32 47 41 30 30 30 31 39 34 01 00 AB 12 0A 11 0B 33 01 01 01 03 01 01 36 00 00 15 C7 05 E6 27 A4 40 01 2E EA 60 11 00 05 00 07 28 B8 6C 01 CE C3 21 06 01 03 0E E2 01 20 0E D0 01 01 41 01 01 41 02 01 01 02 4B 56 AE 4E 66 47 05 E6 27 B0 08 01 01 05 E6 27 A4 00 28 00 01 28 0E DC 0E D7 0E E2 0E D6 0E DD 0E D7 0E DA 0E D4 0E D7 0E D7 0E D4 0E D1 0E D6 0E D6 0E D6 0E DA 0E D3 0E D7 0E D7 0E DA 0E D6 0E D4 0E D3 0E D7 0E D7 0E D3 0E D7 0E D6 0E D3 0E D3 0E D3 0E D0 0E D6 0E D4 0E DD 0E D4 0E D4 0E D6 0E D9 0E D7 09 01 01 00 08 41 41 41 41 41 41 41 41 6F"));
                messageNo += 1;
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer("spafka");
        new Thread(producer).start();
        producer.join();


    }
}
