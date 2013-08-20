package com.yenrof.payme;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountInfo {

	String accountName;
	long accountNumber;
	String cardType;
	int month;
	int year;
	int securityCode;
	Date created;
	private Drawable mIcon;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(int securityCode) {
		this.securityCode = securityCode;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	public Date getCreated() {
		return created;
	}

	public AccountInfo(String accountName) {
		this(accountName, new Date(java.lang.System.currentTimeMillis()));
	}

	public AccountInfo(String accountName, Date _created) {
		this.accountName = accountName;
		created = _created;
	}

	public AccountInfo() {
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		String dateString = sdf.format(created);
		return "(" + dateString + ") " + accountName;
	}

	public Drawable getmIcon() {
		return mIcon;
	}

	public void setmIcon(Drawable mIcon) {
		this.mIcon = mIcon;
	}
}
