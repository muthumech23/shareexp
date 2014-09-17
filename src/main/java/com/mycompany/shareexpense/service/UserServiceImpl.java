package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.controller.UserController;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.repository.UserSecureRepository;
import com.mycompany.shareexpense.util.CommonUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Log log = LogFactory.getLog (UserController.class);

    DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserSecureRepository userSecureRepository;

    @Override
    public User findByEmailAndPassword (String email,
                                        String password) throws Exception {

        User user = userRepository.findByEmail (email);

        if (user == null) {
            return null;
        }

        log.info ("ID ---> " + user.getId ());

        UserSecure userSecure = userSecureRepository.findByUserIdAndPassword (user.getId (), password);

        if (userSecure == null) {
            return null;
        }
        log.info ("Secure ID ---> " + userSecure.getUserId ());
        return user;
    }

    @Override
    public boolean forgotPassword (String email) throws Exception {
        boolean status = false;
        User user = userRepository.findByEmail (email);
        if (user == null) {
            return false;
        } else {
            UserSecure userSecure = userSecureRepository.findByUserId (user.getId ());

            CommonUtil.sendEmail ("Password reset", "muthumech23@gmail.com", "Password Body");
            status = true;
        }

        return status;
    }

    @Override
    public User createAccount (User user) throws Exception {

        User userResponse = null;
        List<String> friends = null;
        List<User> users = null;
        UserSecure userSecure = null;

        Date sysDate = new Date ();

        log.debug (user.getEmail ());
        User userExists = userRepository.findByEmail (user.getEmail ());

        if (userExists != null) {

            userSecure = userSecureRepository.findOne (userExists.getId ());

            if (userSecure == null) {

                /* users = userRepository.findByFriends (userExists.getId ());

                friends = userExists.getFriends () != null ? userExists.getFriends () : new ArrayList<String> ();

                for (User addUser : users) {

                    friends.add (addUser.getId ());
                }

                userExists.setFriends (friends);

                userExists.setCreateDate (dateFormat.format (sysDate));
                userExists.setModifiedDate (dateFormat.format (sysDate));
                userResponse = userRepository.save (userExists);
                 */

                userSecure = new UserSecure ();
                userSecure.setUserId (userExists.getId ());
                userSecure.setPassword (user.getPassword ());
                userSecure.setCreateDate (dateFormat.format (sysDate));
                userSecure.setModifiedDate (dateFormat.format (sysDate));
                userSecureRepository.save (userSecure);

            } else {
                throw new Exception ("User already register, please login to continue");
            }

        } else {
            userResponse = userRepository.save (user);

            userSecure = new UserSecure ();
            userSecure.setUserId (userResponse.getId ());
            userSecure.setPassword (user.getPassword ());
            userSecure.setCreateDate (dateFormat.format (sysDate));
            userSecure.setModifiedDate (dateFormat.format (sysDate));
            userSecureRepository.save (userSecure);

        }

        return userResponse;
    }

    @Override
    public User updateAccount (User user) throws Exception {

        Date sysDate = new Date ();

        UserSecure userSecure = new UserSecure ();
        userSecure.setUserId (user.getId ());
        userSecure.setPassword (user.getPassword ());

        userSecure.setModifiedDate (dateFormat.format (sysDate));
        userSecureRepository.save (userSecure);
        user.setPassword (null);
        return user;
    }

    @Override
    public User showAccount (String Id) throws Exception {
        return userRepository.findOne (Id);

    }

    @Override
    public User createFriend (User user,
                              String Id) throws Exception {

        Date sysDate = new Date ();
        List<String> friends = null;
        
        User friendExist = userRepository.findByEmail (user.getEmail ());
        if (friendExist == null) {
            user.setModifiedDate (dateFormat.format (sysDate));
            user.setCreateDate (dateFormat.format (sysDate));
            
            friends = user.getFriends () != null ? user.getFriends () : new ArrayList<String> ();
            friends.add (Id);
            
            friendExist = userRepository.save (user);
        }

        User loggedUser = userRepository.findOne (Id);

        if (loggedUser.getFriends () != null) {
            loggedUser.getFriends ().add (friendExist.getId ());
        } else {
            List<String> loggedFriends = new ArrayList<> ();
            loggedFriends.add (friendExist.getId ());
            loggedUser.setFriends (loggedFriends);
        }
        loggedUser.setModifiedDate (dateFormat.format (sysDate));
        userRepository.save (loggedUser);
        
        if (friendExist.getFriends () != null) {
            friendExist.getFriends ().add (Id);
        } else {
            List<String> friendsList = new ArrayList<> ();
            friendsList.add (Id);
            friendExist.setFriends (friendsList);
        }
        
        friendExist.setModifiedDate (dateFormat.format (sysDate));
        userRepository.save (friendExist);
        
        return friendExist;
    }

    @Override
    public User updateFriend (User user) throws Exception {
        User friendUpdated = userRepository.save (user);

        return friendUpdated;
    }

    @Override
    public List<User> findByFriend (String userId) throws Exception {
        List<User> friendList = null;
        User user = userRepository.findOne (userId);

        if (user.getFriends () != null) {
            Iterable<User> friends = userRepository.findAll (user.getFriends ());
            friendList = CommonUtil.toList (friends);

        } else {
            friendList = new ArrayList<> ();
        }
        
        friendList.add (user);
        return friendList;
    }

    @Override
    public void deleteFriend (String userId,
                              String Id) throws Exception {

        Date sysDate = new Date ();

        User user = userRepository.findOne (userId);
        List<String> friends = user.getFriends ();

        if (friends != null) {
            friends.remove (Id);

            user.setFriends (friends);

            user.setModifiedDate (dateFormat.format (sysDate));

            userRepository.save (user);
        }
    }

}
