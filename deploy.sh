#!/usr/bin/env bash

ROOT_PATH="bin"
cd ${ROOT_PATH}
for SERVER_PATH in `ls ../${ROOT_PATH}`;do
    if [ ${SERVER_PATH} != 'run.sh' ]; then
        API_NAME=`awk 'NR==1 {print $1}' ${SERVER_PATH}/config`
        API_PORT=`awk 'NR==2 {print $1}' ${SERVER_PATH}/config`
        API_VERSION=`awk 'NR==3 {print $1}' ${SERVER_PATH}/config`
        bash run.sh ${SERVER_PATH} ${API_NAME} ${API_PORT} ${API_VERSION}
    fi
done