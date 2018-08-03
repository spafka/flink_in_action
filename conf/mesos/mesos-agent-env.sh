# This file contains environment variables that are passed to mesos-slave.
# To get a description of all options run mesos-slave --help; any option
# supported as a command-line option is also supported as an environment
# variable.

# You must at least set MESOS_master.

# The mesos master URL to contact. Should be host:port for
# non-ZooKeeper based masters, otherwise a zk:// or file:// URL.
export MESOS_master=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos

# Other options you're likely to want to set:
export MESOS_log_dir=/var/logs/mesos/slave
export MESOS_work_dir=/var/run/mesos
export MESOS_isolation=cgroups
export MESOS_resources="mem(*):8096;cpus(*):8"
