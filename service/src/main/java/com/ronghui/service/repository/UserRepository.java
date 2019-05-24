package com.ronghui.service.repository;

import com.ronghui.service.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query(value = "from User u where u.uid=:uid")
    public User findUser(@Param("uid") long uid);

    public User findByPhone(String phone);

    public User findByEmail(String email);

    public User findByOpenid(String openid);

    public User findByTokenAndPhone(String phone, String token);
    
    @Query(value = "select count(*) from User u where u.phone=:phone and u.password=:password")
    public int matchPasswordByPhone(@Param("phone") String phone, @Param("password") String password);
    
    @Modifying
    @Query(value = "update User u set u.birthday=:#{#user.birthday}, u.company=:#{#user.company}, u.email=:#{#user.email}, "
    		+ "u.head=:#{#user.head}, u.industry=:#{#user.industry}, u.level=:#{#user.level}, u.nickname=:#{#user.nickname}, "
    		+ "u.phone=:#{#user.phone}, u.qq=:#{#user.qq}, u.sex=:#{#user.sex}, u.title=:#{#user.title}, u.token=:#{#user.token} "
    		+ "where u.uid=:#{#user.uid}")
    public void updateWithoutPassword(@Param("user") User user);
    
    @Modifying
    @Query(value = "update User u set u.openid=:#{#user.openid} where u.uid=:#{#user.uid}")
    public void updateOpenId(User user);

    @Modifying
    @Query(value = "update User u set u.openid= ?2 where u.uid= ?1")
    public void updateOpenId(long uid, String openid);
}
