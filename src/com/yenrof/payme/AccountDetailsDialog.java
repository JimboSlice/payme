package com.yenrof.payme;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AccountDetailsDialog extends DialogFragment implements
		OnEditorActionListener {
	@SuppressWarnings("unused")
	private AccountDetailsDialogListener accountDetailsDialogListener;
	private EditText accountName;
	private Button okButton;
	private Button cancelButton;
	private AccountInfo acctInfo = new AccountInfo();

	public interface AccountDetailsDialogListener {
		void onFinishEditDialog(String inputText);

		void onNewAccountAdded(AccountInfo accountInfo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.account_details_fragment,
				container, false);

		final EditText accountNameText = (EditText) view
				.findViewById(R.id.accountName);

		accountNameText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				acctInfo.setAccountName(s.toString());
			}
		});

		final Spinner cardTypeSpinner = (Spinner) view
				.findViewById(R.id.cardType);
		cardTypeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						acctInfo.setCardType(parent.getItemAtPosition(pos)
								.toString());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		final EditText cardNumberText = (EditText) view
				.findViewById(R.id.cardNumber);

		cardNumberText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				acctInfo.setAccountNumber(Long.parseLong(s.toString()));
			}
		});

		final Spinner monthSpinner = (Spinner) view
				.findViewById(R.id.cardExpirationMonth);
		monthSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						acctInfo.setMonth(Integer.parseInt(parent
								.getItemAtPosition(pos).toString()));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		final Spinner yearSpinner = (Spinner) view
				.findViewById(R.id.cardExpirationYear);
		yearSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						acctInfo.setYear(Integer.parseInt(parent
								.getItemAtPosition(pos).toString()));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		final EditText securityCodeText = (EditText) view
				.findViewById(R.id.securityCode);

		securityCodeText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				acctInfo.setSecurityCode(Integer.parseInt(s.toString()));
			}
		});

		okButton = (Button) view.findViewById(R.id.accountOK);
		cancelButton = (Button) view.findViewById(R.id.accountCancel);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Return input text to activity
				AccountDetailsDialogListener activity = (AccountDetailsDialogListener) getActivity();
				activity.onNewAccountAdded(acctInfo); //jkf add edits
				dismiss();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

		getDialog().setTitle("Account Maintenance");
		// Show soft keyboard automatically
		accountNameText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		accountNameText.setOnEditorActionListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			accountDetailsDialogListener = (AccountDetailsDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement AccountDetailsDialogListener");
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// Return input text to activity
			AccountDetailsDialogListener activity = (AccountDetailsDialogListener) getActivity();
			activity.onFinishEditDialog(accountName.getText().toString());
			this.dismiss();
			return true;
		}
		return false;
	}

}
