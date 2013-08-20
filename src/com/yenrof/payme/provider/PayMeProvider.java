package com.yenrof.payme.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import java.util.HashMap;

/**
 * Simple content provider that demonstrates the basics of creating a content
 * provider that stores account meta-data.
 */
public class PayMeProvider extends ContentProvider {
	public static final String PAYME = "payme_store";
	public static final String PAYME_TABLE_NAME = "payme";
	public static final String OWNER_TABLE_NAME = "owner";

	private static final int ACCOUNTS = 1;
	private static final int ACCOUNT_ID = 2;
	private static final int OWNERS = 3;
	private static final int OWNER_ID = 4;

	private static UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(PayMeAccount.AUTHORITY,
				PayMeAccount.Account.PAYME_NAME, ACCOUNTS);
		// use of the hash character indicates matching of an id
		sUriMatcher.addURI(PayMeAccount.AUTHORITY,
				PayMeAccount.Account.PAYME_NAME + "/#", ACCOUNT_ID);
		sUriMatcher.addURI(PayMeOwner.AUTHORITY, PayMeOwner.Owner.OWNER_NAME,
				OWNERS);
		// use of the hash character indicates matching of an id
		sUriMatcher.addURI(PayMeOwner.AUTHORITY, PayMeOwner.Owner.OWNER_NAME
				+ "/#", OWNER_ID);

	}

	private static class PayMeDbHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = PAYME + ".db";
		private static int DATABASE_VERSION = 2;

		PayMeDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqLiteDatabase) {
			createTable(sqLiteDatabase);
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv) {
			sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PAYME_TABLE_NAME
					+ ";");
			sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_TABLE_NAME
					+ ";");
			createTable(sqLiteDatabase);
		}

		private void createTable(SQLiteDatabase sqLiteDatabase) {
			String qs = "CREATE TABLE " + PAYME_TABLE_NAME + " ("
					+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ PayMeAccount.Account.ACCOUNT_NAME + " TEXT, "
					+ PayMeAccount.Account.ACCOUNT_NUMBER + " TEXT, "
					+ PayMeAccount.Account.MONTH + " integer, "
					+ PayMeAccount.Account.YEAR + " integer, "
					+ PayMeAccount.Account.SECURITY_CODE + " TEXT, "
					+ PayMeAccount.Account.CARD_TYPE + " TEXT);";
			sqLiteDatabase.execSQL(qs);
			qs = "CREATE TABLE " + OWNER_TABLE_NAME + " (" 
					+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ PayMeOwner.Owner.FIRST_NAME + " TEXT, "
					+ PayMeOwner.Owner.LAST_NAME + " TEXT, "
					+ PayMeOwner.Owner.MIDDLE_NAME + " TEXT, "
					+ PayMeOwner.Owner.ADDRESS + " TEXT, "
					+ PayMeOwner.Owner.CITY + " TEXT, "
					+ PayMeOwner.Owner.STATE + " TEXT, " 
					+ PayMeOwner.Owner.ZIP   + " TEXT, " 
					+ PayMeOwner.Owner.EMAIL + " TEXT, "
					+ PayMeOwner.Owner.SSN + " TEXT);";
			sqLiteDatabase.execSQL(qs);
		}
	}

	private PayMeDbHelper mOpenDbHelper;

	@Override
	public boolean onCreate() {
		mOpenDbHelper = new PayMeDbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case ACCOUNTS:
			return PayMeAccount.Account.CONTENT_TYPE;
		case ACCOUNT_ID:
			return PayMeAccount.Account.CONTENT_ACCOUNT_TYPE;
		case OWNERS:
			return PayMeOwner.Owner.CONTENT_TYPE;
		case OWNER_ID:
			return PayMeOwner.Owner.CONTENT_ACCOUNT_TYPE;

		default:
			throw new IllegalArgumentException("Unknown video type: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String where,
			String[] whereArgs, String sortOrder) {
		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = PayMeAccount.Account.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		int match = sUriMatcher.match(uri);
		long accountID = 0;
		Cursor c;
		switch (match) {
		case ACCOUNTS:
			// query the database for all videos
			c = getDb().query(PAYME_TABLE_NAME, projection, where, whereArgs,
					null, null, null);

			c.setNotificationUri(getContext().getContentResolver(),
					PayMeAccount.Account.CONTENT_URI);
			break;
		case ACCOUNT_ID:
			// query the database for a specific account
			accountID = ContentUris.parseId(uri);
			c = getDb()
					.query(PAYME_TABLE_NAME,
							projection,
							BaseColumns._ID
									+ " = "
									+ accountID
									+ (!TextUtils.isEmpty(where) ? " AND ("
											+ where + ')' : ""), whereArgs,
							null, null, null);
			c.setNotificationUri(getContext().getContentResolver(),
					PayMeAccount.Account.CONTENT_URI);
			break;
		case OWNERS:
			// query the database for all owners
			c = getDb().query(OWNER_TABLE_NAME, projection, where, whereArgs,
					null, null, null);

			c.setNotificationUri(getContext().getContentResolver(),
					PayMeOwner.Owner.CONTENT_URI);
			break;
		case OWNER_ID:
			// query the database for a specific account
			accountID = ContentUris.parseId(uri);
			c = getDb()
					.query(OWNER_TABLE_NAME,
							projection,
							BaseColumns._ID
									+ " = "
									+ accountID
									+ (!TextUtils.isEmpty(where) ? " AND ("
											+ where + ')' : ""), whereArgs,
							null, null, null);
			c.setNotificationUri(getContext().getContentResolver(),
					PayMeOwner.Owner.CONTENT_URI);
			break;

		default:
			throw new IllegalArgumentException("unsupported uri: " + uri);
		}

		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if ((sUriMatcher.match(uri) != ACCOUNTS)
				&& (sUriMatcher.match(uri) != OWNERS)) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		long rowId =0;
		SQLiteDatabase db=null;
		int match = sUriMatcher.match(uri);
		switch (match) {
		case ACCOUNTS:
			verifyAccountValues(values);
			// insert the initialValues into a new database row
			db = mOpenDbHelper.getWritableDatabase();
		    rowId = db.insert(PAYME_TABLE_NAME,
					PayMeAccount.Account.PAYME_NAME, values);
			if (rowId > 0) {
				Uri accountURi = ContentUris.withAppendedId(
						PayMeAccount.Account.CONTENT_URI, rowId);
				getContext().getContentResolver()
						.notifyChange(accountURi, null);
				return accountURi;
			}
			break;
		case OWNERS:
			verifyOwnerValues(values);
			// insert the initialValues into a new database row
		    db = mOpenDbHelper.getWritableDatabase();
			rowId = db.insert(OWNER_TABLE_NAME,
					PayMeOwner.Owner.OWNER_NAME, values);
			if (rowId > 0) {
				Uri ownerURi = ContentUris.withAppendedId(
						PayMeOwner.Owner.CONTENT_URI, rowId);
				getContext().getContentResolver()
						.notifyChange(ownerURi, null);
				return ownerURi;
			}
			break;


		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	private void verifyAccountValues(ContentValues values) {
		// Make sure that the fields are all set
		if (!values.containsKey(PayMeAccount.Account.ACCOUNT_NAME)) {
			Resources r = Resources.getSystem();
			values.put(PayMeAccount.Account.ACCOUNT_NAME,
					r.getString(android.R.string.untitled));
		}

		if (!values.containsKey(PayMeAccount.Account.ACCOUNT_NUMBER)) {
			values.put(PayMeAccount.Account.ACCOUNT_NUMBER, "");
		}

		if (!values.containsKey(PayMeAccount.Account.CARD_TYPE)) {
			values.put(PayMeAccount.Account.CARD_TYPE, "");
		}
		if (!values.containsKey(PayMeAccount.Account.MONTH)) {
			values.put(PayMeAccount.Account.MONTH, "");
		}
		if (!values.containsKey(PayMeAccount.Account.YEAR)) {
			values.put(PayMeAccount.Account.YEAR, "");
		}
		if (!values.containsKey(PayMeAccount.Account.SECURITY_CODE)) {
			values.put(PayMeAccount.Account.SECURITY_CODE, "");
		}

		
	}
	
	private void verifyOwnerValues(ContentValues values) {
		// Make sure that the fields are all set
		if (!values.containsKey(PayMeOwner.Owner.FIRST_NAME)) {
			Resources r = Resources.getSystem();
			values.put(PayMeOwner.Owner.FIRST_NAME,
					r.getString(android.R.string.untitled));
		}

		if (!values.containsKey(PayMeOwner.Owner.LAST_NAME)) {
			values.put(PayMeOwner.Owner.LAST_NAME, "");
		}

		if (!values.containsKey(PayMeOwner.Owner.ADDRESS)) {
			values.put(PayMeOwner.Owner.ADDRESS, "");
		}
		if (!values.containsKey(PayMeOwner.Owner.CITY)) {
			values.put(PayMeOwner.Owner.CITY, "");
		}
		if (!values.containsKey(PayMeOwner.Owner.STATE)) {
			values.put(PayMeOwner.Owner.STATE, "");
		}
		if (!values.containsKey(PayMeOwner.Owner.ZIP)) {
			values.put(PayMeOwner.Owner.ZIP, "");
		}
		if (!values.containsKey(PayMeOwner.Owner.SSN)) {
			values.put(PayMeOwner.Owner.SSN, "");
		}
		if (!values.containsKey(PayMeOwner.Owner.EMAIL)) {
			values.put(PayMeOwner.Owner.EMAIL, "");
		}
	}


	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int match = sUriMatcher.match(uri);
		int affected;
		long accountId = 0;
		switch (match) {
		case ACCOUNTS:
			affected = getDb().delete(PAYME_TABLE_NAME,
					(!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
					whereArgs);
			break;
		case ACCOUNT_ID:
			accountId = ContentUris.parseId(uri);
			affected = getDb().delete(
					PAYME_TABLE_NAME,
					BaseColumns._ID
							+ "="
							+ accountId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case OWNERS:
			affected = getDb().delete(OWNER_TABLE_NAME,
					(!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
					whereArgs);
			break;
		case OWNER_ID:
			accountId = ContentUris.parseId(uri);
			affected = getDb().delete(
					OWNER_TABLE_NAME,
					BaseColumns._ID
							+ "="
							+ accountId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("unknown account element: "
					+ uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return affected;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int affected;
		String accountId = null;
		switch (sUriMatcher.match(uri)) {
		case ACCOUNTS:
			affected = getDb().update(PAYME_TABLE_NAME, values, where,
					whereArgs);
			break;

		case ACCOUNT_ID:
			accountId = uri.getPathSegments().get(1);
			affected = getDb().update(
					PAYME_TABLE_NAME,
					values,
					BaseColumns._ID
							+ "="
							+ accountId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case OWNERS:
			affected = getDb().update(OWNER_TABLE_NAME, values, where,
					whereArgs);
			break;

		case OWNER_ID:
			accountId = uri.getPathSegments().get(1);
			affected = getDb().update(
					OWNER_TABLE_NAME,
					values,
					BaseColumns._ID
							+ "="
							+ accountId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return affected;
	}

	private SQLiteDatabase getDb() {
		return mOpenDbHelper.getWritableDatabase();
	}
}