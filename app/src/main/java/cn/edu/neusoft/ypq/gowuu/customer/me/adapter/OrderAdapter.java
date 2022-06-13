package cn.edu.neusoft.ypq.gowuu.customer.me.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
public class OrderAdapter extends BaseAdapter<Order> {
    public static final int NOT_PAY = 0;
    public static final int PAID_NOT_SEND = 1;
    public static final int SEND_NOT_RECEIVE = 2;
    public static final int RECEIVED = 3;

    public List<OrderGoodsAdapter> goodsAdapterList = new ArrayList<>();

    public OrderAdapter(Context context, List<Order> dataList) {
        super(context, dataList);
    }

    @Override
    protected void convert(ViewHolder holder, Order data, int position) {
        holder.setText(R.id.itm_odr_tv_business_name, data.getBznsName());
        switch (data.getState()){
            case NOT_PAY:
                holder.setVisibility(R.id.itm_odr_bt_1, View.VISIBLE);
                holder.setVisibility(R.id.itm_odr_bt_2, View.VISIBLE);
                holder.setText(R.id.itm_odr_tv_tag1, "需付款￥");
                holder.setText(R.id.itm_odr_tv_order_state, "未付款");
                holder.setText(R.id.itm_odr_bt_1, "取消订单");
                holder.setText(R.id.itm_odr_bt_2, "确认付款");
                holder.setOnClickListener(R.id.itm_odr_bt_1, v -> {
                    if (onOrderCancelListener != null)
                        onOrderCancelListener.cancel(holder, data, position);
                });

                holder.setOnClickListener(R.id.itm_odr_bt_2, v -> {
                    if (onOrderPayListener != null)
                        onOrderPayListener.pay(holder, data, position);
                });
                break;
            case PAID_NOT_SEND:
                holder.setText(R.id.itm_odr_tv_order_state, "未发货");
                holder.setVisibility(R.id.itm_odr_bt_1, View.GONE);
                holder.setText(R.id.itm_odr_bt_2, "取消订单");
                holder.setVisibility(R.id.itm_odr_bt_2, View.VISIBLE);
                holder.setTextColor(R.id.itm_odr_bt_2, R.color.gray);
                holder.setBackground(R.id.itm_odr_bt_2, R.drawable.item_shape_fillet_gray);
                holder.setOnClickListener(R.id.itm_odr_bt_2, v -> {
                    if (onOrderCancelListener != null)
                        onOrderCancelListener.cancel(holder, data, position);
                });
                break;
            case SEND_NOT_RECEIVE:
                holder.setText(R.id.itm_odr_tv_order_state, "未收货");
                holder.setVisibility(R.id.itm_odr_bt_1, View.GONE);
                holder.setText(R.id.itm_odr_bt_2, "确认收货");
                holder.setVisibility(R.id.itm_odr_bt_2, View.VISIBLE);
                holder.setOnClickListener(R.id.itm_odr_bt_2, v -> {
                    if (onOrderReceiveListener != null)
                        onOrderReceiveListener.receive(holder, data, position);
                });
                break;
            case RECEIVED:
                holder.setText(R.id.itm_odr_tv_order_state, "已完成");
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
        OrderGoodsAdapter goodsAdapter = new OrderGoodsAdapter(mContext, data.getGoodsList(), position);
        if (goodsAdapterList.size() < position+1)
            goodsAdapterList.add(goodsAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(goodsAdapterList.get(position));
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

    private OnOrderPayListener onOrderPayListener;

    public interface OnOrderPayListener{
        void pay(ViewHolder holder, Order order, int position);
    }

    public void pay(OnOrderPayListener onOrderPayListener){
        this.onOrderPayListener = onOrderPayListener;
    }

    OnOrderReceiveListener onOrderReceiveListener;

    public interface OnOrderReceiveListener{
        void receive(ViewHolder holder, Order order, int position);
    }

    public void receive(OnOrderReceiveListener onOrderReceiveListener){
        this.onOrderReceiveListener = onOrderReceiveListener;
    }

    public class OrderGoodsAdapter extends BaseAdapter<OrderGoods> {

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
            if (data.getState() == 1){
                holder.setVisibility(R.id.itm_odr_bt_appraise, View.VISIBLE);
                holder.setOnClickListener(R.id.itm_odr_bt_appraise, v ->
                        onGoodsEvaluationListener.evaluation(holder, data, fPosition, position));
            }
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.item_order_goods;
        }


    }

    private OnGoodsEvaluationListener onGoodsEvaluationListener;

    public interface OnGoodsEvaluationListener{
        void evaluation(ViewHolder holder, OrderGoods goods, int fPosition, int position);
    }

    public void evaluation(OnGoodsEvaluationListener onGoodsEvaluationListener){
        this.onGoodsEvaluationListener = onGoodsEvaluationListener;
    }
}
