package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.User;
import java.util.List;

public interface UserService {

    /*Login Services */
    public User findByEmailAndPassword(String email, String password) throws Exception;

    public boolean forgotPassword(String email) throws Exception;

    
    /* User Services */
    public User createAccount(User user) throws Exception;
    
    public User showAccount(String Id) throws Exception;

    public User updateAccount(User user) throws Exception;

    /* Friend Services */
    public User createFriend(User user, String Id) throws Exception;
    
    public User updateFriend(User user) throws Exception;

    public List<User> findByFriend(String userId) throws Exception;

    public void deleteFriend(String userId, String Id) throws Exception;

     
}
