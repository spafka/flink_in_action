# flink on  mesos 

| id        | cpu (core)   |  memory(G)  |
| --------   | -----:   | :----: |
| hadoop11        | 4     |   8    |
| hadoop12        | 4      |   8    |
| hadoop13        | 4      |   8   |
| hadoop14        | 4     |   8    |


参数说明 mesos.resourcemanager.tasks.mem=3172 mesos分配的每个task的内存
        taskmanager.heap.mb=2048 -Xmx -Xms 指定的java 堆内存
        剩余的内存属于堆外内存  即 -MaxDirectMemorySize
        high-availability.cluster-id 每个单独的程序都有一个都需指定一个clusterId,否则会把资源合在一起，看作一个集群
        

## marathon
/root/marathon 
nohup bin/marathon --master zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos --zk zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/marathon 2>&1 &


### ev_etl 总共需大约15solt  2core 6g  total=1+6G

```bash
 /usr/tass/flink-1.5.1/bin/mesos-appmaster.sh  \
      -Dmesos.master=zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos  \
      -Djobmanager.heap.mb=512 \
      -Djobmanager.rpc.port=6124 \
      -Drest.port=8082 \
      -Dmesos.initial-tasks=2 \
      -Dmesos.resourcemanager.tasks.mem=3172 \
      -Dtaskmanager.heap.mb=2048 \
      -Dtaskmanager.numberOfTaskSlots=10  \
      -Dparallelism.default=3 \
      -Dmesos.resourcemanager.tasks.container.type=mesos \
      -Dmesos.resourcemanager.framework.name=ev_etl \
      -Dmesos.resourcemanager.tasks.cpus=1 \
      -Dhigh-availability.cluster-id=/ev_etl

```
      
      
### ev_test  6solt 1Core+3G total=1+3G 
```bash
 /usr/tass/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos  \
            -Djobmanager.heap.mb=512 \
            -Djobmanager.rpc.port=6125 \
            -Drest.port=8083 \
            -Dmesos.initial-tasks=1 \
            -Dmesos.resourcemanager.tasks.mem=3172 \
            -Dtaskmanager.heap.mb=2048 \
            -Dtaskmanager.numberOfTaskSlots=6 \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=ev_test \
            -Dmesos.resourcemanager.tasks.cpus=1 \
            -Dhigh-availability.cluster-id=/ev_test
```      


### fv 2core 4G 

```bash
/usr/tass/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos  \
            -Djobmanager.heap.mb=512 \
            -Djobmanager.rpc.port=6126 \
            -Drest.port=8084 \
            -Dmesos.initial-tasks=2 \
            -Dmesos.resourcemanager.tasks.mem=2048 \
            -Dtaskmanager.heap.mb=1800 \
            -Dtaskmanager.numberOfTaskSlots=10  \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=fv \
            -Dmesos.resourcemanager.tasks.cpus=1 \
            -Dhigh-availability.cluster-id=/fv
```

### test 2core 4g
```bash

/usr/tass/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos  \
            -Djobmanager.heap.mb=512 \
            -Djobmanager.rpc.port=6127 \
            -Drest.port=8085 \
            -Dmesos.initial-tasks=2 \
            -Dmesos.resourcemanager.tasks.mem=2048 \
            -Dtaskmanager.heap.mb=1800 \
            -Dtaskmanager.numberOfTaskSlots=10  \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=test \
            -Dmesos.resourcemanager.tasks.cpus=1 \
            -Dhigh-availability.cluster-id=/test

```
### push  1core  2G 
```bash
/usr/tass/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos  \
            -Djobmanager.heap.mb=512 \
            -Djobmanager.rpc.port=6128 \
            -Drest.port=8086 \
            -Dmesos.initial-tasks=1 \
            -Dmesos.resourcemanager.tasks.mem=2048 \
            -Dtaskmanager.heap.mb=1800 \
            -Dtaskmanager.numberOfTaskSlots=3  \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=event_push \
            -Dmesos.resourcemanager.tasks.cpus=1 \
            -Dhigh-availability.cluster-id=/event_push

```

```bash
 /usr/tass/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://kafkazoo1:2181,kafkazoo2:2181,kafkazoo3:2181/mesos  \
            -Djobmanager.heap.mb=2048 \
            -Djobmanager.rpc.port=6125 \
            -Drest.port=8083 \
            -Dmesos.initial-tasks=4 \
            -Dmesos.resourcemanager.tasks.mem=4096 \
            -Dtaskmanager.heap.mb=3172 \
            -Dtaskmanager.numberOfTaskSlots=25  \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=flink-all-in-one \
            -Dmesos.resourcemanager.tasks.cpus=3 \
            -Dhigh-availability.cluster-id=/mesos
```

