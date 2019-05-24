用于参考的项目编写demo\
https://github.com/zhaoshuxue/springBoot/tree/master/spring-boot-hibernate-v2

Springboot的使用方式\
https://github.com/zhaoshuxue/springBoot

- 使用spring boot+GridFS作为文件服务器
- file-server 用于生成PPT和PDF等文件
- zimg 为图片服务器，用于图片存储

#发布流程

提交到master 分支\
切换到release 分支\
将master的代码合并到release分支\
推入远端git，等待部署\
consul 命令
```jshelllanguage
consul agent -dev  -bind 0.0.0.0 -advertise="192.168.31.42" -client 0.0.0.0 -ui
```
springboot集成mongodb，实现文件上传下载
https://blog.csdn.net/xiaoxiangzi520/article/details/80595287

docker+consul搭建集群
https://blog.csdn.net/qq_22152261/article/details/76099579?utm_source=blogxgwz1