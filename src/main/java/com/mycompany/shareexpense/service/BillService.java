package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.UserDto;
import com.mycompany.shareexpense.model.UsersBalance;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author AH0661755
 */
public interface BillService {

    public Bill saveBill(Bill bill) throws Exception;

    public void deleteBill(String Id) throws Exception;

    public Bill showBill(String Id) throws Exception;

    public Bill updateBill(Bill bill) throws Exception;

    public List<UsersBalance> usersBillDetails(String userId) throws Exception;

    public void userSettlement(UserDto userDto) throws Exception;

    public void userPayReminder(UserDto userDto) throws Exception;

    public List<Bill> recentTrans(String userId) throws Exception;

    public Page<Bill> recentPageTrans(String userId, int page, int pagesize) throws Exception;

    public List<Bill> recentUserTrans(String userId, String loggedUser) throws Exception;

    public List<Bill> recentGroupTrans(String groupId) throws Exception;

    public Bill addBill(String userId) throws Exception;

    public Bill addGroupBill(String groupId) throws Exception;

    public List<Bill> findAllBills() throws Exception;

}
