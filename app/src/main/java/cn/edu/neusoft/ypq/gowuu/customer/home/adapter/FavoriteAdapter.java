package cn.edu.neusoft.ypq.gowuu.customer.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.home.bean.Favorite;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/26
 * 功能:FavoriteAdapter
 */
public class FavoriteAdapter extends BaseAdapter<Favorite> {

    private boolean edit = false;
    private boolean selectAll = false;

    public FavoriteAdapter(Context context, List<Favorite> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Favorite data, int position) {
        mDataList.get(position).setPosition(position);
        holder.setText(R.id.itm_favorite_goods_tv_name, data.getGoods().getName());
        holder.setText(R.id.itm_favorite_goods_tv_count, data.getGoods().getFavorite()+"人收藏");
        holder.setText(R.id.itm_favorite_goods_tv_price, String.valueOf(data.getGoods().getPrice()));
        ImageView imageView = holder.getView(R.id.itm_favorite_goods_image);
        GlideUtils.setImage(mContext, Constants.RES_URL+data.getGoods().getPathList().get(0), imageView);
        if (edit){
            holder.setVisibility(R.id.itm_favorite_goods_cb, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.itm_favorite_goods_cb, View.GONE);
        }
        holder.setChecked(R.id.itm_favorite_goods_cb, data.getIsChecked());
        double compare = data.getPrice()
                - CheckUtils.doubleTrim(data.getGoods().getPrice()*data.getGoods().getDiscount());
        if (compare != 0){
            if (compare < 0){
                compare = -compare;
                holder.setText(R.id.itm_favorite_goods_tv_compare, "距收藏涨￥"+ CheckUtils.doubleTrim(compare));
            } else {
                holder.setText(R.id.itm_favorite_goods_tv_compare, "距收藏降￥"+CheckUtils.doubleTrim(compare));
            }
            holder.setVisibility(R.id.itm_favorite_goods_tv_compare, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.itm_favorite_goods_tv_compare, View.GONE);
        }

        CheckBox checkBox = holder.getView(R.id.itm_favorite_goods_cb);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.setOnSelectListener(checkBox.isChecked(), data, position);
            }
        });

        holder.setOnClickListener(holder.getConvertView(),new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.onItemClick(holder,data,position);
                    onSelectListener.setOnSelectListener(checkBox.isChecked(), data, position);
                }
            }
        });
    }

    public void setEdit(boolean edit){
        this.edit = edit;
    }

    public boolean getEdit(){
        return edit;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_cstm_favorite_goods;
    }

    private OnSelectListener onSelectListener;

    public interface OnSelectListener{
        void setOnSelectListener(boolean isChecked, Favorite favorite, int position);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}
