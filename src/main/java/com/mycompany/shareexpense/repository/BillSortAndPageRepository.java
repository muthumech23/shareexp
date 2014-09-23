
package com.mycompany.shareexpense.repository;

import com.mycompany.shareexpense.model.Bill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author AH0661755
 */
@Repository
public interface BillSortAndPageRepository extends PagingAndSortingRepository<Bill, String> {

	public Page<Bill> findByUserPaidOrBillSplitsUserId(	String Id,
														String userId, Pageable pageable);

	public Page<Bill> findByUserPaidOrUserPaid(	String userId,
												String loggedUser, Pageable pageable);

	public Page<Bill> findByGroupId(String groupId, Pageable pageable);



}
