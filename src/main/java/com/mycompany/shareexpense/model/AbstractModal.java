
package com.mycompany.shareexpense.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author AH0661755
 */
public class AbstractModal {

	@Id
	private String	id;

	@Indexed
	private String	createDate		= null;

	@Indexed
	private String	modifiedDate	= null;

	private String	by				= null;

	public String getBy() {

		return by;
	}


	public void setBy(String by) {

		this.by = by;
	}

	public void setCreateDate(String createDate) {

		this.createDate = createDate;
	}

	public void setModifiedDate(String modifiedDate) {

		this.modifiedDate = modifiedDate;
	}

	public String getCreateDate() {

		return createDate;
	}

	public String getModifiedDate() {

		return modifiedDate;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getId() {

		return id;
	}

}
