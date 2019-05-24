#!/usr/bin/env bash

# ./run <相对路径> <名称> <端口号> <版本>
#定义变量
API_NAME=$2
API_VERSION=$4
API_PORT=$3
IMAGE_NAME="$API_NAME:$API_VERSION"
echo "构建${API_NAME}"

#进入target目录并复制Dockerfile文件
#删除docker容器
cid=$(sudo docker ps -a | grep "${API_NAME}" | awk '{print $1}')
if [ "$cid" != "" ]; then
    echo "停止docker镜像"
	sudo docker stop $cid
	sudo docker rm -f $cid
fi

#删除旧镜像
mid=$(sudo docker images | grep "${API_NAME}" | awk '{print $3}')
if [ "$mid" != "" ]; then
	sudo docker rmi  $mid
fi

#构建Docker镜像
sudo docker build -t $IMAGE_NAME ./$1/.
#启动dcoker容器
sudo docker run -d \
    -p $API_PORT:$API_PORT \
    --hostname ${API_NAME} \
    --name $API_NAME  $IMAGE_NAME

