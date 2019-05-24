package com.ronghui.service.service;

import com.ronghui.service.entity.User;
import org.springframework.data.annotation.Transient;

import java.util.List;

public interface UserService {

    public abstract boolean checkPhone(String phone);

    public abstract void save(User user);

    public abstract void update(User user);
    
    public abstract void updateOpenId(User user);

    public abstract void updateOpenId(long uid, String openid);

    public abstract void delete(long uid);

    public abstract User findById(long uid);

    public abstract User findByPhone(String phone);

    public abstract User findByOpenid(String openid);

    public abstract User findByTokenAndPhone(String phone, String token);

    public abstract List<User> getUserList();

    public abstract boolean matchPasswordByPhone(String phone, String password);
    
    // TODO: Delete ?
    public void cleanAuthorEntity(User user);

}
