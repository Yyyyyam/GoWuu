package cn.edu.neusoft.ypq.gowuu.customer.classify.adapter;

import android.content.Context;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.classify.bean.GoodsClassifyItem;

/**
 * 作者:颜培琦
 * 时间:2022/4/12
 * 功能:ParentAdapter
 */
public class ParentAdapter extends BaseAdapter<GoodsClassifyItem> {
    int selectedPosition = 0;

    public ParentAdapter(Context context, List<GoodsClassifyItem> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, GoodsClassifyItem data, int position) {
        holder.setText(R.id.classify_parent_name, data.getName());
        if (position == selectedPosition)
            holder.setTextSelect(R.id.classify_parent_name, R.color.orange, 18, true);
        else
            holder.setTextSelect(R.id.classify_parent_name, R.color.back_gray, 16, false);
        holder.setOnClickListener(holder.getConvertView(), v -> {
            int lastPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(lastPosition);
            notifyItemChanged(selectedPosition);
            itemSelectListener.setOnSelectListener(holder, data, position);
        });
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_cstm_classify_parent;
    }

    private OnItemSelectListener itemSelectListener;

    public interface OnItemSelectListener {
        void setOnSelectListener(ViewHolder holder, GoodsClassifyItem data, int position);
    }

    public void setOnSelectListener(OnItemSelectListener itemSelectListener) {
        this.itemSelectListener = itemSelectListener;
    }
}
