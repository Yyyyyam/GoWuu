package cn.edu.neusoft.ypq.gowuu.customer.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.Cart;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/30
 * 功能:OrderConfirmAdapter
 */
public class OrderConfirmAdapter extends BaseAdapter<Cart> {
    OrderConfirmGoodsAdapter goodsAdapter;

    public OrderConfirmAdapter(Context context, List<Cart> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Cart data, int position) {
        holder.setText(R.id.itm_odr_tv_business_name, data.getHeader().getBznsName());
        holder.setVisibility(R.id.itm_odr_tv_order_state, View.GONE);
        RecyclerView recyclerView = holder.getView(R.id.itm_odr_recyclerview);
        goodsAdapter = new OrderConfirmGoodsAdapter(mContext, data.getGoodsList());
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(goodsAdapter);
        holder.setVisibility(R.id.itm_odr_bt_1, View.GONE);
        holder.setVisibility(R.id.itm_odr_bt_2, View.GONE);
        holder.setVisibility(R.id.itm_odr_tv_price, View.GONE);
        holder.setVisibility(R.id.itm_odr_tv_tag1, View.GONE);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_order;
    }

    public class OrderConfirmGoodsAdapter extends BaseAdapter<CartGoods> {
        public OrderConfirmGoodsAdapter(Context context, List<CartGoods> dataList) {
            super(context, dataList);
        }

        @Override
        protected void convert(ViewHolder holder, CartGoods data, int position) {
            ImageView imageView = holder.getView(R.id.itm_cfm_iv_goods_img);
            String url = Constants.RES_URL+data.getGoods().getPathList().get(0);
            GlideUtils.setImage(mContext,url, imageView);
            holder.setText(R.id.itm_cfm_tv_goods_name, data.getGoods().getName());
            holder.setText(R.id.itm_cfm_tv_goods_type, data.getGoods().getCategory());
            holder.setText(R.id.itm_cfm_tv_goods_price, String.valueOf(data.getGoods().getPrice()));
            holder.setText(R.id.itm_cfm_tv_goods_count, String.valueOf(data.getCount()));
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.item_confirm_goods;
        }
    }
}
