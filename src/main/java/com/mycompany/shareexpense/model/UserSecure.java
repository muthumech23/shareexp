
package com.mycompany.shareexpense.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author AH0661755
 */
@Document(collection = "usersecures")
public class UserSecure extends AbstractModal {

	@Indexed(unique = true)
	private String	userId;

	private String	password;

	private String	status;

	private String	randomString;

	
	
	public String getRandomString() {
	
		return randomString;
	}


	
	public void setRandomString(String randomString) {
	
		this.randomString = randomString;
	}


	public String getStatus() {
	
		return status;
	}

	
	public void setStatus(String status) {
	
		this.status = status;
	}

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

}
