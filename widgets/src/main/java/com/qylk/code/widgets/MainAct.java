package com.qylk.code.widgets;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class MainAct extends ListActivity implements OnItemClickListener {
	private List<ActivityInfo> activities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities = getActivities(this);// 获取所有activity

		// 取得label
		String[] labels = new String[activities.size()];
		for (int i = 0; i < activities.size(); i++) {
			labels[i] = activities.get(i).nonLocalizedLabel.toString();
		}

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, labels));
		getListView().setOnItemClickListener(this);
	}

	public ArrayList<ActivityInfo> getActivities(Context ctx) {
		ArrayList<ActivityInfo> result = new ArrayList<ActivityInfo>();
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setPackage(ctx.getPackageName());
		for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(
				intent, 0)) {
			final String name = info.activityInfo.name;
			if (!name.equals(this.getClass().getName()))// 排除自己
				result.add(info.activityInfo);
		}
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		List<ActivityInfo> activities = getActivities(this);
		Intent intent;
		try {
			intent = new Intent(this,
					Class.forName(activities.get(position).name));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		startActivity(intent);
	}
}
