package cn.edu.neusoft.ypq.gowuu.customer.me.adapter;

import android.content.Context;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Message;

/**
 * 作者:颜培琦
 * 时间:2022/3/24
 * 功能:MessageAdapter
 */
public class MessageAdapter extends BaseAdapter<Message> {

    public MessageAdapter(Context context, List<Message> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Message data, int position) {
        if (data.getType() == 0){
            holder.setText(R.id.itm_cstm_message_tv_type,"申请结果");
        }else {
            holder.setText(R.id.itm_cstm_message_tv_type,"举报反馈");
        }
        holder.setText(R.id.itm_cstm_message_tv_date, data.getDate());
        holder.setText(R.id.itm_cstm_message_tv_detail, data.getDetail());
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_cstm_message;
    }
}
