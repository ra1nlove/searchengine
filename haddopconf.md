# Hadoop、Hbase　分布式部署

## 基础配置

### 创建Hadoop用户

	//创建用户
	1 sudo useradd -m hadoop -s /bin/bash
	//设置密码
	2 sudo passwd hadoop
	//添加权限
	3 sudo adduser hadoop sudo

### 安装SSH
安装SSH以实现无密码登录，Ubuntu已经默认安装了`SSH client`。
		
	sudo apt-get install openssh-server
	ssh localhost

在步骤2后，将会建立`~/.ssh`文件。
master：
		
	cd ~/.ssh
	ssh-keygen -t rsa //三次回车
	cat id_rsa.pub >> authorized_keys
　　
slave :  

	cd ~/.ssh
	ssh-keygen -t rsa //三次回车
	scp id_rsa.pub master@10.108.113.214:/home/master/.ssh/id_rsa.pub.s1

master:

	cat id_rsa.pub.s1 >> authorized_keys
	scp authorized_keys slave@10.108.112.176:/home/slave/.ssh/

end!

### 安装Java

下载JDK到`/home/hadoop/java`
		
	vim ~/.bashrc
添加
		
	export JAVA_HOME=/home/hadoop/java
	export JRE_HOME=${JAVA_HOME}/jre
	export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
	export PATH=${JAVA_HOME}/bin:$PATH
保存
		
	source ~/.bashrc
运行
	
	hadoop@hadoop-master:~/hbase$ java -version
	java version "1.8.0_111"
	Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
	Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)

### 配置/etc/hosts
	
	10.108.114.127 hadoop-master
	10.108.113.135 proxy2
	10.108.119.15  ubuntu

## Hadoop 安装
下载Hadoop到`/home/hadoop/hadoop`
添加环境变量
	
	vim ~/.bashrc
添加一下内容
		
	export HADOOP_HOME=/home/hadoop/hadoop
	export PATH=$PATH:$HADOOP_HOME/bin
	export PATH=$PATH:$HADOOP_HOME/sbin
保存
		
	source ~/.bashrc

配置`etc/hadoop/core-site.xml`
```xml
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://hadoop-master:9000</value>
</property>
    
<property>
    <name>hadoop.tmp.dir</name>
    <value>/usr/local/data/hadoop/tmp</value>
</property>
```
配置`etc/hadoop/hdfs-site.xml`
```xml
<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>

<property>  
   <name>dfs.namenode.name.dir</name>  
   <value>/usr/local/data/hadoop/namenode</value>  
</property>  
    
<property>  
   <name>dfs.datanode.data.dir</name>  
   <value>/usr/local/data/hadoop/datanode</value>  
</property>
```
配置`etc/hadoop/mapred-site.xml`
```xml
<property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
</property>
```
配置`etc/hadoop/yarn-site.xml`
```xml
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>

<property>    
    <name>yarn.log-aggregation-enable</name>    
    <value>true</value>    
</property> 

<property>
    <name>yarn.resourcemanager.hostname</name>
    <value>hadoop-master</value>
</property>
```
配置`slaves`
	
	hadoop-master
	proxy2
	ubuntu

启动`hadoop`

	start-all.sh

master显示
	
	19410 DataNode
	19748 ResourceManager
	19865 NodeManager
	19278 NameNode
	19582 SecondaryNameNode

slave显示

	15507 DataNode
	16073 NodeManager
	
##HBase配置

下载HBase到`/home/hadoop/hbase`
添加环境变量
```bash
export HBASE_HOME=/home/hadoop/hbase
export PATH=$PATH:$HBASE_HOME/bin
export PATH=$PATH:$HBASE_HOME/sbin
```

配置`conf/hbase-site.xml`
```xml
<property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
</property>

<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>

<property>
   <name>hbase.master</name>
    <value>hdfs://hadoop-master:60000</value>
</property>

<property>
    <name>hbase.rootdir</name>
    <value>hdfs://hadoop-master:9000/hbase</value>
</property>

<property>
    <name>hbase.zookeeper.quorum</name>
    <value>hadoop-master,proxy2,ubuntu</value>
</property>
```

配置`regionservers`
	
	hadoop-master
	proxy2
	ubuntu

启动HBase

	start-hbase.sh

`master`
	
	19410 DataNode
	22707 HMaster
	19748 ResourceManager
	19865 NodeManager
	22843 HRegionServer
	19278 NameNode
	19582 SecondaryNameNode
	22591 HQuorumPeer

`slaves`
	
	15507 DataNode
	31204 HQuorumPeer
	31385 HRegionServer
	16073 NodeManager
