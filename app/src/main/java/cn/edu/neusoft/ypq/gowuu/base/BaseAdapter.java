package cn.edu.neusoft.ypq.gowuu.base;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:颜培琦
 * 时间:2022/3/23
 * 功能:BaseAdapter
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<T> mDataList;
    protected OnItemClickListener<T> mItemClickListener;

    public BaseAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList == null ? new ArrayList<>() : dataList;
    }

    protected abstract void convert(ViewHolder holder, T data, int position);

    protected abstract int  getItemLayoutId(int viewType);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(mContext, getItemLayoutId(viewType), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        convert(viewHolder, mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public T getData(int position) {
        if (mDataList.isEmpty()) {
            return null;
        }
        return mDataList.get(position);
    }

    public List<T> getDataList(){
        return mDataList;
    }

    public void addMoreData(List<T> list){
        insert(list, mDataList.size());
        notifyItemRangeInserted(mDataList.size(), list.size());
    }

    /**
     * 删除某个位置的数据
     * @param position 未知
     */
    public void remove(int position){
        if (position >= mDataList.size() || position < 0) {
            return;
        }
        mDataList.remove(position);
        notifyItemRemoved(position);
        if (position != mDataList.size()) {
            notifyItemRangeChanged(position , mDataList.size() - position);
        }
    }
    /**
     * 从某个位置开始添加若干个数据
     * @param datas 数据
     * @param position 位置
     */
    public void insert(List<T> datas, int position) {
        if (position > mDataList.size() || position < 0) {
            return;
        }
        mDataList.addAll(position, datas);
        notifyItemRangeInserted(position, datas.size());
        notifyItemRangeChanged(position, mDataList.size() - position);
    }
    /**
     * 给列表末尾追加多个数据
     * @param datas 数据
     */
    public void insert(List<T> datas) {
        insert(datas, mDataList.size());
    }

    /**
     * 添加单个数据到指定位置
     * @param data 数据
     * @param position 位置
     */
    public void insert(T data, int position) {
        if (position > mDataList.size() || position < 0) {
            return;
        }
        mDataList.add(position, data);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size() - position);
    }

    /**
     * 给列表末尾追加单个数据
     * @param data 数据
     */
    public void insert(T data) {
        insert(data, mDataList.size());
    }

    /**
     * 更新某个位置的数据
     * @param position 位置
     */
    public void change(int position) {
        notifyItemChanged(position);
    }

    public void reset(){
        int count = mDataList.size();
        mDataList.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
