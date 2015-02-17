package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.ShareGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface GroupRepository extends CrudRepository<ShareGroup, String> {

    public List<ShareGroup> findByUserIdOrUserIds(String Id, String userId);

    public List<ShareGroup> findAll();

}
