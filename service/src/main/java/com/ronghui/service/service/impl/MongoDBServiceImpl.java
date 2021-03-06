package com.ronghui.service.service.impl;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoGridFSException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import com.ronghui.service.service.MongoDBService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import static java.util.Arrays.asList;

@Service
@Slf4j
public class MongoDBServiceImpl implements MongoDBService {

    @Value("${spring.data.mongodb.host}")
    String mongoHost = "127.0.0.1";


    @Value("${spring.data.mongodb.port}")
    String mongoPort = "27017";

    @Value("${spring.data.mongodb.username}")
    String mongoUsername = "root";

    @Value("${spring.data.mongodb.password}")
    String mongoPassword = "123";

    @Value("${spring.data.mongodb.authentication-database}")
    String mongoAuthenticationDatabase = "admin";


    @Value("${spring.data.mongodb.uri}")
    String mongoUri = "mongodb://root:123@47.112.204.145:27017/admin";

    private static final String dbName = "gridfs";


    private static MongoInstance mongoInstance;

    private static final class MongoInstance {
        MongoClient client;
        // new ConnectionString(String.format("mongodb://%s:%s", mongoHost, mongoPort))
        // new ConnectionString(String.format("mongodb://%s:%s@%s:%s/%s", mongoHost, mongoPort))
        MongoInstance(String mongoHost, String mongoPort,String mongoUri) {
            MongoClientSettings settings = MongoClientSettings
                    .builder()
                    .applyConnectionString(new ConnectionString(mongoUri))
                    .applyToConnectionPoolSettings(builder -> builder.maxSize(25)
                            .minSize(10)
                            .maxWaitTime(25, TimeUnit.SECONDS)
                            .maxConnectionIdleTime(10000, TimeUnit.SECONDS)
                            .maxConnectionLifeTime(10000, TimeUnit.SECONDS)
                            .maintenanceInitialDelay(0, TimeUnit.SECONDS)
                            .build())
                    .build();
            client = MongoClients.create(settings);
        }
    }

    private MongoDatabase getDatabase() {
        if (mongoInstance == null) {
            synchronized (MongoInstance.class) {
                if (mongoInstance == null) {
                    mongoInstance = new MongoInstance(mongoHost, mongoPort,mongoUri);
                }
            }
        }
        return mongoInstance.client.getDatabase(dbName);
    }


    /**
     * 用给出的id，保存文件，透明处理已存在的情况
     * id 可以是string，long，int，org.bson.types.ObjectId 类型
     *
     * @param in
     * @param id
     */
    public String save(InputStream in, Object id, String fileName) {
        GridFSBucket bucket = GridFSBuckets.create(getDatabase());
        GridFSUploadOptions options = new GridFSUploadOptions();
        ObjectId objectId = bucket.uploadFromStream(fileName, in, options);
        log.info("save：{}\nid:{} ", fileName, objectId.toHexString());
        return objectId.toHexString();
    }

    /**
     * 据id返回文件
     *
     * @param id
     * @return
     */
    public byte[] getById(String id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GridFSBucket bucket = GridFSBuckets.create(getDatabase());
        byte[] data = null;
        try {
            bucket.downloadToStream(new ObjectId(id), outputStream);
            data = outputStream.toByteArray();
        } catch (MongoGridFSException e) {
            log.error("getByFileName", e);

        } finally {
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
        return data;
    }

    /**
     * 据文件名返回文件，只返回第一个
     *
     * @param fileName
     * @return
     */
    public byte[] getByFileName(String fileName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MongoDatabase mongoDatabase = getDatabase();
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase);
        byte[] data = null;
        try {
            bucket.downloadToStream(fileName, outputStream);
            data = outputStream.toByteArray();

        } catch (MongoGridFSException e) {
            log.error("getByFileName", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ignored) {
            }
        }
        return data;
    }
}
