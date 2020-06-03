package cn.itcast.mobliesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobliesafe.R;
import cn.itcast.mobliesafe.entity.ItemHomeEntity;
import cn.itcast.mobliesafe.entity.ItemHomeViewHolder;

public class HomeAdapter extends BaseAdapter {

	private List<ItemHomeEntity> mList=this.getItemHomeData();; // 项数据
	private Context context;// 上下文，获取项布局时需要用到


	private LayoutInflater inflater;// 布局获取器

	public HomeAdapter(Context context) { // 通过构造函数读取实例化时传递过来的上下文
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// arg0表示项id,arg1表示当前项,arg2为父容器
		inflater = LayoutInflater.from(context);
		ItemHomeViewHolder holder=new ItemHomeViewHolder();

		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.item_home, null);
			holder.setImg((ImageView) arg1.findViewById(R.id.iv_icon));//获取项布局
			holder.setTv((TextView) arg1.findViewById(R.id.tv_name));
			arg1.setTag(holder);
		} else {
			holder=(ItemHomeViewHolder) arg1.getTag();
		}
		holder.getImg().setImageResource(mList.get(arg0).getImgId());
		holder.getTv().setText(mList.get(arg0).getTitle());
		return arg1;
	}

	/**
	 * 封装项数
	 *
	 * @return List<ItemHomeEntity>
	 */
	private List<ItemHomeEntity> getItemHomeData() {
		List<ItemHomeEntity> list = new ArrayList<ItemHomeEntity>();
		list.add(new ItemHomeEntity(1, R.drawable.safe, "手机防盗"));
		list.add(new ItemHomeEntity(2, R.drawable.callmsgsafe, "通讯卫士"));
		list.add(new ItemHomeEntity(3, R.drawable.app, "软件管家"));
		list.add(new ItemHomeEntity(4, R.drawable.trojan, "手机杀毒"));
		list.add(new ItemHomeEntity(5, R.drawable.sysoptimize, "缓存清理"));
		list.add(new ItemHomeEntity(6, R.drawable.taskmanager, "进程管理"));
		list.add(new ItemHomeEntity(7, R.drawable.netmanager, "流量统计"));
		list.add(new ItemHomeEntity(8, R.drawable.atools, "高级工具"));
		list.add(new ItemHomeEntity(9, R.drawable.settings, "设置中心"));
		list.add(new ItemHomeEntity(10, R.drawable.about, "关于"));
		return list;
	}

}