package com.yenrof.payme;

//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountInfoAdapter extends ArrayAdapter<AccountInfo> {
	private LayoutInflater mInflater;
	private int resource;

	public AccountInfoAdapter(Context ctx) {
		super(ctx, android.R.layout.simple_list_item_2);
		setmInflater((LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
	}

	public AccountInfoAdapter(Context context, int resource,
			List<AccountInfo> items) {
		super(context, resource, items);
		this.resource = resource;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout accountView;

		AccountInfo item = getItem(position);

		String accountName = item.getAccountName();
		/*
		 * Date createdDate = item.getCreated(); SimpleDateFormat sdf = new
		 * SimpleDateFormat("dd/MM/yy");
		 * 
		 * @SuppressWarnings("unused") String dateString =
		 * sdf.format(createdDate);
		 */

		if (convertView == null) {
			accountView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, accountView, true);
		} else {
			accountView = (LinearLayout) convertView;
		}

		TextView accountInfoView = (TextView) accountView
				.findViewById(R.id.row);

		accountInfoView.setText(accountName + " " + item.getCardType() + " "
				+ item.getAccountNumber());

		return accountView;
	}

	public void setData(List<AccountInfo> data) {
		clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				add(data.get(i));
			}
		}
	}

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}
}
