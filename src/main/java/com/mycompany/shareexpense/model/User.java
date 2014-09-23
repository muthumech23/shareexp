
package com.mycompany.shareexpense.model;

import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author AH0661755
 */
@Document(collection = "users")
public class User extends AbstractModal {

	private String			name;
	
	@Transient
	private String			status;

	@Indexed(unique = true)
	private String			email;

	@Transient
	private String			password;

	@Indexed
	private List<String>	friends;

	public void setName(String name) {

		this.name = name;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getName() {

		return name;
	}

	public String getEmail() {

		return email;
	}

	public String getPassword() {

		return password;
	}

	public List<String> getFriends() {

		return friends;
	}

	public void setFriends(List<String> friends) {

		this.friends = friends;
	}
	

	public String getStatus() {
	
		return status;
	}

	
	public void setStatus(String status) {
	
		this.status = status;
	}


}
