package com.aimowei.spinner;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.aimowei.garbage.R;

public class CustomerSpinner extends Spinner implements OnItemClickListener {

	public static SelectDialog dialog = null;
	private ArrayList<String> list;//ArrayList<String> list瀛樺偍锟�?锟斤拷鏄剧ず鐨勬暟锟�?
	public static String text;

	public CustomerSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
    //濡傛灉瑙嗗浘瀹氫箟浜哋nClickListener鐩戝惉鍣紝璋冪敤姝ゆ柟娉曟潵鎵ц
	@Override
	public boolean performClick() {
		Context context = getContext();
		final LayoutInflater inflater = LayoutInflater.from(getContext());
		final View view = inflater.inflate(R.layout.formcustomspinner, null);
		final ListView listview = (ListView) view
				.findViewById(R.id.formcustomspinner_list);
		ListviewAdapter adapters = new ListviewAdapter(context, getList());
		listview.setAdapter(adapters);
		listview.setOnItemClickListener(this);
		dialog = new SelectDialog(context, R.style.dialog);//鍒涘缓Dialog骞惰缃牱寮忎富锟�?
		@SuppressWarnings("deprecation")
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dialog.setCanceledOnTouchOutside(true);// 璁剧疆鐐瑰嚮Dialog澶栭儴浠绘剰鍖哄煙鍏抽棴Dialog
		dialog.show();
		dialog.addContentView(view, params);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> view, View itemView, int position,
			long id) {
		setSelection(position);
		setText(list.get(position));
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		CustomerSpinner.text = text;
	}

}
