package com.github.spafka.flink;

import org.apache.flink.runtime.taskmanager.TaskManager$;

public class TaskManager {

    public static void main(String[] args) {

     args=   String.format("--configDir %s",
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("flink-conf.yaml")
                        .getFile()+"/..").split(" ");
        TaskManager$.MODULE$.main(args);

    }
}
