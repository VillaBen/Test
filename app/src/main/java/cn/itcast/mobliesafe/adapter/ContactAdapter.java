package cn.itcast.mobliesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.itcast.mobliesafe.R;
import cn.itcast.mobliesafe.entity.ContactInfo;

public class ContactAdapter extends BaseAdapter {

    private List<ContactInfo> contactInfos;
    private Context context;

    public ContactAdapter(List<ContactInfo> contactInfos, Context context) {
        this.contactInfos = contactInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return contactInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null){
            view=View.inflate(context, R.layout.item_list_contact_select,null);
            holder=new ViewHolder();
            holder.mNameTV=view.findViewById(R.id.tv_name);
            holder.mPhoneTV=view.findViewById(R.id.tv_phone);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.mPhoneTV.setText(contactInfos.get(i).phone);
        holder.mNameTV.setText(contactInfos.get(i).name);
        return view;
    }

    static class ViewHolder {
        TextView mNameTV;
        TextView mPhoneTV;
    }
}
