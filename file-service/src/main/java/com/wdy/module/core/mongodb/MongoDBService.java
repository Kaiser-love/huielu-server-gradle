package com.wdy.module.core.mongodb;

import java.io.InputStream;

public interface MongoDBService {
    String save(InputStream in, Object id, String fileName);

    byte[] getById(String id);

    byte[] getByFileName(String fileName);
}
