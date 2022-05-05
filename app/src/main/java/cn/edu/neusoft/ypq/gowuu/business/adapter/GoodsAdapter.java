package cn.edu.neusoft.ypq.gowuu.business.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/23
 * 功能:GoodsAdapter
 */
public class GoodsAdapter extends BaseAdapter<Goods> {
    public GoodsAdapter(Context context, List<Goods> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Goods data, int position) {
        holder.setText(R.id.itm_goods_name, data.getName());
        holder.setText(R.id.itm_goods_price, "￥"+CheckUtils.doubleTrim(data.getPrice()*data.getDiscount()));
        holder.setText(R.id.itm_goods_sale, "已售出"+data.getSale()+"件");
        if (data.getDiscount() == 1) {
            holder.setVisibility(R.id.itm_goods_tv_discount, View.GONE);
        } else {
            Integer d = CheckUtils.doubleTrim((1-data.getDiscount())*100).intValue();
            holder.setText(R.id.itm_goods_tv_discount, "-"+d+"%");
        }
        ImageView imageView = holder.getView(R.id.itm_goods_img);
        GlideUtils.setImage(mContext, Constants.RES_URL+data.getPathList().get(0), imageView);
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
        return R.layout.item_goods;
    }
}
