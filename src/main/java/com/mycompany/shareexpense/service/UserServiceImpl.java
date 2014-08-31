package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.repository.BillRepository;
import com.mycompany.shareexpense.repository.UserCustomRepository;
import com.mycompany.shareexpense.repository.UserRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;
    
     
    @Autowired
    public UserCustomRepository userCustomRepository;
    
    
    @Override
    public User saveUser(User user) throws Exception {
        return userRepository.save(user);
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
    public void deleteFriend(String Id) throws Exception {
        userRepository.delete(Id);
    }

    @Override
    public List<User> findByFriend(String friendId) throws Exception {
        return userCustomRepository.findByFriend(friendId);
    }

    @Override
    public List<User> findByGroup(String groupId) throws Exception {
        return null; //userRepository.findByGroup(groupId);
    }


}
