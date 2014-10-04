
package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.repository.BillRepository;
import com.mycompany.shareexpense.repository.BillSortAndPageRepository;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.util.CommonUtil;
import com.mycompany.shareexpense.util.ErrorConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author AH0661755
 */
@Service
public class BillServiceImpl implements BillService {

	private final Logger log = Logger.getLogger(BillServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private BillRepository billRepository;


	@Autowired
	private BillSortAndPageRepository billSortAndPageRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Bill saveBill(Bill bill) throws Exception {

		Bill billResponse = billRepository.save(bill);

		if (billResponse != null) {


			String subject = null;
			String emailbody = null;
			String userPaid = null;

			for (BillSplit billSplit : bill.getBillSplits()) {

				if (billSplit.getAmount().compareTo(BigDecimal.ZERO) > 0) {

					userPaid = billSplit.getEmail();
					subject = env.getProperty("mail.template.bill.owner.subject");
					emailbody = env.getProperty("mail.template.bill.owner.body");
					emailbody = emailbody.replaceAll("<<billamount>>", bill.getAmount() + "");
					emailbody = emailbody.replaceAll("<<billdesc>>", bill.getDescription());
					emailbody = emailbody.replaceAll("<<username>>", bill.getBy());
					emailbody = emailbody.replaceAll("<<siteurl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home");
					break;
				}
			}
			try{
				CommonUtil.sendEmail(subject, userPaid, emailbody, env);
			}catch(Exception exception){
				log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
			}
			

			for (BillSplit billSplit : bill.getBillSplits()) {

				if (billSplit.getAmount().compareTo(BigDecimal.ZERO) < 0) {

					subject = env.getProperty("mail.template.bill.recipants.subject");
					subject = subject.replaceAll("<<userpaid>>", userPaid + "");

					emailbody = env.getProperty("mail.template.bill.recipants.body");
					emailbody = emailbody.replaceAll("<<billamount>>", bill.getAmount() + "");
					emailbody = emailbody.replaceAll("<<billdesc>>", bill.getDescription());
					emailbody = emailbody.replaceAll("<<toaddress>>", billSplit.getEmail());
					emailbody = emailbody.replaceAll("<<splitamount>>", billSplit.getAmount() + "");
					emailbody = emailbody.replaceAll("<<username>>", bill.getBy());
					emailbody = emailbody.replaceAll("<<useremail>>", userPaid);
					emailbody = emailbody.replaceAll("<<siteurl>>", "http://shareexpense-shareexp.rhcloud.com/shareexpense/#/home");

				}
				try{
					CommonUtil.sendEmail(subject, billSplit.getEmail(), emailbody, env);
				}catch(Exception exception){
					log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
				}
			}
		}

		return billResponse;
	}

	@Override
	public Bill updateBill(Bill bill) throws Exception {

		return billRepository.save(bill);
	}

	@Override
	public void deleteBill(String Id) throws Exception {

		billRepository.delete(Id);
	}

	@Override
	public List<BillSplit> usersBillDetails(String userId) throws Exception {

		User user = userRepository.findOne(userId);
		List<User> users = null;
		if (user.getFriends() != null) {
			users = CommonUtil.toList(userRepository.findAll(user.getFriends()));
		} else {
			users = new ArrayList<>();
		}

		List<Bill> bills = billRepository.findByUserPaidOrBillSplitsUserId(userId, userId);

		List<BillSplit> billSpits = new ArrayList<>();

		BillSplit loggedUser = new BillSplit();
		loggedUser.setUserId(user.getId());
		loggedUser.setName(user.getName());
		loggedUser.setEmail(user.getEmail());

		billSpits.add(0, loggedUser);

		BigDecimal loggedUserAmt = BigDecimal.ZERO;

		for (User userBill : users) {
			BillSplit billSplit = new BillSplit();
			String Id = userBill.getId();

			BigDecimal bigDecimal = BigDecimal.ZERO;
			for (Bill bill : bills) {
				for (BillSplit billsplit : bill.getBillSplits()) {
					if (billsplit.getUserId().equalsIgnoreCase(Id)) {
						if (userId.equalsIgnoreCase(bill.getUserPaid()) || billsplit.getUserId().equalsIgnoreCase(bill.getUserPaid())) {
							bigDecimal = bigDecimal.add(billsplit.getAmount());
						}
					}
				}
			}

			billSplit.setUserId(Id);
			billSplit.setName(userBill.getName());
			billSplit.setEmail(userBill.getEmail());
			billSplit.setAmount(bigDecimal);
			loggedUserAmt = loggedUserAmt.add(bigDecimal);
			billSpits.add(billSplit);
		}

		billSpits.get(0).setAmount(loggedUserAmt);
		return billSpits;
	}

	@Override
	public List<Bill> recentTrans(String userId) throws Exception {

		List<Bill> bills = billRepository.findByUserPaidOrBillSplitsUserId(userId, userId);

		return bills;
	}

	@Override
	public Page<Bill> recentPageTrans(	String userId,
										int page,
										int pagesize) throws Exception {

		Pageable pageable = new PageRequest(page, pagesize, Direction.DESC, "date");

		Page<Bill> bills = billSortAndPageRepository.findByUserPaidOrBillSplitsUserId(userId, userId, pageable);

		return bills;
	}

	@Override
	public List<Bill> recentUserTrans(	String userId,
										String loggedUser) throws Exception {

		List<Bill> bills = billRepository.findByUserPaidAndBillSplitsUserId(userId, loggedUser);

		List<Bill> bills1 = billRepository.findByUserPaidAndBillSplitsUserId(loggedUser, userId);

		bills.addAll(bills1);

		return bills;
	}

	@Override
	public List<Bill> recentGroupTrans(String groupId) throws Exception {

		List<Bill> bills = billRepository.findByGroupId(groupId);

		log.info("Bill Details ---> " + bills.size());

		return bills;

	}

	@Override
	public Bill addBill(String userId) throws Exception {

		List<User> users = null;
		Bill bill = null;

		User user = userRepository.findOne(userId);

		if (user.getFriends() != null) {
			users = CommonUtil.toList(userRepository.findAll(user.getFriends()));
		} else {
			users = new ArrayList<>();
		}
		users.add(user);

		List<BillSplit> billSplits = new ArrayList<>();

		for (User userBill : users) {
			BillSplit billSplit = new BillSplit();
			billSplit.setUserId(userBill.getId());
			billSplit.setName(userBill.getName());
			billSplit.setEmail(userBill.getEmail());
			billSplit.setAmount(BigDecimal.ZERO);
			billSplits.add(billSplit);
		}
		bill = new Bill();
		bill.setBillSplits(billSplits);
		return bill;
	}
	
	

	@Override
	public Bill showBill(String Id) throws Exception {

		Bill bill = billRepository.findOne(Id);
		
		List<BillSplit> billSplits = bill.getBillSplits();
		
		for (BillSplit userBill : billSplits) {
			
			User user = userRepository.findOne(userBill.getUserId());
			userBill.setEmail(user.getEmail());
		}
		
		
		return bill;
	}

}
