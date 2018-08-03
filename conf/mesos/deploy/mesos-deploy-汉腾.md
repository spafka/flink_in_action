# flink on  mesos 

| id        | cpu (core)   |  memory(G)  |
| --------   | -----:   | :----: |
| iovdc16        | 8     |   8    |
| iovdc17        | 8      |   8    |
| iovdc18        | 8      |   8   |
| iovdc19        | 8     |   8    |
| iovdc20        | 8     |   8    |

参数说明 mesos.resourcemanager.tasks.mem=3172 mesos分配的每个task的内存
        taskmanager.heap.mb=2048 -Xmx -Xms 指定的java 堆内存
        剩余的内存属于堆外内存  即 -MaxDirectMemorySize
        high-availability.cluster-id 每个单独的程序都有一个都需指定一个clusterId,否则会把资源合在一起，看作一个集群
        

## marathon
/root/marathon 
nohup bin/marathon --master zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos --zk zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/marathon 2>&1 &


### ev_etl 总共需大约15solt  8core 8g  total=1+8G

```bash
/home/hadoop/flink-1.5.1/bin/mesos-appmaster.sh  \
      -Dmesos.master=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos  \
      -Djobmanager.heap.mb=1024 \
      -Djobmanager.rpc.port=6124 \
      -Drest.port=8082 \
      -Dmesos.initial-tasks=4 \
      -Dmesos.resourcemanager.tasks.mem=2048 \
      -Dtaskmanager.heap.mb=1800 \
      -Dtaskmanager.numberOfTaskSlots=5 \
      -Dparallelism.default=3 \
      -Dmesos.resourcemanager.tasks.container.type=mesos \
      -Dmesos.resourcemanager.framework.name=ev_etl \
      -Dmesos.resourcemanager.tasks.cpus=1 \
      -Dhigh-availability.cluster-id=/ev_etl

```
      
      
### ev_test  6solt 1Core+3G total=1+3G 
```bash
/home/hadoop/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos  \
            -Djobmanager.heap.mb=1024 \
            -Djobmanager.rpc.port=6126 \
            -Drest.port=8084 \
            -Dmesos.initial-tasks=2 \
            -Dmesos.resourcemanager.tasks.mem=2048 \
            -Dtaskmanager.heap.mb=1800 \
            -Dtaskmanager.numberOfTaskSlots=10  \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=ev_test \
            -Dmesos.resourcemanager.tasks.cpus=2 \
            -Dhigh-availability.cluster-id=/ev_test
```      


### fv 2core 4G 

```bash
/home/hadoop/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos  \
            -Djobmanager.heap.mb=1024 \
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

/home/hadoop/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos  \
            -Djobmanager.heap.mb=1024 \
            -Djobmanager.rpc.port=6127 \
            -Drest.port=8085 \
            -Dmesos.initial-tasks=2 \
            -Dmesos.resourcemanager.tasks.mem=2048 \
            -Dtaskmanager.heap.mb=1800 \
            -Dtaskmanager.numberOfTaskSlots=6 \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=test \
            -Dmesos.resourcemanager.tasks.cpus=2 \
            -Dhigh-availability.cluster-id=/test

```

```bash
/home/hadoop/flink-1.5.1/bin/mesos-appmaster.sh  \
            -Dmesos.master=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos  \
            -Djobmanager.heap.mb=1024 \
            -Djobmanager.rpc.port=6128 \
            -Drest.port=8086 \
            -Dmesos.initial-tasks=1 \
            -Dmesos.resourcemanager.tasks.mem=2048 \
            -Dtaskmanager.heap.mb=1800 \
            -Dtaskmanager.numberOfTaskSlots=6  \
            -Dparallelism.default=3 \
            -Dmesos.resourcemanager.tasks.container.type=mesos \
            -Dmesos.resourcemanager.framework.name=event_push \
            -Dmesos.resourcemanager.tasks.cpus=2 \
            -Dhigh-availability.cluster-id=/event_push
```