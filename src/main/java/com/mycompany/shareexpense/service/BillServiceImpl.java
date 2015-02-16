
package com.mycompany.shareexpense.service;

import com.mycompany.shareexpense.model.AmtCurr;
import com.mycompany.shareexpense.model.Bill;
import com.mycompany.shareexpense.model.BillSplit;
import com.mycompany.shareexpense.model.ShareGroup;
import com.mycompany.shareexpense.model.User;
import com.mycompany.shareexpense.model.UserDto;
import com.mycompany.shareexpense.model.UsersBalance;
import com.mycompany.shareexpense.repository.BillRepository;
import com.mycompany.shareexpense.repository.BillSortAndPageRepository;
import com.mycompany.shareexpense.repository.GroupRepository;
import com.mycompany.shareexpense.repository.UserRepository;
import com.mycompany.shareexpense.util.CommonUtil;
import com.mycompany.shareexpense.util.Constants;
import com.mycompany.shareexpense.util.ErrorConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	

	@Autowired
	private GroupRepository groupRepository;

	@Override
	public Bill saveBill(Bill bill) throws Exception {

		Bill billResponse = billRepository.save(bill);

		if (billResponse != null && bill.isEmailRequired()) {


			String subject = null;
			String emailbody = null;
			
			User user = userRepository.findOne(bill.getUserPaid());
			
			try {

				subject = env.getProperty("mail.template.bill.owner.subject");

				emailbody = env.getProperty("mail.template.bill.owner.body");
				emailbody = emailbody.replaceAll("<<billamount>>", bill.getAmount()+"");
				emailbody = emailbody.replaceAll("<<billdesc>>", bill.getDescription() + "");
				emailbody = emailbody.replaceAll("<<username>>", bill.getBy() + "");
				emailbody = emailbody.replaceAll("<<siteurl>>", env.getProperty("application.baseurl"));
		

				CommonUtil.sendEmail(subject, user.getEmail(), emailbody, env);
					
			} catch (Exception exception) {
				log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
			}
			try {

				for (BillSplit billSplit : bill.getBillSplits()) {

					if (billSplit.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)) {

						subject = env.getProperty("mail.template.bill.recipants.subject");
						subject = subject.replaceAll("<<userpaid>>", user.getName() + "");

						emailbody = env.getProperty("mail.template.bill.recipants.body");
						emailbody = emailbody.replaceAll("<<billamount>>", bill.getAmount()+"");
						emailbody = emailbody.replaceAll("<<billdesc>>", bill.getDescription() + "");
						emailbody = emailbody.replaceAll("<<toaddress>>", billSplit.getEmail() + "");
						emailbody = emailbody.replaceAll("<<splitamount>>", billSplit.getAmount()+"");
						emailbody = emailbody.replaceAll("<<username>>", bill.getBy() + "");
						emailbody = emailbody.replaceAll("<<useremail>>", user.getName() + "(" + user.getEmail() + ")");
						emailbody = emailbody.replaceAll("<<siteurl>>", env.getProperty("application.baseurl"));

					}
				
					CommonUtil.sendEmail(subject, billSplit.getEmail(), emailbody, env);
				
				}
			} catch (Exception exception) {
				log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
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
	public List<UsersBalance> usersBillDetails(String userId) throws Exception {

		User user = userRepository.findOne(userId);
		
		List<User> users = null;
		if (user.getFriends() != null) {
			users = CommonUtil.toList(userRepository.findAll(user.getFriends()));
		} else {
			users = new ArrayList<>();
		}



        List<Bill> myBills = billRepository.findByUserPaid(userId);

        log.debug("My Bill"+myBills.size());

        List<Bill> othersBill = billRepository.findByBillSplitsUserId(userId);

        log.debug("Other Bill"+othersBill.size());

		List<UsersBalance> usersBalances = new ArrayList<>();

		UsersBalance loggedUser = new UsersBalance();
		loggedUser.setUserId(user.getId());
		loggedUser.setName(user.getName());
		loggedUser.setEmail(user.getEmail());

		usersBalances.add(0, loggedUser);

		BigDecimal loggedUserUsdAmt = BigDecimal.ZERO;
		
		BigDecimal loggedUserRupeeAmt = BigDecimal.ZERO;
		double tempValue = -1.0;
		
		for (User userBill : users) {

			UsersBalance billSplit = new UsersBalance();

			String Id = userBill.getId();

            BigDecimal usdBigDecimal = BigDecimal.ZERO;
			BigDecimal rupeeBigDecimal = BigDecimal.ZERO;
			for (Bill bill : myBills) {
				
				String currency = bill.getCurrency();

                if(Constants.CURRENCY_USD.equalsIgnoreCase(currency)){
					for (BillSplit billsplit : bill.getBillSplits()) {


                        if (billsplit.getUserId().equalsIgnoreCase(Id)) {
                         	if(billsplit.getAmountStatus().equalsIgnoreCase(Constants.CREDIT)){
								usdBigDecimal = usdBigDecimal.add(billsplit.getAmount());
                			}
								
							if(billsplit.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)){
								usdBigDecimal = usdBigDecimal.subtract(billsplit.getAmount());
                			}
                		}
					}
					
				}else if(Constants.CURRENCY_RUPEE.equalsIgnoreCase(currency)){

                    for (BillSplit billsplit : bill.getBillSplits()) {

                        if (billsplit.getUserId().equalsIgnoreCase(Id)) {

                            if (billsplit.getAmountStatus().equalsIgnoreCase(Constants.CREDIT)) {
                                rupeeBigDecimal = rupeeBigDecimal.add(billsplit.getAmount());
                            }

                            if (billsplit.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)) {
                                rupeeBigDecimal = rupeeBigDecimal.subtract(billsplit.getAmount());
                            }
                        }
                    }
					
				}
				
			}

            for (Bill bill : othersBill) {

                String currency = bill.getCurrency();
                if(Id.equalsIgnoreCase(bill.getUserPaid())){

                    if(Constants.CURRENCY_USD.equalsIgnoreCase(currency)){
                        for (BillSplit billsplit : bill.getBillSplits()) {

                            if (billsplit.getUserId().equalsIgnoreCase(userId)) {
                                if(billsplit.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)){
                                    usdBigDecimal = usdBigDecimal.add(billsplit.getAmount());
                                }

                            }
                        }

                    }else if(Constants.CURRENCY_RUPEE.equalsIgnoreCase(currency)){

                        for (BillSplit billsplit : bill.getBillSplits()) {
                            if (billsplit.getUserId().equalsIgnoreCase(userId)) {

                                if (billsplit.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)) {
                                    rupeeBigDecimal = rupeeBigDecimal.add(billsplit.getAmount());
                                }

                            }
                        }
                    }


                }

            }
			
			loggedUserUsdAmt = loggedUserUsdAmt.add(usdBigDecimal);
			loggedUserRupeeAmt = loggedUserRupeeAmt.add(rupeeBigDecimal);
			
			List<AmtCurr> amtCurrency = new ArrayList<AmtCurr>();
			
			if(!BigDecimal.ZERO.equals(usdBigDecimal)){
				AmtCurr usdKeyValue = new AmtCurr();
				usdKeyValue.setCurrency(Constants.CURRENCY_USD);
				
				if(usdBigDecimal.compareTo(BigDecimal.ZERO) > 0){
					usdKeyValue.setAmountStatus(Constants.CREDIT);
					usdKeyValue.setAmount(usdBigDecimal);
				}else{
					usdBigDecimal = usdBigDecimal.multiply(BigDecimal.valueOf(tempValue));
					usdKeyValue.setAmount(usdBigDecimal);	
					usdKeyValue.setAmountStatus(Constants.DEBIT);
				}
				
				amtCurrency.add(usdKeyValue);
			}

			if(!BigDecimal.ZERO.equals(rupeeBigDecimal)){
				AmtCurr rupeeKeyValue = new AmtCurr();
				rupeeKeyValue.setCurrency(Constants.CURRENCY_RUPEE);
				rupeeKeyValue.setAmount(rupeeBigDecimal);
				if(rupeeBigDecimal.compareTo(BigDecimal.ZERO) > 0){
					rupeeKeyValue.setAmountStatus(Constants.CREDIT);
					rupeeKeyValue.setAmount(rupeeBigDecimal);
				}else{
					rupeeBigDecimal = rupeeBigDecimal.multiply(BigDecimal.valueOf(tempValue));
					rupeeKeyValue.setAmount(rupeeBigDecimal);	
					rupeeKeyValue.setAmountStatus(Constants.DEBIT);
				}
				amtCurrency.add(rupeeKeyValue);
			}

			billSplit.setUserId(Id);
			billSplit.setName(userBill.getName());
			billSplit.setEmail(userBill.getEmail());
			billSplit.setAmtCurrs(amtCurrency);
			
			usersBalances.add(billSplit);
		}
		List<AmtCurr> loggedUserCurrency = new ArrayList<AmtCurr>();
		if(!BigDecimal.ZERO.equals(loggedUserUsdAmt)){
			AmtCurr usdKeyValue = new AmtCurr();
			usdKeyValue.setCurrency(Constants.CURRENCY_USD);
			usdKeyValue.setAmount(loggedUserUsdAmt);
			if(loggedUserUsdAmt.compareTo(BigDecimal.ZERO) > 0){
				usdKeyValue.setAmountStatus(Constants.CREDIT);
				usdKeyValue.setAmount(loggedUserUsdAmt);
			}else{
				loggedUserUsdAmt = loggedUserUsdAmt.multiply(BigDecimal.valueOf(tempValue));
				usdKeyValue.setAmount(loggedUserUsdAmt);	
				usdKeyValue.setAmountStatus(Constants.DEBIT);
			}
			loggedUserCurrency.add(usdKeyValue);
		}
		if(!BigDecimal.ZERO.equals(loggedUserRupeeAmt)){
			AmtCurr rupeeKeyValue = new AmtCurr();
			rupeeKeyValue.setCurrency(Constants.CURRENCY_RUPEE);
			rupeeKeyValue.setAmount(loggedUserRupeeAmt);
			if(loggedUserRupeeAmt.compareTo(BigDecimal.ZERO) > 0){
				rupeeKeyValue.setAmountStatus(Constants.CREDIT);
				rupeeKeyValue.setAmount(loggedUserRupeeAmt);
			}else{
				loggedUserRupeeAmt = loggedUserRupeeAmt.multiply(BigDecimal.valueOf(tempValue));
				rupeeKeyValue.setAmount(loggedUserRupeeAmt);	
				rupeeKeyValue.setAmountStatus(Constants.DEBIT);
			}
			loggedUserCurrency.add(rupeeKeyValue);
		}
		usersBalances.get(0).setAmtCurrs(loggedUserCurrency);
		return usersBalances;
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

	@Override
	public void userSettlement(UserDto userDto) throws Exception {

		User user = userRepository.findOne(userDto.getUserId());
		User loggedUser = userRepository.findOne(userDto.getLoggedUser());

		Bill billInput = new Bill();

		billInput.setBy(loggedUser.getId());
		billInput.setCategory("PAYBACK");
		billInput.setDate(CommonUtil.getCurrentDateTime());
		billInput.setEmailRequired(false);
		
		billInput.setDescription("Paying back pending amount");

		billInput.setSplitType("equally");

        String userSettled = "";
        String whomPaid = "";
		
		for(AmtCurr amtCurr: userDto.getAmtCurrs()){
			
			billInput.setAmount(amtCurr.getAmount());
						
			billInput.setCurrency(amtCurr.getCurrency());

			List<BillSplit> billSplits = new ArrayList<>();
			if (amtCurr.getAmountStatus().equalsIgnoreCase(Constants.CREDIT)) {
				
				billInput.setUserPaid(loggedUser.getId());
                userSettled = loggedUser.getName();

                whomPaid = user.getEmail();

				BillSplit billSplit = new BillSplit();
				billSplit.setAmount(amtCurr.getAmount());
				billSplit.setEmail(user.getEmail());
				billSplit.setName(user.getName());
				billSplit.setUserId(user.getId());
				billSplit.setAmountStatus(Constants.DEBIT);
				

				BillSplit billSplit1 = new BillSplit();
				billSplit1.setAmount(amtCurr.getAmount());
				billSplit1.setEmail(loggedUser.getEmail());
				billSplit1.setName(loggedUser.getName());
				billSplit1.setUserId(loggedUser.getId());
				billSplit1.setAmountStatus(Constants.CREDIT);
				
				billSplits.add(billSplit);
				billSplits.add(billSplit1);

			}
			if (amtCurr.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)) {

				billInput.setUserPaid(user.getId());
                userSettled = user.getName();
                whomPaid = loggedUser.getEmail();

				BillSplit billSplit = new BillSplit();
				billSplit.setAmount(amtCurr.getAmount());
				billSplit.setEmail(user.getEmail());
				billSplit.setName(user.getName());
				billSplit.setUserId(user.getId());
				billSplit.setAmountStatus(Constants.CREDIT);
				
				BillSplit billSplit1 = new BillSplit();
				billSplit1.setAmount(amtCurr.getAmount());
				billSplit1.setEmail(loggedUser.getEmail());
				billSplit1.setName(loggedUser.getName());
				billSplit1.setUserId(loggedUser.getId());
				billSplit1.setAmountStatus(Constants.DEBIT);
				
				billSplits.add(billSplit);
				billSplits.add(billSplit1);
			}
			billInput.setBillSplits(billSplits);
            Bill billResponse = saveBill(billInput);

            if (billResponse != null) {

                String subject = null;
                String emailbody = null;

                try {

                    subject = env.getProperty("mail.template.bill.settle.owner.subject");

                    emailbody = env.getProperty("mail.template.bill.settle.owner.body");
                    emailbody = emailbody.replaceAll("<<billamount>>", billInput.getAmount() + "");
                    emailbody = emailbody.replaceAll("<<billdesc>>", billInput.getDescription() + "");
                    emailbody = emailbody.replaceAll("<<username>>", userSettled + "");
                    emailbody = emailbody.replaceAll("<<loggeduser>>", loggedUser.getName() + "");
                    emailbody = emailbody.replaceAll("<<siteurl>>", env.getProperty("application.baseurl"));

                    CommonUtil.sendEmail(subject, whomPaid, emailbody, env);

                } catch (Exception exception) {
                    log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
                }
            }
		}
	}

	@Override
	public void userPayReminder(UserDto userDto) throws Exception {

		User user = userRepository.findOne(userDto.getUserId());
		User loggedUser = userRepository.findOne(userDto.getLoggedUser());

		String subject = null;
		String emailbody = null;
		try {
			
			for(AmtCurr amtCurr: userDto.getAmtCurrs()){
				if(amtCurr.getAmountStatus().equalsIgnoreCase(Constants.DEBIT)){
					emailbody = env.getProperty("mail.template.payee.reminder.body");
					emailbody = emailbody.replaceAll("<<amount>>", amtCurr.getAmount() + "");
					emailbody = emailbody.replaceAll("<<username>>", loggedUser.getName() + "");
					emailbody = emailbody.replaceAll("<<useremail>>", loggedUser.getEmail() + "");
					emailbody = emailbody.replaceAll("<<siteurl>>", env.getProperty("application.baseurl"));

					CommonUtil.sendEmail(subject, user.getEmail(), emailbody, env);				
	
				}
			}

		} catch (Exception exception) {
			log.error(ErrorConstants.ERR_EMAIL_SENT_FAILED, exception);
		}

	}

	@Override
	public Bill addGroupBill(String groupId) throws Exception {

		List<User> users = null;
		Bill bill = null;

		ShareGroup shareGroup = groupRepository.findOne(groupId);

		if (shareGroup.getUserIds() != null) {
			users = CommonUtil.toList(userRepository.findAll(shareGroup.getUserIds()));
		} else {
			users = new ArrayList<>();
		}
		
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

}
