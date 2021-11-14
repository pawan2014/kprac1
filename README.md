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
http://localhost:9090/send?msg=test5;POC1;Message2

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