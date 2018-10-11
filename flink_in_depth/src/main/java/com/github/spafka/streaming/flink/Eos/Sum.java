package com.github.spafka.streaming.flink.Eos;

public class Sum {


    public static void main(String[] args) {

        int sum=0;
        for (int i = 0; i <= 250000; i++) {
            sum+=i;
        }

        System.out.println(sum);
    }
}
