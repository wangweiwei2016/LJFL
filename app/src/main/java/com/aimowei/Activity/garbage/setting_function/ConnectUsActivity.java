package com.aimowei.Activity.garbage.setting_function;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;  

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.garbage.R;

public class ConnectUsActivity extends BaseActivity {
	
	private ImageButton img_phone;
	private TextView connectus_tv;
	
	
	// 将textview中的字符全角化。即将所有的数字、字母及标点
	//全部转为全角字符，使它们与汉字同占两个字节，这样就可以避
	//免由于占位导致的排版混乱问题了。
	// 半角转为全角的代码如下，只需调用即可。
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
	

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_us);
		settitle("联系我们");
		
		connectus_tv=(TextView)findViewById(R.id.connectus_tv);

		img_phone = (ImageButton)findViewById(R.id.img_phone);
		img_phone.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
            	
            	AlertDialog.Builder builder = new Builder(ConnectUsActivity.this);
            	builder.setMessage("拨打电话：0592-6155289");
            
            	builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener()  {
            		@Override
            		public void onClick(DialogInterface arg0, int arg1) {
            		// TODO Auto-generated method stub
            		arg0.dismiss();
            		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:05926155289"));  
            		startActivity(intent);  
            		}
            		});
            	builder.setNegativeButton("取消",
            			new android.content.DialogInterface.OnClickListener() {
            			@Override
            			public void onClick(DialogInterface dialog, int which) {
            			dialog.dismiss();
            			}
            			});
            			builder.create().show();
					
				}
              
                 
               
            
        });  
	}
}
