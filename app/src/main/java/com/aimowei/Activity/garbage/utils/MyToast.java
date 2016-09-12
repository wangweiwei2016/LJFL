package com.aimowei.Activity.garbage.utils;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimowei.garbage.R;

 public class MyToast {
	 /**
	  * 自定义Toast
	  * @param context
	  * @param msg 要显示的字符串
	  */
	public static void ShowMyToast(Context context, String msg) {
		View layout = null;

		    layout = View.inflate(context,R.layout.my_toast,
		     null);
		   //查找ImageView控件  
           //注意是在layout中查找  
         
           TextView text = (TextView) layout.findViewById(R.id.text);  
           text.setText(msg);  

           Toast toast = new Toast(context);  
           //设置Toast的位置  
           toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);  
           toast.setDuration(Toast.LENGTH_SHORT);  
           //让Toast显示为我们自定义的样子  
           toast.setView(layout);  
           toast.show();  
	}
 }
