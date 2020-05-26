#!/bin/bash

javac -d bin -cp src src/*/*.java

cd bin
rmiregistry &
registryID=$!
sleep 1
java rmiserver.WhiteBoardService

kill -9 $registryID
