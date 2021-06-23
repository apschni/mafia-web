#!/bin/bash

#reload and start backend
#> nohup.out
#cd ..
#cd ITMO-mafia-web/
#kill -9 $(pgrep -f java)
#mvn package
#+
#tail -f nohup.out

#start and reload frontend
cd mafia-frontend/
pm2 list
pm2 stop npm
git pull
pm2 start npm
