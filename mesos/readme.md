CENTOS 6 7没有这么麻烦

内核跟新
# Install a recent kernel for full support of process isolation.
$ sudo rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
$ sudo rpm -Uvh http://www.elrepo.org/elrepo-release-6-8.el6.elrepo.noarch.rpm
$ sudo yum --enablerepo=elrepo-kernel install -y kernel-lt

# Make the just installed kernel the one booted by default, and reboot.
$ sudo sed -i 's/default=1/default=0/g' /boot/grub/grub.conf
$ sudo reboot

uname -a 查看内核，如果没有更新到3以上，修改/boot/grub/grub.conf default值

# Install a few utility tools. This also forces an update of `nss`,
# which is necessary for the Java bindings to build properly.
$ sudo yum install -y tar wget git which nss

GCC 跟新


# 'Mesos > 0.21.0' requires a C++ compiler with full C++11 support,
# (e.g. GCC > 4.8) which is available via 'devtoolset-2'.
# Fetch the Scientific Linux CERN devtoolset repo file.
$ sudo wget -O /etc/yum.repos.d/slc6-devtoolset.repo http://linuxsoft.cern.ch/cern/devtoolset/slc6-devtoolset.repo

# Import the CERN GPG key.
$ sudo rpm --import http://linuxsoft.cern.ch/cern/centos/7/os/x86_64/RPM-GPG-KEY-cern

# Fetch the Apache Maven repo file.
$ sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo


SVN及一些必备组件跟新，svn为1.8 即可
<!-- yum clean metadata
yum update  -->
# 'Mesos > 0.21.0' requires 'subversion > 1.8' devel package, which is
# not available in the default repositories.
# Create a WANdisco SVN repo file to install the correct version:
$ sudo bash -c 'cat > /etc/yum.repos.d/wandisco-svn.repo <<EOF
[WANdiscoSVN]
name=WANdisco SVN Repo 1.8
enabled=1
baseurl=http://opensource.wandisco.com/centos/6/svn-1.8/RPMS/$basearch/
gpgcheck=1
gpgkey=http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco
EOF'

# Install essential development tools.
$ sudo yum groupinstall -y "Development Tools"

# Install 'devtoolset-2-toolchain' which includes GCC 4.8.2 and related packages.
$ sudo yum install -y devtoolset-2-toolchain

# Install other Mesos dependencies.
$ sudo yum install -y apache-maven python-devel python-six python-virtualenv zlib-devel libcurl-devel openssl-devel cyrus-sasl-devel cyrus-sasl-md5 apr-devel subversion-devel apr-util-devel

# Enter a shell with 'devtoolset-2' enabled.
$ scl enable devtoolset-2 bash
$ g++ --version  # Make sure you've got GCC > 4.8!

注意 动态链接库可能没有跟新，手动覆盖
strings /usr/lib64/libstdc++.so.6 | grep GLIBC 
要求要GLIBCXX_3.4.18 及以上
cp /usr/local/lib64/libstdc++.so.6 /usr/lib64/libstdc++.so.6

# Process isolation is using cgroups that are managed by 'cgconfig'.
# The 'cgconfig' service is not started by default on CentOS 6.6.
# Also the default configuration does not attach the 'perf_event' subsystem.
# To do this, add 'perf_event = /cgroup/perf_event;' to the entries in '/etc/cgconfig.conf'.
$ sudo yum install -y libcgroup
$ sudo service cgconfig start

yum -y install python-pip
pip install pytz --upgrade


wget http://archive.apache.org/dist/mesos/1.4.1/mesos-1.4.1.tar.gz
# Change working directory.
$ cd mesos

安装在  /usr/local/mesos下
# Configure and build.
$ mkdir build
$ cd build
$ ../configure --prefix=/usr/local/mesos
$ make 
$ make check
$ make install 



Install Marathon
Through your Package Manager
Marathon packages are available from Mesosphere’s repositories.

From a Tarball
Download and unpack the latest Marathon release.

$ curl -O http://downloads.mesosphere.com/marathon/v1.5.1/marathon-1.5.1.tgz
$ tar xzf marathon-1.5.1.tgz
SHA-256 checksums are available by appending .sha256 to the URLs.

Upgrading to a Newer Version
See the Marathon upgrade guide to learn how to upgrade to a new version.

Running in High Availability Mode
Both ZooKeeper and Mesos need to be running in order to launch Marathon in high availability mode.

Point your web browser to localhost:8080 and you should see the Marathon UI.

$ ./bin/start --master zk://zk1.foo.bar:2181,zk2.foo.bar:2181/mesos --zk zk://zk1.foo.bar:2181,zk2.foo.bar:2181/marathon
Marathon uses --master to find the Mesos masters, and --zk to find ZooKeepers for storing state. They are separate options because Mesos masters can also be discovered in other ways.

For all configuration options, see the command line flags doc. For more information on the high-availability feature of Marathon, see the high availability doc.

Mesos Library
MESOS_NATIVE_JAVA_LIBRARY: bin/start searches the common installation paths, /usr/lib and /usr/local/lib, for the Mesos native library. If the library lives elsewhere in your configuration, set the environment variable MESOS_NATIVE_JAVA_LIBRARY to its full path.

For example:

$ MESOS_NATIVE_JAVA_LIBRARY=/Users/bob/libmesos.dylib ./bin/start --master local --zk zk://localhost:2181/marathon