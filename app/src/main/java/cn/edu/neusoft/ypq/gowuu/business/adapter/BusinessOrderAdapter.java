package cn.edu.neusoft.ypq.gowuu.business.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseAdapter;
import cn.edu.neusoft.ypq.gowuu.base.ViewHolder;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.OrderGoods;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/31
 * 功能:OrderAdapter
 */
public class BusinessOrderAdapter extends BaseAdapter<Order> {
    public static final int NOT_PAY = 0;
    public static final int PAID_NOT_SEND = 1;
    public static final int SEND_NOT_RECEIVE = 2;
    public static final int RECEIVED = 3;

    public OrderGoodsAdapter goodsAdapter;

    public BusinessOrderAdapter(Context context, List<Order> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Order data, int position) {
        holder.setText(R.id.itm_odr_tv_business_name, data.getBznsName());
        switch (data.getState()){
            case NOT_PAY:
                holder.setText(R.id.itm_odr_tv_tag1, "需付款￥");
                holder.setVisibility(R.id.itm_odr_bt_1, View.VISIBLE);
                holder.setText(R.id.itm_odr_tv_order_state, "未付款");
                holder.setVisibility(R.id.itm_odr_bt_2, View.VISIBLE);
                holder.setText(R.id.itm_odr_bt_1, "取消订单");
                holder.setVisibility(R.id.itm_odr_bt_1, View.VISIBLE);
                holder.setText(R.id.itm_odr_bt_2, "更改价格");
                holder.setVisibility(R.id.itm_odr_bt_2, View.VISIBLE);

                holder.setOnClickListener(R.id.itm_odr_bt_1, v -> {
                    if (onOrderCancelListener != null) onOrderCancelListener.cancel(holder, data, position);
                });

                holder.setOnClickListener(R.id.itm_odr_bt_2, v -> {
                    if (onOrderChangePriceListener != null) onOrderChangePriceListener.changePrice(holder, data, position);
                });
                break;
            case PAID_NOT_SEND:
                holder.setText(R.id.itm_odr_tv_order_state, "未发货");
                holder.setVisibility(R.id.itm_odr_bt_1, View.GONE);
                holder.setText(R.id.itm_odr_bt_2, "发货");
                holder.setVisibility(R.id.itm_odr_bt_2, View.VISIBLE);
                holder.setOnClickListener(R.id.itm_odr_bt_2, v -> {
                    if (onOrderSendListener != null) onOrderSendListener.send(holder, data, position);
                });
                break;
            case SEND_NOT_RECEIVE:
                holder.setText(R.id.itm_odr_tv_order_state, "已发货");
                holder.setVisibility(R.id.itm_odr_bt_1, View.GONE);
                holder.setVisibility(R.id.itm_odr_bt_2, View.GONE);
                holder.setText(R.id.itm_odr_bt_2, "确认收货");
                break;
            case RECEIVED:
                holder.setText(R.id.itm_odr_tv_order_state, "已收货");
                holder.setVisibility(R.id.itm_odr_bt_1, View.GONE);
                holder.setVisibility(R.id.itm_odr_bt_2, View.GONE);
                break;
            default:
                holder.setText(R.id.itm_odr_bt_1, "取消订单");
                holder.setText(R.id.itm_odr_bt_2, "确认付款");
                break;
        }
        holder.setText(R.id.itm_odr_tv_price, String.valueOf(data.getPrice()));
        RecyclerView recyclerView = holder.getView(R.id.itm_odr_recyclerview);
        goodsAdapter = new OrderGoodsAdapter(mContext, data.getGoodsList(), position);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(goodsAdapter);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_order;
    }

    private OnOrderCancelListener onOrderCancelListener;

    public interface OnOrderCancelListener{
        void cancel(ViewHolder holder, Order order, int position);
    }

    public void cancel(OnOrderCancelListener onOrderCancelListener){
        this.onOrderCancelListener = onOrderCancelListener;
    }

    private OnOrderChangePriceListener onOrderChangePriceListener;

    public interface OnOrderChangePriceListener{
        void changePrice(ViewHolder holder, Order order, int position);
    }

    public void changePrice(OnOrderChangePriceListener onOrderChangePriceListener){
        this.onOrderChangePriceListener = onOrderChangePriceListener;
    }

    private OnOrderSendListener onOrderSendListener;

    public interface OnOrderSendListener{
        void send(ViewHolder holder, Order order, int position);
    }

    public void send(OnOrderSendListener onOrderSendListener){
        this.onOrderSendListener = onOrderSendListener;
    }

    public static class OrderGoodsAdapter extends BaseAdapter<OrderGoods> {

        private final Integer fPosition;

        public OrderGoodsAdapter(Context context, List<OrderGoods> dataList, int fPosition) {
            super(context, dataList);
            this.fPosition = fPosition;
        }

        @Override
        protected void convert(ViewHolder holder, OrderGoods data, int position) {
            ImageView img  = holder.getView(R.id.itm_odr_iv_goods_img);
            GlideUtils.setImage(mContext, Constants.RES_URL+data.getPic(), img);
            holder.setText(R.id.itm_odr_tv_goods_name, data.getName());
            holder.setText(R.id.itm_odr_tv_goods_price, String.valueOf(data.getPrice()));
            holder.setText(R.id.itm_odr_tv_goods_type, data.getCategory());
            holder.setText(R.id.itm_odr_tv_goods_count, String.valueOf(data.getCount()));
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.item_order_goods;
        }
    }
}
