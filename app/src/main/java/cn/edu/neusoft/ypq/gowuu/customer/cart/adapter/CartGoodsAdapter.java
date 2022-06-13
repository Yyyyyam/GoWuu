package cn.edu.neusoft.ypq.gowuu.customer.cart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/28
 * 功能:购物车商品适配器
 */
public class CartGoodsAdapter extends RecyclerView.Adapter<CartGoodsAdapter.MyHolder> {
    private final Context mContext;
    private final List<CartGoods> goodsList;
    private final int fPosition;

    public CartGoodsAdapter(Context context, List<CartGoods> goodsList, int fPosition){
        this.mContext = context;
        this.goodsList = goodsList;
        this.fPosition = fPosition;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cstm_cart_goods,parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        goodsList.get(position).setPosition(position);
        holder.bind(goodsList.get(position));
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public List<CartGoods> getGoodsList(){
        return goodsList;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CartGoods cartGoods;

        private final TextView tvName;
        private final TextView tvCategory;
        private final TextView tvPrice;
        private final TextView tvCount;
        private final CheckBox cbSelect;
        private final ImageView btAdd;
        private final ImageView btSub;
        private final ImageView image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.cart_goods_tv_name);
            tvCategory = itemView.findViewById(R.id.cart_goods_tv_type);
            tvPrice = itemView.findViewById(R.id.cart_goods_tv_price);
            tvCount = itemView.findViewById(R.id.cart_goods_tv_count);
            cbSelect = itemView.findViewById(R.id.cart_goods_cb_select);
            btAdd = itemView.findViewById(R.id.cart_goods_bt_add);
            btSub = itemView.findViewById(R.id.cart_goods_bt_sub);
            image = itemView.findViewById(R.id.cart_goods_iv_pic);
        }

        public void bind(CartGoods data){
            cartGoods = data;
            tvName.setText(data.getGoods().getName());
            tvCategory.setText(data.getGoods().getCategory());
            tvPrice.setText(String.valueOf(CheckUtils.doubleTrim(data.getGoods().getPrice()*data.getGoods().getDiscount())));
            tvCount.setText(String.valueOf(data.getCount()));
            GlideUtils.setImage(mContext, Constants.RES_URL+data.getGoods().getPathList().get(0), image);
            cbSelect.setChecked(data.getSelect());

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null){
                    onItemClickListener.setOnItemClickListener(goodsList, v, fPosition, getAdapterPosition());
                }
            });

            cbSelect.setOnClickListener(v -> {
                if (onSelectListener != null){
                    onSelectListener.setOnSelectListener(cbSelect.isChecked(), v, fPosition, getAdapterPosition());
                }
            });

            btAdd.setOnClickListener(v -> {
                if (goodsList.get(getAdapterPosition()).getCount() < goodsList.get(getAdapterPosition()).getGoods().getCount()){
                    goodsList.get(getAdapterPosition()).setCount(goodsList.get(getAdapterPosition()).getCount()+1);
                    notifyItemChanged(getAdapterPosition());
                } else {
                    Toast.makeText(mContext, "数量已达到该商品最大库存", Toast.LENGTH_SHORT).show();
                }
                onCountChangeListener.setOnCountChangeListener(goodsList.get(getAdapterPosition()), v, fPosition, getAdapterPosition());
            });

            btSub.setOnClickListener(v -> {
                if (goodsList.get(getAdapterPosition()).getCount() > 1){
                    goodsList.get(getAdapterPosition()).setCount(goodsList.get(getAdapterPosition()).getCount()-1);
                    notifyItemChanged(getAdapterPosition());
                } else {
                    Toast.makeText(mContext, "购物车数量至少为1", Toast.LENGTH_SHORT).show();
                }
                onCountChangeListener.setOnCountChangeListener(goodsList.get(getAdapterPosition()), v, fPosition, getAdapterPosition());
            });
        }
    }

    private OnSelectListener onSelectListener;

    public interface OnSelectListener{
        void setOnSelectListener(boolean selected, View view, int fPosition, int position);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener){
        this.onSelectListener = onSelectListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void setOnItemClickListener(List<CartGoods> goodsList,View view, int fPosition, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private OnCountChangeListener onCountChangeListener;

    public interface  OnCountChangeListener {
        void setOnCountChangeListener(CartGoods goods, View view, int fPosition, int position);
    }

    public void setOnCountChangeListener(OnCountChangeListener onCountChangeListener){
        this.onCountChangeListener = onCountChangeListener;
    }
}