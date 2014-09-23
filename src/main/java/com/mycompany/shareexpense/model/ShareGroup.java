
package com.mycompany.shareexpense.model;

import java.util.List;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author AH0661755
 */
@Document(collection = "sharegroups")
public class ShareGroup extends AbstractModal {

	private String			groupName;

	private String			email;

	private List<String>	userIds;

	@Indexed
	private String			userId;

	public String getGroupName() {

		return groupName;
	}

	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public List<String> getUserIds() {

		return userIds;
	}

	public void setUserIds(List<String> userIds) {

		this.userIds = userIds;
	}

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

}
