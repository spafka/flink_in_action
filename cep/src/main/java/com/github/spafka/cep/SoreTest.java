package com.github.spafka.cep;

import org.apache.flink.cep.EventComparator;

import java.util.ArrayList;
import java.util.stream.Stream;

public class SoreTest {

    public static void main(String[] args) {

        BASEDTO b = new BASEDTO();
        GPSDTO gpsdto = new GPSDTO();
        CallDTO callDTO = new CallDTO();

        ArrayList<BASEDTO> list = new ArrayList<>();

        list.add(b);
        list.add(gpsdto);
        list.add(callDTO);

        Stream<BASEDTO> sorted = list.stream().sorted(new EventComparator<BASEDTO>() {
            @Override
            public int compare(BASEDTO o1, BASEDTO o2) {
                return o1.getCompareId() - o2.getCompareId();
            }
        });

        sorted.forEach(System.out::println);

    }
}
