#!/bin/bash

cd bin
rmiregistry &
java rmiserver.WhiteBoardService
