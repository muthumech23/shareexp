package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserSecure;
import com.mycompany.shareexpense.util.CustomException;

import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface UserService {
    /*Login Services */

    /**
     * DOCUMENT ME!
     *
     * @param email    DOCUMENT ME!
     * @param password DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User authenticateLogin(String email, String password) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param email DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public boolean forgotPassword(String email) throws CustomException;

    /**
     * DOCUMENT ME!
     *
     * @param email DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public boolean regenerateActivation(String email) throws CustomException;


    /* User Services */

    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User createAccount(User user) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param Id DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User showAccount(String Id) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User updateAccount(User user) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User activateAccount(UserSecure user) throws CustomException;


    /* Friend Services */

    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @param Id   DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User createFriend(User user, String Id) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param user DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public User updateFriend(User user) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param userId DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public List<User> findByFriend(String userId) throws CustomException;


    /**
     * DOCUMENT ME!
     *
     * @param userId DOCUMENT ME!
     * @param Id     DOCUMENT ME!
     * @throws CustomException DOCUMENT ME!
     */
    public void deleteFriend(String userId, String Id) throws CustomException;

    public List<User> findAllUsers() throws Exception;

    public List<UserSecure> findAllUserSecures() throws Exception;
}
