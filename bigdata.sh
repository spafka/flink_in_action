#!/usr/bin/env bash

USAGE="Usage: bigdata.sh  [hadoop|hbase|flink] [start|stop]"

su hadoop

EXECUTIONMODE=$1
STARTSTOP=$2

if [[ $STARTSTOP != "start" ]] &&  [[ $STARTSTOP != "stop" ]]; then
  echo $USAGE
  exit 1
fi

echo ${EXECUTIONMODE} ${STARTSTOP}

echo 'HADOOP_HOME=' $HADOOP_HOME
echo 'HBASE_HOME=' $HBASE_HOME
echo 'FLINK_HOME=' $FLINK_HOME

case $EXECUTIONMODE in
    (hadoop)
      exec $HADOOP_HOME/sbin/${STARTSTOP}-dfs.sh
      ;;
    (hbase)
      exec  $HBASE/bin/${STARTSTOP}-hbase.sh
      ;;
    (flink)
      exec  $FLINK/bin/${STARTSTOP}-cluster.sh
      ;;
esac