#!/bin/bash

> nohup.out
kill -9 $(pgrep -f java)
git pull
mvn package
echo "💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩💩"
nohup java -jar target/mafia-backend-0.0.1-SNAPSHOT.jar &
tail -f nohup.out

