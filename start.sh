#!/bin/bash

> nohup.out
killall java
git pull
mvn package
nohup java -jar target/mafia-backend-0.0.1-SNAPSHOT.jar &
tail -f nohup.out
