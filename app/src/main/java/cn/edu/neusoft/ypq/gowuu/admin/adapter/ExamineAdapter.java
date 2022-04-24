package cn.edu.neusoft.ypq.gowuu.admin.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Examine;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/24
 * 功能:ExamineAdapter
 */
public class ExamineAdapter extends BaseAdapter<Examine> {
    public ExamineAdapter(Context context, List<Examine> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Examine data, int position) {
        if (data.getAvatar() == null){
            holder.setImageView(R.id.itm_admin_exm_avatar, R.drawable.ic_launcher_foreground);
        } else {
            holder.setImageView(R.id.itm_admin_exm_avatar, Constants.RES_URL+data.getAvatar());
        }
        holder.setText(R.id.itm_admin_exm_tv_name, data.getName());
        holder.setText(R.id.itm_admin_exm_tv_time, data.getTime());
        holder.setText(R.id.itm_admin_exm_tv_bzns_name, data.getBusinessName());
        if (data.getType() == 0){
            holder.setText(R.id.itm_admin_exm_tv_type, "商家申请");
        } else {
            holder.setText(R.id.itm_admin_exm_tv_type, "商品举报");
        }
        holder.setText(R.id.itm_admin_exm_tv_detail, data.getDetail());
        if (data.getState() == 0){
            holder.setVisibility(R.id.itm_admin_exm_tv_state, View.GONE);
            holder.setVisibility(R.id.itm_admin_exm_bt_agree, View.VISIBLE);
            holder.setVisibility(R.id.itm_admin_exm_bt_refuse, View.VISIBLE);
        } else if (data.getState() == 1) {
            holder.setText(R.id.itm_admin_exm_tv_state, "已同意");
            holder.setVisibility(R.id.itm_admin_exm_tv_state, View.VISIBLE);
            holder.setVisibility(R.id.itm_admin_exm_bt_agree, View.GONE);
            holder.setVisibility(R.id.itm_admin_exm_bt_refuse, View.GONE);
        } else {
            holder.setText(R.id.itm_admin_exm_tv_state, "已拒绝");
            holder.setVisibility(R.id.itm_admin_exm_tv_state, View.VISIBLE);
            holder.setVisibility(R.id.itm_admin_exm_bt_agree, View.GONE);
            holder.setVisibility(R.id.itm_admin_exm_bt_refuse, View.GONE);
        }
        holder.setOnClickListener(R.id.itm_admin_exm_bt_refuse, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    data.setState(2);
                    mItemClickListener.onItemClick(holder,data,position);
                }
            }
        });
        holder.setOnClickListener(R.id.itm_admin_exm_bt_agree, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    data.setState(1);
                    mItemClickListener.onItemClick(holder,data,position);
                }
            }
        });
        holder.setBanner(R.id.itm_admin_exm_banner, data.getRequestPic(), 500000);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_admin_examine;
    }
}
