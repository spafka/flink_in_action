# This file contains environment variables that are passed to mesos-master.
# To get a description of all options run mesos-master --help; any option
# supported as a command-line option is also supported as an environment
# variable.

# Some options you're likely to want to set:
export MESOS_log_dir=/var/logs/mesos/master
export MESOS_zk=zk://iovdc13:2181,iovdc14:2181,iovdc15:2181/mesos
export MESOS_quorum=2
export MESOS_work_dir=/var/lib/mesos/master
