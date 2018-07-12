#!/usr/bin/env bash

cd $ZK_HOME
bin/zkServer.sh start

echo "zk started"
sleep 10s

cd $HADOOP_HOME
sbin/stop-all.sh
sleep 10s
sbin/start-dfs.sh
sleep 10s
sbin/start-yarn.sh

echo "Hadoop started"
sleep 10s

cd $FLINK_HOME
bin/yarn-session.sh -n 1 -s 6 -jm 2048 -tm 6144 -nm test -d

echo "flink on yarn started"