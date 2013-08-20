package com.yenrof.payme;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.yenrof.payme.BuildConfig;
import com.yenrof.payme.fragments.AccountInfoFragment;
import com.yenrof.payme.provider.PayMeAccount;
import com.yenrof.payme.provider.PayMeOwner;
import com.yenrof.payme.provider.PayMeProvider;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		AccountDetailsDialog.AccountDetailsDialogListener {

	

	@SuppressLint("HandlerLeak") 
	public MainActivity() {
		super();
		/*
		 * Instantiates a new anonymous Handler object and defines its
		 * handleMessage() method. The Handler *must* run on the UI thread,
		 * because it moves ProjectInfo object to the View object. To force the
		 * Handler to run on the UI thread, it's defined as part of the
		 * ProjectActivity constructor. The constructor is invoked when the
		 * class is first referenced, and that happens when the View is invoked.
		 * Since the View runs on the UI Thread, so does the constructor and the
		 * Handler.
		 */
		setHandler(new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				Log.i(TAG, "handleMessage");
				/*
				 * handleMessage() defines the operations to perform when the
				 * Handler receives a new Message to process.
				 */
				super.handleMessage(msg);
			}
		});
	}
	
	private Uri uri = null;
	private Button button;
	public static String TAG = "com.yenrof.onsite";
	private static Handler handler;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		button = (Button) findViewById(R.id.addAccount);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showEditDialog();
			}
		});

		// Create the ListFragment and add it to our content.
		FragmentManager fm = getSupportFragmentManager();
		if (fm.findFragmentById(R.id.ui_container) == null) {
			AccountListFragment list = new AccountListFragment();
			fm.beginTransaction().add(R.id.ui_container, list).commit();
		}
		Thread thread = new Thread(null, doBackgroundThreadProcessing,
				"LoadProjects");
		thread.start();
	}

	// Runnable that executes the background processing method.
	private Runnable doBackgroundThreadProcessing = new Runnable() {
		public void run() {
			Log.i(TAG, "run");
			backgroundThreadProcessing();
		}
	};

	// Method which does some processing in the background.
	private void backgroundThreadProcessing() {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "MainActivity backgroundThreadProcessing called");
		}

		Log.i(TAG, "backgroundThreadProcessing");
		ContentValues mNewValues = new ContentValues();

		// Get the Content Resolver.
		ContentResolver cr = getContentResolver();

		// Specify the result column projection. Return the minimum set
		// of columns required to satisfy your requirements.
		String[] from = { PayMeOwner.Owner.FIRST_NAME,
				PayMeOwner.Owner.LAST_NAME, PayMeOwner.Owner.MIDDLE_NAME,
				PayMeOwner.Owner.ADDRESS, PayMeOwner.Owner.CITY,
				PayMeOwner.Owner.STATE, PayMeOwner.Owner.ZIP,
				PayMeOwner.Owner.SSN, PayMeOwner.Owner.EMAIL };

		// Specify the where clause that will limit your results.
		String where = PayMeOwner.Owner.OWNER_ID + "=" + 1;

		// Replace these with valid SQL statements as necessary.
		String whereArgs[] = null;
		String order = null;

		// Return the specified rows.
		Cursor mCursor = cr.query(PayMeOwner.Owner.CONTENT_URI, from, where,
				whereArgs, order);

		if (mCursor != null) {
			/*
			 * Moves to the next row in the cursor. Before the first movement in
			 * the cursor, the "row pointer" is -1, and if you try to retrieve
			 * data at that position you will get an exception.
			 */
			while (mCursor.moveToNext()) {
				// Gets the value from the column.
				// Determine the column index of the column named "first name"
				int index = mCursor.getColumnIndex(PayMeOwner.Owner.FIRST_NAME);
				String newWord = mCursor.getString(index);

				// end of while loop
			}
		} else {

			// Insert code here to report an error if the cursor is null or the
			// provider threw an exception.
		}

		// Close the Cursor when you've finished with it.
		mCursor.close();

		//
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "MainActivity load Owner finished");
		}
	}

	private void showEditDialog() {
		FragmentManager fm = getSupportFragmentManager();
		AccountDetailsDialog editNameDialog = new AccountDetailsDialog();
		editNameDialog.show(fm, "account_details_fragment");
	}

	@Override
	public void onFinishEditDialog(String inputText) {
		Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
	}

	public void onNewAccountAdded(AccountInfo newAccountInfoItem) {
		Log.i(TAG, "onNewAccountAdded");
		// accountInfoItems.add(0, newAccountInfoItem);
		// aa.notifyDataSetChanged();
		// Defines a new Uri object that receives the result of the insertion
		// Defines an object to contain the new values to insert
		ContentValues mNewValues = new ContentValues();

		/*
		 * Sets the values of each column and inserts the word. The arguments to
		 * the "put" method are "column name" and "value"
		 */
		mNewValues.put(PayMeAccount.Account.ACCOUNT_NAME,
				newAccountInfoItem.getAccountName());
		mNewValues.put(PayMeAccount.Account.ACCOUNT_NUMBER,
				newAccountInfoItem.getAccountNumber());
		mNewValues.put(PayMeAccount.Account.CARD_TYPE,
				newAccountInfoItem.getCardType());
		mNewValues.put(PayMeAccount.Account.MONTH,
				newAccountInfoItem.getMonth());
		mNewValues.put(PayMeAccount.Account.YEAR, newAccountInfoItem.getYear());
		mNewValues.put(PayMeAccount.Account.SECURITY_CODE,
				newAccountInfoItem.getSecurityCode());

		setUri(getContentResolver().insert(PayMeAccount.Account.CONTENT_URI, // the
																			// PayMeAccount
																			// content
																			// URI
				mNewValues // the values to insert
				));
	}

	public static Handler getHandler() {
		return handler;
	}

	public static void setHandler(Handler handler) {
		MainActivity.handler = handler;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	/**
	 * This ListFragment displays a list of AccountInfo. It uses an
	 * {@link AccountListLoader} to load its data and the LoaderManager to
	 * manage the loader across the activity and fragment life cycles.
	 */
	public static class AccountListFragment extends ListFragment implements
			LoaderManager.LoaderCallbacks<Cursor> {

		private CursorAdapter adapter;

		private static final String TAG = "AccountListFragment";
		private static final boolean DEBUG = true;
		private static final int LOADER_ID = 1;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.i(TAG, "+++ onCreateView() called! +++");
			super.onCreateView(inflater, container, savedInstanceState);
			View accountlistview = inflater.inflate(R.layout.accountlistview,
					container, false);
			setListAdapter(adapter);
			return accountlistview;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
		}

		@Override
		public void setListAdapter(ListAdapter adapter) {
			super.setListAdapter(adapter);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			Log.i(TAG, "+++ onActivityCreated() called! +++");
			super.onActivityCreated(savedInstanceState);

			if (DEBUG) {
				Log.i(TAG, "+++ Calling initLoader()! +++");
				if (getLoaderManager().getLoader(LOADER_ID) == null) {
					Log.i(TAG, "+++ Initializing the new Loader... +++");
				} else {
					Log.i(TAG,
							"+++ Reconnecting with existing Loader (id '1')... +++");
				}
			}

			String[] from = { PayMeAccount.Account.ACCOUNT_NAME,
					PayMeAccount.Account.ACCOUNT_NUMBER,
					PayMeAccount.Account.CARD_TYPE, PayMeAccount.Account.MONTH,
					PayMeAccount.Account.YEAR,
					PayMeAccount.Account.SECURITY_CODE };
			int[] to = { R.id.accountName, R.id.accountNumber, R.id.cardType,
					R.id.month, R.id.year, R.id.securityCode };
			adapter = new SimpleCursorAdapter(getActivity(),
					R.layout.listview_item_layout, null, from, to,
					CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

			setListAdapter(adapter);

			// Initialize a Loader with id '1'. If the Loader with this id
			// already
			// exists, then the LoaderManager will reuse the existing Loader.
			getLoaderManager().initLoader(LOADER_ID, null, this);
		}

		/**********************/
		/** LOADER CALLBACKS **/
		/**********************/

		/*
		 * Callback that's invoked when the system has initialized the Loader
		 * and is ready to start the query. This usually happens when
		 * initLoader() is called. The loaderID argument contains the ID value
		 * passed to the initLoader() call.
		 */
		@Override
		public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
			/*
			 * Takes action based on the ID of the Loader that's being created
			 */

			Log.i(TAG, "onCreateLoader");
			Uri uri = PayMeAccount.Account.CONTENT_URI;
			String[] mProjection = new String[] {
					PayMeAccount.Account.ACCOUNT_ID,
					PayMeAccount.Account.ACCOUNT_NAME,
					PayMeAccount.Account.ACCOUNT_NUMBER,
					PayMeAccount.Account.CARD_TYPE, PayMeAccount.Account.MONTH,
					PayMeAccount.Account.YEAR,
					PayMeAccount.Account.SECURITY_CODE };
			String mSelectionClause = null;
			// Initializes an array to contain selection arguments
			String[] mSelectionArgs = null;
			String mSortOrder = null;

			switch (loaderID) {
			case LOADER_ID:
				// Returns a new CursorLoader
				return new CursorLoader(getActivity(), // Parent activity
														// context
						uri, // Table to query
						mProjection, // Projection to return
						mSelectionClause, // No selection clause
						mSelectionArgs, // No selection arguments
						mSortOrder // Default sort order
				);
			default:
				// An invalid id was passed in
				return null;
			}
		}

		@Override
		public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
			Log.i(TAG, "+++ onLoadFinished() called! +++");
			adapter.swapCursor(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> cursorLoader) {
			Log.i(TAG, "+++ onLoaderReset() called! +++");
			adapter.swapCursor(null);
		}
	}
}
