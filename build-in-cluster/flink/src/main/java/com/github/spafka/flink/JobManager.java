package com.github.spafka.flink;

import org.apache.flink.runtime.jobmanager.JobManager$;

public class JobManager {

    public static void main(String[] args) {

        args = String.format("--configDir %s --executionMode cluster --host localhost --webui-port 8081",
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("flink-conf.yaml")
                        .getFile()+"/..").split(" ");
        JobManager$.MODULE$.main(args);

    }
}
