package io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

public class tset {


    @Test
    public void duplicate(){

        Charset utf8 = Charset.forName("utf-8");
        ByteBuf spafka = Unpooled.copiedBuffer("spafka", utf8);

        ByteBuf duplicate = spafka.duplicate();
        duplicate.setByte(0,(byte)'z');

        System.out.println(spafka.toString(utf8));
        System.out.println(duplicate.toString(utf8));
    }

    @Test
    public void slice(){

        Charset utf8 = Charset.forName("utf-8");
        ByteBuf spafka = Unpooled.copiedBuffer("spafka", utf8);

        ByteBuf slice = spafka.slice();
        slice.setByte(0,(byte)'z');

        System.out.println(spafka.toString(utf8));
        System.out.println(slice.toString(utf8));
    }

    @Test
    public void copy(){

        Charset utf8 = Charset.forName("utf-8");
        ByteBuf spafka = Unpooled.copiedBuffer("spafka", utf8);

        ByteBuf copy = spafka.copy();
        copy.setByte(0,(byte)'z');

        System.out.println(spafka.toString(utf8));
        System.out.println(copy.toString(utf8));
    }


}
