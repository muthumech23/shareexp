
package com.mycompany.shareexpense.model;

import java.util.List;

/**
 * @author AH0661755
 */
public class UsersBalance {

	private List<AmtCurr> amtCurrs ;

	private String		name;

	private String		email;
	
	private String		userId;
	
	public String getUserId() {

		return userId;
	}

	
	public List<AmtCurr> getAmtCurrs() {
	
		return amtCurrs;
	}

	
	public void setAmtCurrs(List<AmtCurr> amtCurrs) {
	
		this.amtCurrs = amtCurrs;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

}
