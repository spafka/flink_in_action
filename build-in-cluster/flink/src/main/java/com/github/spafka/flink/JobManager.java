package com.github.spafka.flink;

import org.apache.flink.runtime.entrypoint.StandaloneSessionClusterEntrypoint;

public class JobManager {

    public static void main(String[] args) {

        System.setProperty("log.file","jobmanager.log");

        args = String.format("--configDir %s --executionMode cluster --host localhost --webui-port 8081",
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("flink-conf.yaml")
                        .getFile()+"/..").split(" ");
        StandaloneSessionClusterEntrypoint.main(args);

    }
}
