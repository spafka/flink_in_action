Installing Mesos
Please follow the instructions on how to setup Mesos on the official website.

After installation you have to configure the set of master and agent nodes by creating the files MESOS_HOME/etc/mesos/masters and MESOS_HOME/etc/mesos/slaves. These files contain in each row a single hostname on which the respective component will be started (assuming SSH access to these nodes).

Next you have to create MESOS_HOME/etc/mesos/mesos-master-env.sh or use the template found in the same directory. In this file, you have to define

export MESOS_work_dir=WORK_DIRECTORY
and it is recommended to uncommment

export MESOS_log_dir=LOGGING_DIRECTORY
In order to configure the Mesos agents, you have to create MESOS_HOME/etc/mesos/mesos-agent-env.sh or use the template found in the same directory. You have to configure

export MESOS_master=MASTER_HOSTNAME:MASTER_PORT
and uncomment

export MESOS_log_dir=LOGGING_DIRECTORY
export MESOS_work_dir=WORK_DIRECTORY
Mesos Library
In order to run Java applications with Mesos you have to export MESOS_NATIVE_JAVA_LIBRARY=MESOS_HOME/lib/libmesos.so o


bin/mesos-appmaster.sh \
    -Djobmanager.heap.mb=1024 \
    -Djobmanager.rpc.port=6123 \
    -Djobmanager.web.port=8081 \
    -Dmesos.initial-tasks=1 \
    -Dmesos.resourcemanager.tasks.mem=4096 \
    -Dtaskmanager.heap.mb=3500 \
    -Dtaskmanager.numberOfTaskSlots=2 

    
    
 {
                       "id": "flinkdemo1",
                       "cmd": "/usr/tass/flink/bin/mesos-appmaster.sh -Dmesos.master=zk://kafkazoo1:2181,kafkazoo1:2182,kafkazoo3:2181/mesos -Djobmanager.heap.mb=1024 -Djobmanager.rpc.port=6123 -Djobmanager.web.port=8081 -Dmesos.initial-tasks=1 -Dmesos.resourcemanager.tasks.mem=1024 -Dtaskmanager.heap.mb=1024 -Dtaskmanager.numberOfTaskSlots=2 -Dparallelism.default=2 -Dmesos.resourcemanager.tasks.cpus=1",
                       "cpus": 1.0,
                       "mem": 1024
 }