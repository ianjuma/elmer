## Elmer


### About

```elmer``` is a fraud detection service; it receives a payment transaction and determines using 
ML whether the transaction is genuine or not.


### Depends

```bash
install redis
install mariadb
```

### run

```bash
# update application.conf file

/usr/bin/jsvc -home ${JAVA_HOME} -pidfile ${PID} \
     -outfile ${OUT_LOG} -errfile ${ERR_LOG} -cp ${APP_CLASSPATH} ${APP_CLASS}
```

```bash

$ sbt
> compile
> run
```


```bash

# sbt - check spark libs -provided- when assembling

install spark-1.6.1 # prebuilt for hadoop 2.6
install hadoop-pre-built-bin-2.6.4

# exports

export LD_LIBRARY_PATH=/usr/lib/hadoop-2.6.4/lib/native/:$LD_LIBRARY_PATH
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native

export JAVA_HOME=/usr/lib/jdk/jdk1.8.0_91

export HADOOP_HOME=/usr/lib/hadoop-2.6.4

start-master.sh
start-slave.sh spark://synod:7077
spark-submit --master spark://synod:7077 --deploy-mode cluster app.jar
```
