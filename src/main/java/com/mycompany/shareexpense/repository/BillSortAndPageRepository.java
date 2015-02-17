package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author AH0661755
 */
@Repository
public interface BillSortAndPageRepository extends PagingAndSortingRepository<Bill, String> {

    public Page<Bill> findByUserPaidOrBillSplitsUserId(String Id, String userId, Pageable pageable);

    public List<Bill> findByUserPaidOrBillSplitsUserId(String Id, String userId, Sort sort);


    public Page<Bill> findByUserPaidOrUserPaid(String userId, String loggedUser, Pageable pageable);


    public Page<Bill> findByGroupId(String groupId, Pageable pageable);

    public List<Bill> findByGroupId(String groupId, Sort sort);


    public List<Bill> findByUserPaidAndBillSplitsUserId(String userId, String loggedUser, Sort sort);


    public List<Bill> findByUserPaid(String Id, Sort sort);

    public List<Bill> findByBillSplitsUserId(String userId, Sort sort);

    public List<Bill> findAll(Sort sort);

}
