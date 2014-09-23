package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.ShareGroup;
import com.mycompany.shareexpense.repository.GroupRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {
    
	private final Logger	log	= Logger.getLogger (GroupServiceImpl.class);
    DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
    
    @Autowired
    public GroupRepository groupRepository;

    @Override
    public ShareGroup createGroup(ShareGroup shareGroup) throws Exception {
        shareGroup.setCreateDate (dateFormat.format (new Date()));
        return groupRepository.save(shareGroup);
    }

    @Override
    public ShareGroup showGroup(String Id) throws Exception {
        return groupRepository.findOne(Id);
    }

    @Override
    public ShareGroup updateGroup(ShareGroup shareGroup) throws Exception {
        shareGroup.setModifiedDate (dateFormat.format (new Date()));
        return groupRepository.save(shareGroup);
    }

    @Override
    public List<ShareGroup> listGroups(String userId) throws Exception {
        return groupRepository.findByUserIdOrUserIds (userId, userId);
    }
    
    
}
