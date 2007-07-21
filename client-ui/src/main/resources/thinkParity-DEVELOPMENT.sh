#!/bin/bash

TPS_DIRECTORY=$PWD
TPS_EXECUTABLE=`basename $0`

jre1.6.0_01/bin/java -Dthinkparity-directory="$TPS_DIRECTORY" \
    -Dthinkparity-executable="$TPS_EXECUTABLE" \
    -Dthinkparity.environment=DEVELOPMENT \
    -Dthinkparity.mode=DEVELOPMENT -jar thinkParity.jar
