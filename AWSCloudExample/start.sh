#!/bin/bash
yum update -y
cd ~/tmp/WhiteBoard-Implementation-Examples/RPCExample
javac -d bin -cp src src/rpc/*.java
cd bin
java rpc.Server
