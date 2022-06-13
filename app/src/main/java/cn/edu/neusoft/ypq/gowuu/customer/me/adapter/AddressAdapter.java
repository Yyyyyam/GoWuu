package cn.edu.neusoft.ypq.gowuu.customer.me.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Address;

/**
 * 作者:颜培琦
 * 时间:2022/3/24
 * 功能:AddressAdapter
 */
public class AddressAdapter extends BaseAdapter<Address> {
    public AddressAdapter(Context context, List<Address> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Address data, int position) {
        if (data.getIsDefault() == 1){
            holder.setVisibility(R.id.itm_address_tv_default, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.itm_address_tv_default, View.GONE);
        }
        holder.setText(R.id.itm_address_tv_tag, data.getTag());
        holder.setText(R.id.itm_address_tv_area,data.getProvinceName()+data.getCityName()+data.getAreaName());
        holder.setText(R.id.itm_address_tv_detail, data.getDetail());
        holder.setText(R.id.itm_address_tv_name, data.getName());
        holder.setText(R.id.itm_address_tv_phone, data.getPhone());
        holder.setOnClickListener(R.id.itm_address_ib_edit, v ->
                mItemClickListener.onItemClick(holder, data, position));

        holder.getConvertView().setOnClickListener(v ->
                mSelectListener.select(holder, data, position));
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_cstm_address;
    }

    private OnSelectListener mSelectListener;

    public interface OnSelectListener{
        void select(ViewHolder viewHolder, Address address, int position);
    }

    public void select(OnSelectListener mSelectListener){
        this.mSelectListener = mSelectListener;
    }
}
