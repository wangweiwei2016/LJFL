package com.aimowei.Activity.garbage.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.aimowei.Activity.garbage.bean.DateText;
import com.aimowei.garbage.R;

public class DateAdapter extends BaseAdapter {
	private Context context;
	private List<DateText> list;
	/**
	 * 积分树干左边的图标
	 */
	private int[] tupian = {R.drawable.jifen_5,R.drawable.jifen_4,R.drawable.jifen_3,R.drawable.jifen_2,R.drawable.jifen_1};

	public DateAdapter(Context context, List<DateText> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if (list == null) {
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_time_line, parent, false);
			holder.date = (TextView) convertView
					.findViewById(R.id.txt_date_time);
			holder.content = (TextView) convertView
					.findViewById(R.id.txt_date_content);
			holder.line = (View) convertView.findViewById(R.id.v_line);
			holder.title = (RelativeLayout) convertView
					.findViewById(R.id.rl_title);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_mark);
			holder.number = (TextView) convertView.findViewById(R.id.date2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 时间轴竖线的layout
		LayoutParams params = (LayoutParams) holder.line.getLayoutParams();
		holder.date.setText(list.get(position).getDate());
		holder.line.setBackgroundResource(R.color.suganbg);
		holder.content.setVisibility(View.INVISIBLE);
		holder.number.setText(list.get(position).getNumber());
		holder.iv.setBackgroundResource(tupian[position]);
		params.addRule(RelativeLayout.ALIGN_TOP, R.id.txt_date_content);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.txt_date_content);
		if (list.get(position).getYincangtubiao()) {
			holder.title.setVisibility(View.INVISIBLE);

		}
		if (list.get(position).getIsIn()) {
			// 是否显示右边的图片
			holder.content.setText(list.get(position).getText());
			holder.content.setVisibility(View.VISIBLE);
		}
		if (list.get(position).getIsGET()) {
			holder.line.setBackgroundResource(R.color.sugandeep);
			// 是否把线变色
		}

		holder.line.setLayoutParams(params);

		return convertView;
	}

	public static class ViewHolder {
		RelativeLayout title;
		ImageView iv;
		TextView number;
		View line;
		TextView date;
		TextView content;
	}
}
