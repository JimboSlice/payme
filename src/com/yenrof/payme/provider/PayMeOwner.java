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
public class PayMeOwner {
	public static final int ID_COLUMN = 0;
	public static final int FIRST_NAME_COLUMN = 1;
	public static final int LAST_NAME_COLUMN = 2;
	public static final int MIDDLE_NAME_COLUMN = 3;
	public static final int ADDRESS_COLUMN = 4;
	public static final int CITY_COLUMN = 5;
	public static final int STATE_COLUMN = 6;
	public static final int ZIP_COLUMN = 7;
	public static final int SSN_COLUMN = 8;
	public static final int EMAIL_COLUMN = 9;


	public static final String AUTHORITY = "com.yenrof.payme.provider.PayMeProvider";


	/**
	 * Owner columns
	 */
	public static final class Owner implements BaseColumns {
		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		// This class cannot be instantiated
		private Owner() {
		}

		// uri references all owners
		public static final Uri OWNERS_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + Owner.OWNER_NAME);

		public static final Uri CONTENT_URI = OWNERS_URI;
		public static final String OWNER_NAME = "owner";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.yenrof.owner";
		public static final String CONTENT_ACCOUNT_TYPE = "vnd.android.cursor.item/vnd.yenrof.owner";
		public static final String OWNER_ID = "_id";
		public static final String FIRST_NAME = "firstname";
		public static final String LAST_NAME = "lastname";
		public static final String MIDDLE_NAME = "middlename";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIP = "zip";
		public static final String SSN = "ssn";
		public static final String EMAIL = "email";

	}
}