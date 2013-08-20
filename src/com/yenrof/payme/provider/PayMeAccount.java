package com.yenrof.payme.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Public API for the PayMe content provider.
 * 
 * The public API for a content provider should only contain information that
 * should be referenced by content provider clients. Implementation details such
 * as constants only used by a content provider subclass should not appear in
 * the provider API.
 */
public class PayMeAccount {
	public static final int ID_COLUMN = 0;
	public static final int ACCOUNT_NAME_COLUMN = 1;
	public static final int ACCOUNT_NUMBER_COLUMN = 2;
	public static final int CARD_TYPE_COLUMN = 3;
	public static final int MONTH_COLUMN = 4;
	public static final int YEAR_COLUMN = 5;
	public static final int SECURITY_CODE_COLUMN = 6;
	public static final int TIMESTAMP_COLUMN = 7;

	public static final String AUTHORITY = "com.yenrof.payme.provider.PayMeProvider";


	/**
	 * Payme columns
	 */
	public static final class Account implements BaseColumns {
		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		// This class cannot be instantiated
		private Account() {
		}

		// uri references all accounts
		public static final Uri ACCOUNTS_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + Account.PAYME_NAME);

		public static final Uri CONTENT_URI = ACCOUNTS_URI;
		public static final String PAYME_NAME = "payme";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.yenrof.payme";
		public static final String CONTENT_ACCOUNT_TYPE = "vnd.android.cursor.item/vnd.yenrof.payme";
		public static final String ACCOUNT_ID = "_id";
		public static final String ACCOUNT_NAME = "accountname";
		public static final String ACCOUNT_NUMBER = "accountnumber";
		public static final String CARD_TYPE = "cardtype";
		public static final String MONTH = "month";
		public static final String YEAR = "year";
		public static final String SECURITY_CODE = "securitycode";
	}
}