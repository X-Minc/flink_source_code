#!/bin/bash 

current_path=`pwd`
case "`uname`" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname $0))
		;;
	*)
		bin_abs_path=`cd $(dirname $0); pwd`
		;;
esac
base=${bin_abs_path}/..
client_mode="Simple"
logback_configurationFile=$base/conf/logback.xml
export LANG=en_US.UTF-8
export BASE=$base

if [ -f $base/bin/canal.pid ] ; then
	echo "found canal.pid , Please run stop.sh first ,then startup.sh" 2>&2
    exit 1
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

ALIBABA_JAVA="/usr/alibaba/java/bin/java"
TAOBAO_JAVA="/opt/taobao/java/bin/java"
if [ -z "$JAVA" ]; then
  if [ -f $ALIBABA_JAVA ] ; then
  	JAVA=$ALIBABA_JAVA
  elif [ -f $TAOBAO_JAVA ] ; then
  	JAVA=$TAOBAO_JAVA
  else
  	echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=1.5) in your PATH." 2>&2
    exit 1
  fi
fi

case "$#" 
in
0 ) 
	;;
1 )	
	client_mode=$*
	;;
2 )	
	if [ "$1" == "debug" ]; then
		DEBUG_PORT=$2
		DEBUG_SUSPEND="y"
		JAVA_DEBUG_OPT="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=$DEBUG_SUSPEND"
	else 
		client_mode=$1
 	fi;;
* )
	echo "THE PARAMETERS MUST BE TWO OR LESS.PLEASE CHECK AGAIN."
	exit;;
esac

str=`file $JAVA_HOME/bin/java | grep 64-bit`
if [ -n "$str" ]; then
	JAVA_OPTS="-server -Xms2048m -Xmx3072m -Xmn1024m -XX:SurvivorRatio=2 -XX:PermSize=96m -XX:MaxPermSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError"
else
	JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxPermSize=128m "
fi
if [ "$YAML_ENV" == "" ]; then
    YAML_ENV="-Drap.log4j.debug=info -Drap.config.datasource=false -Drap.profile=production -Drap.crypto.key=IzxxW5L7UAdFl1huMCrg2TKs6+B/WeTCFCY+h2M2n5c -Ddsb.on=true -Drap.redis.server=192.168.19.4:6379 -Drap.redis.auth.password=CQC7RxN3ND0zhjwMUBXvtwvNSiQFgnq1 -Drap.mvc.config=false -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 -Drap.es.url=es-cn-v0h0m1w6e0003cjlw.elasticsearch.aliyuncs.com:9200 -Drap.es.username=elastic -Drap.es.password=JB%3nozMCSAdgC3h -Drap.mq.rocket.username=duLCaybnI9oOyOdZ -Drap.mq.rocket.password=RCHE8NY4DkIldIei3O4ub61FwxEaGa -Drap.mq.rocket.topic=topic_bot_data_sync_online -Drap.mq.rocket.namesrvAddr=http://onsaddr.cn-shanghai.mq-internal.aliyuncs.com:8080 -Drap.mq.rocket.groupId=CID_rap_bot_es_online -Drap.mq.rocket.topic.Two=rap_bot_kbs -Drap.mq.rocket.namesrvAddr.Two=http://MQ_INST_1122260953808354_BbzN8VSk.cn-shanghai.mq-internal.aliyuncs.com:8080 -Drap.mq.rocket.groupId=GID_rap_bot_kbs"
fi
JAVA_OPTS="$JAVA_OPTS $YAML_ENV"
CANAL_OPTS="-DappName=otter-canal-example -Dlogback.configurationFile=$logback_configurationFile"

if [ -e $logback_configurationFile ]
then 
	
	for i in $base/lib/*;
		do CLASSPATH=$i:"$CLASSPATH";
	done
 	CLASSPATH="$base/conf:$CLASSPATH";
 	
 	echo "cd to $bin_abs_path for workaround relative path"
  	cd $bin_abs_path
 	
	echo LOG CONFIGURATION : $logback_configurationFile
	echo client mode : $client_mode 
	echo CLASSPATH :$CLASSPATH
	if [ $client_mode == "Cluster" ] ; then 
		$JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $CANAL_OPTS -classpath .:$CLASSPATH com.ifugle.rap.canal.common.StartUp &
	else 
		$JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $CANAL_OPTS -classpath .:$CLASSPATH com.ifugle.rap.canal.common.StartUp &
	fi
	
	echo $! > $base/bin/canal.pid 
	echo "cd to $current_path for continue"
  	cd $current_path
else 
	echo "client mode("$client_mode") OR log configration file($logback_configurationFile) is not exist,please create then first!"
fi
