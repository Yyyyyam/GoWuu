package cn.edu.neusoft.ypq.gowuu.customer.me.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;

/**
 * 作者:颜培琦
 * 时间:2022/3/24
 * 功能:RequestImgAdapter
 */
public class RequestImgAdapter extends BaseAdapter<Uri> {

    public RequestImgAdapter(Context context, List<Uri> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Uri data, int position) {
        holder.setImageView(R.id.itm_usr_request_img, data);
        holder.setOnClickListener(R.id.itm_usr_request_bt_delete, v -> remove(position));
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_usr_request_img;
    }
}
