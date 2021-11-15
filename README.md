# kprac1
Kafka prac

Branches
- EX1 simple code to produce a message and consume a message
- EX2	
- EX3
- EX4
- EX5	make separate projects for core-consumer, producer and monitorservice 

REST API Calls

```
http://localhost:9091/monitor/service?group=1&system=SC1&topic=testtopic&partition=0&start=1&end=0

where test5 is group and POC1 is the system name
```

To see the MetaInfo

```
http://localhost:9090/monitor/
```
## EX5
3 separate projects. 
### Monitoring service 
Will get the offset data and will also be called by core consumer for updating the current offset processed
Expose a API to be called by core consumer to update the last offset

```
bin/kafka-topics.sh --create --topic MY-TEST-TOPIC_1 --bootstrap-server localhost:9092 --replication-factor 1 --partitions 5

```
## ES6
```
Update current offset
http://localhost:9091/monitor/currentOffset/update?group=FORNOW-HARDCODED&topic=MY-TEST-TOPIC_1&partition=0&offset=1

```