package com.ronghui.service.service.impl;

import com.ronghui.service.repository.UserRepository;
import com.ronghui.service.entity.User;
import com.ronghui.service.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;


@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    	// TODO: Delete ?
    @Override
    public void cleanAuthorEntity(User user) {
        if (user != null) {
            user.setDirectorys(null);
            user.setPpts(null);
        }
    }

    @Override
    public boolean checkPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return user != null;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(User user) {
        userRepository.updateWithoutPassword(user);
    }

    @Transactional
    @Override
    public void updateOpenId(User user) {
        userRepository.updateOpenId(user);
    }

    @Transactional
    @Override
    public void updateOpenId(long uid, String openid) {
        userRepository.updateOpenId(uid, openid);
    }
    
    @Override
    public void delete(long uid) {
        userRepository.deleteById(uid);
    }

    @Override
    public User findById(long uid) {
    		User user = userRepository.findUser(uid);
    		if (user != null)
    			user.setPassword("");
        return user;
    }

    @Override
    public User findByPhone(String phone) {
    		User user = userRepository.findByPhone(phone);
    		if (user != null)
    			user.setPassword("");
        return user;
    }

    @Override
    public User findByOpenid(String openid) {
    		User user = userRepository.findByOpenid(openid);
    		if (user != null)
    			user.setPassword("");
        return user;
    }

    @Override
    public User findByTokenAndPhone(String phone, String token) {
    		User user = userRepository.findByTokenAndPhone(phone, token);
    		if (user != null)
    			user.setPassword("");
        return user;
    }
    
    @Override
    public List<User> getUserList() {
        Iterable<User> userIters = userRepository.findAll();
        List<User> userList = new ArrayList<User>();
        for (User u : userIters)
            userList.add(u);
        return userList;
    }

    @Override
    public boolean matchPasswordByPhone(String phone, String password) {
    		int ct = userRepository.matchPasswordByPhone(phone, password);
    		return ct > 0;
    }
}
