package com.ronghui.service.service;

import java.io.InputStream;

public interface MongoDBService {
    public String save(InputStream in, Object id, String fileName);

    public byte[] getById(String id);

    public byte[] getByFileName(String fileName);
}
