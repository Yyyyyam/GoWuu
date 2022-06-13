package cn.edu.neusoft.ypq.gowuu.customer.classify.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.classify.bean.GoodsClassify;
import cn.edu.neusoft.ypq.gowuu.customer.classify.bean.GoodsClassifyItem;

/**
 * 作者:颜培琦
 * 时间:2022/4/12
 * 功能:ChildAdapter
 */
public class ChildAdapter extends BaseAdapter<GoodsClassify> {

    public ChildAdapter(Context context, List<GoodsClassify> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, GoodsClassify data, int position) {
        holder.setText(R.id.classify_child_title, data.getItem().getName());
        RecyclerView recyclerView = holder.getView(R.id.classify_child_rv);
        ChildItemAdapter itemAdapter = new ChildItemAdapter(mContext, data.getChildItems());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_cstm_classify_child;
    }

    public class ChildItemAdapter extends BaseAdapter<GoodsClassifyItem> {
        public ChildItemAdapter(Context context, List<GoodsClassifyItem> dataList) {
            super(context, dataList);
        }

        @Override
        protected void convert(ViewHolder holder, GoodsClassifyItem data, int position) {
            holder.setText(R.id.classify_child_name, data.getName());
            holder.setOnClickListener(holder.getConvertView(), v ->
                    onItemSelectListener.setOnSelectListener(holder, data, position));
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.item_cstm_classify_child_item;
        }
    }

    private OnItemSelectListener onItemSelectListener;

    public interface OnItemSelectListener {
        void setOnSelectListener(ViewHolder holder, GoodsClassifyItem data, int position);
    }

    public void setOnSelectListener(OnItemSelectListener onSelectListener) {
        this.onItemSelectListener = onSelectListener;
    }
}
