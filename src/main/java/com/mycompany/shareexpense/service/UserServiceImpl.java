package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.controller.UserController;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.repository.UserCustomRepository;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.util.CommonUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    private final Log log = LogFactory.getLog(UserController.class);
    
    @Autowired
    public UserRepository userRepository;
    
    @Autowired
    public UserCustomRepository userCustomRepository;
    
    @Override
    public User createUser(User user) throws Exception {
        
        log.debug(user.getEmail());
        User userExists = userRepository.findByEmail(user.getEmail());
        User userResponse = null;
        if (userExists != null) {
            
            if (CommonUtil.checkStringNullBlank(userExists.getPassword())) {
                List<User> users = userRepository.findByFriends(userExists.getId());
                
                List<String> friends = null;
                if (userExists.getFriends() != null) {
                    friends = userExists.getFriends();
                } else {
                    friends = new ArrayList<>();
                }
                
                for (User user1 : users) {
                    
                    friends.add(user1.getId());
                }
                
                userExists.setFriends(friends);
                userExists.setPassword(user.getPassword());
                
                userResponse = userRepository.save(userExists);
            } else {
                throw new Exception("User already register, please login to continue");
            }
            
        } else {
            userResponse = userRepository.save(user);
        }
        
        return userResponse;
    }
    
    @Override
    public User showUser(String Id) throws Exception {
        return userRepository.findOne(Id);
        
    }
    
    @Override
    public User updateUser(User user) throws Exception {
        return userRepository.save(user);
    }
    
    @Override
    public Iterable<User> findAll() throws Exception {
        return userRepository.findAll();
    }
    
    @Override
    public User findByEmailAndPassword(String email, String password) throws Exception {
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    @Override
    public User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public User createFriend(User user, String Id) throws Exception {
        
        User userExists = userRepository.findByEmail(user.getEmail());
        if (userExists != null) {
            
        } else {
            userExists = userRepository.save(user);
        }
        
        User user1 = userRepository.findOne(Id);
        
        if (user1.getFriends() != null) {
            user1.getFriends().add(userExists.getId());
        } else {
            List<String> friends = new ArrayList<>();
            friends.add(userExists.getId());
            user1.setFriends(friends);
        }
        userRepository.save(user1);
        
        return userExists;
    }
    
    @Override
    public void deleteFriend(String Id) throws Exception {
        userRepository.delete(Id);
    }
    
    @Override
    public List<User> findByFriend(String friendId) throws Exception {
        
        User user = userRepository.findOne(friendId);
        List<User> userList = null;
        if (user.getFriends() != null) {
            Iterable<User> users = userRepository.findAll(user.getFriends());
            userList = CommonUtil.toList(users);
            
        } else {
            userList = new ArrayList<>();
        }
        return userList;
    }
    
}
