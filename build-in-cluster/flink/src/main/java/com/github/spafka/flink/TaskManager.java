package com.github.spafka.flink;

import org.apache.flink.runtime.taskexecutor.TaskManagerRunner;

public class TaskManager {

    public static void main(String[] args) throws Exception {
        System.setProperty("log.file","taskmanager.log");
        args = String.format("--configDir %s",
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("flink-conf.yaml")
                        .getFile() + "/..").split(" ");
        TaskManagerRunner.main(args);

    }
}
