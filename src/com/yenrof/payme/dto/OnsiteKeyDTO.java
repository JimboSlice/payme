package com.yenrof.payme.dto;

import java.io.Serializable;
/**
 * The DTO class for the UserCredentials.
 * 
 */
public class OnsiteKeyDTO implements Serializable {
	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

	private long personId;

	private long companyId;

	public OnsiteKeyDTO() {
	}

}