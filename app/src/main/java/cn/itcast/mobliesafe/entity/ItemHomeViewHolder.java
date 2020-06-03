package cn.itcast.mobliesafe.entity;

import android.widget.ImageView;
import android.widget.TextView;

public class ItemHomeViewHolder {
	private ImageView img;  //图片控件
	private TextView tv;  //标签控件
	public ImageView getImg() {
		return img;
	}
	public void setImg(ImageView img) {
		this.img = img;
	}
	public TextView getTv() {
		return tv;
	}
	public void setTv(TextView tv) {
		this.tv = tv;
	}
}
