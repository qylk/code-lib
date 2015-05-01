package com.qylk.code.widgets;

import android.app.ExpandableListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class testExpandableListView extends ExpandableListActivity {
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new MyAdapter();
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(this,
				mAdapter.getChild(groupPosition, childPosition).toString(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	private class MyAdapter extends BaseExpandableListAdapter {
		private String[] groups = { "People Names", "Dog Names", "Cat Names",
				"Fish Names" };
		private String[][] children = {
				{ "Arnold", "Barry", "Chuck", "David" },
				{ "Ace", "Bandit", "Cha-Cha", "Deuce" },
				{ "Fluffy", "Snuggles" }, { "Goldy", "Bubbles" } };

		public TextView getGenericView() {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);
			TextView textView = new TextView(getBaseContext());
			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setPadding(36, 0, 0, 0);
			textView.setTextColor(Color.BLACK);
			return textView;
		}

		@Override
		public int getGroupCount() {
			return groups.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());
			return textView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
}
