
package com.mycompany.shareexpense.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author AH0661755
 */
@Document(collection = "bills")
public class Bill extends AbstractModal {

	@Indexed
	private BigDecimal	amount;

	private String		description;

	@Indexed
	private Date		date;

	private String		Category;

	@Indexed
	private String		userPaid;

	@Indexed
	private String		groupId;

	public List<BillSplit> getBillSplits() {

		return billSplits;
	}

	public void setBillSplits(List<BillSplit> billSplits) {

		this.billSplits = billSplits;
	}

	private List<BillSplit>	billSplits;

	public BigDecimal getAmount() {

		return amount;
	}

	public void setAmount(BigDecimal amount) {

		this.amount = amount;
	}

	public String getGroupId() {

		return groupId;
	}

	public String getUserPaid() {

		return userPaid;
	}

	public void setUserPaid(String userPaid) {

		this.userPaid = userPaid;
	}

	public void setGroupId(String groupId) {

		this.groupId = groupId;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public Date getDate() {

		return date;
	}

	public void setDate(Date date) {

		this.date = date;
	}

	public String getCategory() {

		return Category;
	}

	public void setCategory(String Category) {

		this.Category = Category;
	}

}
