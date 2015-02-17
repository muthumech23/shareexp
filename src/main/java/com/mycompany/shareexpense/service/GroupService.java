package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.ShareGroup;

import java.util.List;

public interface GroupService {

    public ShareGroup createGroup(ShareGroup shareGroup) throws Exception;

    public ShareGroup showGroup(String Id) throws Exception;

    public ShareGroup updateGroup(ShareGroup shareGroup) throws Exception;

    public List<ShareGroup> listGroups(String userId) throws Exception;

    public List<ShareGroup> findAllGroups() throws Exception;

}
