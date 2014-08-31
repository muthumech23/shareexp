package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.User;
import java.util.List;

public interface UserService {

    public User saveUser(User user) throws Exception;

    public User showUser(String Id) throws Exception;

    public User updateUser(User user) throws Exception;

    public User findByEmailAndPassword(String email, String password) throws Exception;

    public Iterable<User> findAll() throws Exception;

    public void deleteFriend(String Id) throws Exception;

     public List<User> findByGroup(String groupId) throws Exception;

    public List<User> findByFriend(String friendId) throws Exception;
    
}
