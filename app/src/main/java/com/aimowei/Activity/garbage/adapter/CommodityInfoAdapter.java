package com.aimowei.Activity.garbage.adapter;

import java.util.List;
import java.util.Map;

import com.aimowei.garbage.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommodityInfoAdapter extends BaseAdapter {

	private Context context;                        //运行上下文
    public List<Map<String, Object>> listItems;    //商品信息集合
    private LayoutInflater listContainer;           //视图容器
    public final class ListItemView{                //自定义控件集合
            public ImageView image;
            public TextView commodityName;
            public TextView originalIntegral;
            public TextView currentIntegral;
            public TextView exchangeNumber;
            public TextView stockNumber;
     }
    
    public CommodityInfoAdapter(Context context, List<Map<String, Object>> listItems) {  
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.listItems = listItems;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	 /**
     * ListView Item设置
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int selectID = position;
        //自定义视图
        ListItemView  listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.shop_list, null);
            //获取控件对象
            listItemView.image = (ImageView)convertView.findViewById(R.id.iv_image);
            listItemView.commodityName = (TextView)convertView.findViewById(R.id.commodityName);
            listItemView.originalIntegral = (TextView)convertView.findViewById(R.id.originalIntegral);
            listItemView.currentIntegral= (TextView)convertView.findViewById(R.id.currentIntegral);
            listItemView.exchangeNumber = (TextView)convertView.findViewById(R.id.exchangeNumber);
            listItemView.stockNumber = (TextView)convertView.findViewById(R.id.stockNumber);
            //设置控件集到convertView
            convertView.setTag(listItemView);
            Log.e("1111111111", "1111111111111");
        }else {
            listItemView = (ListItemView)convertView.getTag();
            Log.e("2222222222", "222222222222");
        }
        //Log.e("image", (String) listItems.get(position).get("title"));  //测试
        //Log.e("image", (String) listItems.get(position).get("info"));

        //设置文字
        listItemView.commodityName.setText((String) listItems.get(position).get("CommodityName"));
        listItemView.originalIntegral.setText((String) listItems.get(position).get("OriginalIntegral"));
        listItemView.originalIntegral.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );//文字中间加横线
        listItemView.currentIntegral.setText((String) listItems.get(position).get("CurrentIntegral"));
        listItemView.exchangeNumber.setText((String) listItems.get(position).get("ExchangeNumber"));
        listItemView.stockNumber.setText((String) listItems.get(position).get("StockNumber"));

        final int wh = context.getResources().getDimensionPixelOffset(R.dimen.width_120_80);
        final int he = context.getResources().getDimensionPixelOffset(R.dimen.height_100_80);
        //加载图片
        ImageView img = listItemView.image;
        ImageLoader imageLoader;
        DisplayImageOptions options;
        imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(convertView.getContext()));
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_loading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 构建完成
        imageLoader.displayImage((String) listItems.get(position).get("CommodityPicture"), img, options,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				Log.e("LCJ-onLoadingComplete","H="+arg2.getHeight() +",W="+arg2.getWidth());
				((ImageView)arg1).setImageBitmap(resizeBitmap(arg2,wh,he));
			}
			/**
			 * 图片缩放
			 * 
			 * @param bm
			 * @param w
			 *            缩小或放大成的宽
			 * @param h
			 *            缩小或放大成的高
			 * @return
			 */
			public Bitmap resizeBitmap(Bitmap bm,int w, int h) {
				Bitmap BitmapOrg = bm;

				int width = BitmapOrg.getWidth();
				int height = BitmapOrg.getHeight();

				float scaleWidth = ((float) w) / width;
				float scaleHeight = ((float) h) / height;

				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
			}


			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
        return convertView; 
	}

}
