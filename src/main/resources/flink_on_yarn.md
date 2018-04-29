## flink on yarn部署

flink on yarn需要的组件与版本如下
1. Zookeeper 3.4.9 用于做Flink的JobManager的HA服务
2. hadoop 2.6.5 搭建HDFS和Yarn
3. flink 1.3.2 或者 1.4.1版本（scala 2.11）

Zookeeper, HDFS 和 Yarn 的组件的安装可以参照网上的教程。

在zookeeper，HDFS 和Yarn的组件的安装好的前提下，在客户机上提交Flink任务，具体流程如下：

- 在启动Yarn-Session 之前， 设置好HADOOP_HOME,YARN_CONF_DIR ， HADOOP_CONF_DIR环境变量中三者的一个。如下所示， 根据具体的hadoop 路径来设置
```command
   $ export HADOOP_HOME=/usr/local/hadoop-current
```
- 配置flink 目录下的flink-conf.yaml, 如下所示
```yaml
jobmanager.rpc.address: localhost
jobmanager.rpc.port: 6123
jobmanager.heap.mb: 256
taskmanager.heap.mb: 512
taskmanager.numberOfTaskSlots: 1
taskmanager.memory.preallocate: false
parallelism.default: 1
jobmanager.web.port: 8081

# yarn
yarn.maximum-failed-containers: 99999

#akka config
akka.watch.heartbeat.interval: 5 s
akka.watch.heartbeat.pause: 20 s
akka.ask.timeout: 60 s
akka.framesize: 20971520b

#high-avaliability
high-availability: zookeeper
## 根据安装的zookeeper信息填写
high-availability.zookeeper.quorum: localhost:2181
high-availability.zookeeper.path.root: /flink
## HA 信息存储到HDFS的目录，根据各自的Hdfs情况修改
high-availability.zookeeper.storageDir: hdfs://localhost:9000/flink/recovery/

#checkpoint config
state.backend: rocksdb
## checkpoint到HDFS的目录 根据各自安装的HDFS情况修改
state.backend.fs.checkpointdir: hdfs://localhost:9000/flink/checkpoint
## 对外checkpoint到HDFS的目录
state.checkpoints.dir: hdfs://localhost:9000/flink/savepoint

#memory config
env.java.opts: -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+AlwaysPreTouch -server -XX:+HeapDumpOnOutOfMemoryError
yarn.heap-cutoff-ratio: 0.2
taskmanager.memory.off-heap: true

```
- 提交Yarn-Session，切换到flink的bin 目录下,提交命令如下
```command
   $ ./yarn-session.sh -n 2 -s 6 -jm 3072 -tm 6144 -nm test -d
```
启动yarn-session的参数解释如下

参数 | 参数解释 |设置推荐
---|---|---
-n(--container) | taskmanager的数量 |
-s(--slots)| 用启动应用所需的slot数量/ -s 的值向上取整，有时可以多一些taskmanager，做冗余 每个taskmanager的slot数量，默认一个slot一个core，默认每个taskmanager的slot的个数为1 | 6～10
-jm | jobmanager的内存（单位MB)| 3072
-tm | 每个taskmanager的内存（单位MB)| 根据core 与内存的比例来设置，-s的值＊ （core与内存的比）来算
-nm | yarn 的appName(现在yarn的ui上的名字)｜
-d |后台执行|

- 提交yarn－session 后，可以在yarn的ui上看到一个应用（应用有一个appId）, 切换到flink的bin目录下，提交flink 应用。命令如下
```command
 $ ./flink -run file:///home/yarn/test.jar -a 1 -p 12 -yid appId -nm flink-test -d
```
启动flink 应用的参数解释如下

参数 | 参数解释
---|---
-j | 运行flink 应用的jar所在的目录
-a | 运行flink 应用的主方法的参数
-p | 运行flink应用的并行度
-c | 运行flink应用的主类, 可以通过在打包设置主类
-nm | flink 应用名字，在flink-ui 上面展示
-d | 后台执行
--fromsavepoint| flink 应用启动的状态恢复点