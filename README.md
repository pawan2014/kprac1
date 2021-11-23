# kprac1
## About
This is all simple stuff to learn about Kafka and test approaches with offset working.

Follwing are the git branches with incremental code
- EX1 simple code to produce a message and consume a message
- EX2	
- EX3
- EX4
- EX5	make separate projects for core-consumer, producer and monitorservice 
- EX6	logic to get the current offset from the remote server, derive tru flag
- EX6 Make the EOD for system based on the combined flag of all paration  

## Project Starup  Sequence
Start with core consumer, monitoring service and then producer given all read from latest offset

## REST API Calls

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
## EX6

## EX7
```
http://localhost:9091/monitor/updateeod?group=FORNOW-HARDCODED&topic=MY_BL&eod=true
```


## Common Problems
- Error "Topic MY-TEST-TOPIC_1 not present in metadata after 60000 ms." Comes when you are tring to send data to partition which does't exists for that topic. like sending to 3 partition but in actual there is only one parition. This issue i am seeing next day when i try to run the producer. Need to check more


## Command
```
bin/kafka-topics.sh --describe --topic MY-TEST-TOPIC_1 --bootstrap-server localhost:9092
Topic: MY-TEST-TOPIC_1	TopicId: 0IwbTd-0Spajz_RZ78ixaw	PartitionCount: 4	ReplicationFactor: 1	Configs: segment.bytes=1073741824
	Topic: MY-TEST-TOPIC_1	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: MY-TEST-TOPIC_1	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: MY-TEST-TOPIC_1	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
	Topic: MY-TEST-TOPIC_1	Partition: 3	Leader: 0	Replicas: 0	Isr: 0
```
## Question
what happens when core consumer is fast than monitoring service. it will mark true but in actully data might be still flowing