package cn.edu.neusoft.ypq.gowuu.admin.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.user.bean.User;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;

/**
 * 作者:颜培琦
 * 时间:2022/4/26
 * 功能:UserAdapter
 */
public class UserAdapter extends BaseAdapter<User> {
    public UserAdapter(Context context, List<User> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, User data, int position) {
        holder.setText(R.id.itm_admin_mng_tv_name, data.getUsername());
        holder.setImageView(R.id.itm_admin_mng_iv, Constants.RES_URL+data.getAvatar());
        holder.setOnClickListener(holder.getConvertView(),new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.onItemClick(holder,data,position);
                }
            }
        });
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_admin_manage_user;
    }
}
