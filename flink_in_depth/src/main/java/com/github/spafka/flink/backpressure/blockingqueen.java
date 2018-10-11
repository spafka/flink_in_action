package com.github.spafka.flink.backpressure;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * 1000
 * 1526138182468
 * current offset is 1001
 * 1000
 * 1526138183473
 * current offset is 1002
 * 1000
 * 1526138184479
 * current offset is 1003
 * 1000
 * 1526138185482
 * current offset is 1004
 * <p>
 * <p>
 * 使用blockqueening 来缓冲数据，当sink消费反压时，souce会自动的阻塞住。
 *
 * @see http://wuchong.me/blog/2016/04/26/flink-internals-how-to-handle-backpressure/
 */
public class blockingqueen {


    public static void main(String[] args) throws InterruptedException {


        BlockingQueue<Runnable> queue = new ArrayBlockingQueue(1000);

        Runnable source = new Runnable() {


            int i = 1;

            @Override
            public void run() {

                for (; i < Integer.MAX_VALUE; i++) {
                    try {
                        queue.put(() -> {
                            System.out.println(System.currentTimeMillis());
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("current offset is " + i);
                }
            }
        };

        new Thread(source).start();


        Thread sink = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    queue.take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        sink.setDaemon(true);
        sink.start();


        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(queue.size());
            }
        }).start();

        Thread.sleep(10000000L);


    }

}
