package cn.edu.neusoft.ypq.gowuu.customer.cart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.Cart;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.ListCopyUtils;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/28
 * 功能:购物车适配器
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    private Context mContext;
    private List<Cart> cartList;
    private boolean selectAll;
    public List<CartGoodsAdapter> goodsAdapterList = new ArrayList<>();

    public CartAdapter(Context context, List<Cart> cartList){
        this.mContext = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cstm_cart,parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        cartList.get(position).setPosition(position);
        holder.bind(holder,cartList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void addMoreData(List<Cart> list){
        int position = cartList.size();
        cartList.addAll(position, list);
        notifyItemRangeInserted(position, list.size());
        notifyItemRangeChanged(position, cartList.size() - position);
//        notifyDataSetChanged();
    }

    public List<Cart> getCartList(){
        return cartList;
    }

    public List<Cart> getSelectCart(){
        List<Cart> cartSelect = new ArrayList<>();
        for (int i = 0; i< cartList.size(); i++){
            Cart cart = cartList.get(i);
            if (cart.isSelect()){
                cartSelect.add(cart);
            }
        }
        return cartSelect;
    }

    public List<CartGoods> getSelectGoods(){
        List<CartGoods> goodsSelect = new ArrayList<>();
        List<Cart> temp = new ArrayList<>(cartList);
        temp.removeAll(getSelectCart());
        for (Cart cart: temp){
            for (int i=0; i<cart.getGoodsList().size(); i++){
                CartGoods goods = cart.getGoodsList().get(i);
                if (goods.getSelect()){
                    goodsSelect.add(goods);
                }
            }
        }
        return goodsSelect;
    }

    public void removeSelect(){
        List<Cart> cartRemove = new ArrayList<>();
        for (Cart cart : cartList){
            int p1 = cartList.indexOf(cart);
            if (cart.isSelect()){
                cartRemove.add(cart);
            } else {
                List<CartGoods> goodsList = cart.getGoodsList();
                List<CartGoods> goodsRemove = new ArrayList<>();
                for (CartGoods goods : goodsList){
                    if (goods.getSelect()){
                        goodsRemove.add(goods);
                    }
                }
                for (int i=0; i<goodsRemove.size(); i++) {
                    CartGoods goods = goodsRemove.get(i);
                    int p = goodsList.indexOf(goods);
                    goodsList.remove(goods);
                    goodsAdapterList.get(p1).notifyItemRemoved(p);
                }
            }
        }
        for (int i=0; i<cartRemove.size(); i++) {
            Cart cart = cartRemove.get(i);
            int p = cartList.indexOf(cart);
            cartList.remove(cart);
            notifyItemRemoved(p);
            goodsAdapterList.remove(p);
        }
    }

    public List<Cart> getConfirmCart(){
        int removeCarts = 0;
        List<Cart> cartConfirm = ListCopyUtils.deepCopy(cartList);
        for (int i=0; i<cartConfirm.size()+removeCarts; i++) {
            Cart cart = cartConfirm.get(i-removeCarts);
            if (!cart.isSelect()){
                int removeGoods = 0;
                List<CartGoods> goodsList = cart.getGoodsList();
                for (int j=0; j<goodsList.size()+removeGoods; j++){
                    CartGoods goods = goodsList.get(j-removeGoods);
                    if (!goods.getSelect()){
                        removeGoods+=1;
                        goodsList.remove(goods);
                    }
                }
                if (goodsList.size() == 0){
                    removeCarts+=1;
                    cartConfirm.remove(cart);
                }
            }
        }
        return cartConfirm;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        Cart cart;
        RecyclerView recyclerView;
        TextView tvName;
        CheckBox cbSelect;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.itm_cart_tv_business_name);
            cbSelect = itemView.findViewById(R.id.itm_cart_cb_select_all);
            recyclerView = itemView.findViewById(R.id.itm_cart_recyclerview);
        }

        public void bind(MyHolder holder, Cart data, int position){
            cart = data;
            cbSelect.setChecked(cart.isSelect());
            tvName.setText(data.getHeader().getBznsName());
            int p = cartList.indexOf(data);
            if (goodsAdapterList.size() < position+1)
                goodsAdapterList.add(new CartGoodsAdapter(mContext, cart.getGoodsList(), p));
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(goodsAdapterList.get(p));

            cbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectAllItemListener.setOnSelectAllItemListener(cbSelect.isChecked(), v, getAdapterPosition());
                }
            });

            // 购物车商家商品的选中监听
            goodsAdapterList.get(data.getPosition()).setOnSelectListener(new CartGoodsAdapter.OnSelectListener() {
                @Override
                public void setOnSelectListener(boolean selected, View view, int fPosition, int position) {
                    cartList.get(fPosition).getGoodsList().get(position).setSelect(selected);
                    int length = cartList.get(fPosition).getGoodsList().size();
                    if (length == 1) {
                        selectAllItemListener.setOnSelectAllItemListener(selected, view, fPosition);
                    } else {
                        for (int i = 0; i < length; i++) {
                            if (cartList.get(fPosition).getGoodsList().get(i).getSelect()) {//true,true,true
                                selectAll = true;
                            } else {
                                selectAll = false;
                                break;
                            }
                        }

                        cartList.get(fPosition).setSelect(selectAll);
                        onMoneyChangeListener.setOnMoneyChangeListener(view, fPosition);
                        onBznsSelectListener.setOnBznsSelectListener(cartList, view, fPosition, position);
                        notifyItemChanged(fPosition);
                    }
                }
            });

            goodsAdapterList.get(data.getPosition()).setOnCountChangeListener(new CartGoodsAdapter.OnCountChangeListener() {
                @Override
                public void setOnCountChangeListener(CartGoods goods, View view, int fPosition, int position) {
                    onMoneyChangeListener.setOnMoneyChangeListener(view, fPosition);
                }
            });

            goodsAdapterList.get(data.getPosition()).setOnItemClickListener(new CartGoodsAdapter.OnItemClickListener() {
                @Override
                public void setOnItemClickListener(List<CartGoods> goodsList, View view, int fPosition, int position) {
                    onChildSelectListener.setOnChildSelectListener(cartList, view, holder, fPosition, position);
                }
            });
        }
    }

    private OnSelectAllItemListener selectAllItemListener;

    public interface OnSelectAllItemListener{
        void setOnSelectAllItemListener(boolean selected, View view, int position);
    }

    public void setOnSelectAllListener(OnSelectAllItemListener selectAllItemListener){
        this.selectAllItemListener = selectAllItemListener;
    }

    public OnMoneyChangeListener onMoneyChangeListener;

    public interface OnMoneyChangeListener{
        void setOnMoneyChangeListener(View view, int position);
    }

    public void setOnMoneyChangeListener(OnMoneyChangeListener onMoneyChangeListener){
        this.onMoneyChangeListener = onMoneyChangeListener;
    }

    private OnChildSelectListener onChildSelectListener;

    public interface OnChildSelectListener{
        void setOnChildSelectListener(List<Cart> cartList, View view, CartAdapter.MyHolder holder, int fPosition, int posiotn);
    }

    public void setOnChildSelectListener(OnChildSelectListener onChildSelectListener){
        this.onChildSelectListener = onChildSelectListener;
    }

    private OnBznsSelectListener onBznsSelectListener;

    public interface OnBznsSelectListener{
        void setOnBznsSelectListener(List<Cart> cartList, View view, int fPosition, int posiotn);
    }

    public void setOnBznsSelectListener(OnBznsSelectListener onBznsSelectListener){
        this.onBznsSelectListener = onBznsSelectListener;
    }


    public interface OnRemoveCartSelecter{
        void remove(List<Cart> cartList);
    }
}
