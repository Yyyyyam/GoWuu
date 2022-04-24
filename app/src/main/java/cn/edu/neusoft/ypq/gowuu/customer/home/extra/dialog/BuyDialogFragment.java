package cn.edu.neusoft.ypq.gowuu.customer.home.extra.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.business.bean.Goods;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.Cart;
import cn.edu.neusoft.ypq.gowuu.customer.cart.bean.CartGoods;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.goods.GoodsDetailFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.order.OrderConfirmFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.extra.order.OrderConfirmFrameFragment;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;
import cn.edu.neusoft.ypq.gowuu.utils.GlideUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/30
 * 功能:BuyDialogFragment
 */
public class BuyDialogFragment extends DialogFragment {
    private Goods goods;
    public static boolean state = false;
    private int buyCount = 1;

    @BindView(R.id.dialog_buy_tv_price)
    TextView tvPrice;
    @BindView(R.id.dialog_buy_tv_goods_count)
    TextView tvCount;
    @BindView(R.id.dialog_buy_tv_buy_count)
    TextView tvBuyCount;
    @BindView(R.id.dialog_buy_iv_goods)
    ImageView img;

    public BuyDialogFragment(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void onStart() {
        super.onStart();
        //将对话框外部（未被遮挡的部分）的背景设置为透明
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);//显示高度为60%
        params.height = height;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_buy, container);
        ButterKnife.bind(this, view);
        tvPrice.setText(String.valueOf(goods.getPrice()));
        tvCount.setText(String.valueOf(goods.getCount()));
        tvBuyCount.setText(String.valueOf(buyCount));
        String url = Constants.RES_URL+goods.getPathList().get(0);
        GlideUtils.setImage(getContext(), url, img);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Dialog dialog = new Dialog(getActivity(), R.style.BottomFragmentDialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @OnClick(R.id.dialog_buy_bt_add)
    public void addBuyCount(){
        if (buyCount<goods.getCount()){
            tvBuyCount.setText(String.valueOf(buyCount+=1));
        } else {
            Toast.makeText(getContext(), "购买数量不能超过库存", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.dialog_buy_bt_sub)
    public void subBuyCount() {
        if (buyCount > 1) {
            tvBuyCount.setText(String.valueOf(buyCount -= 1));
        } else {
            Toast.makeText(getContext(), "购买数量至少为1", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.dialog_buy_bt_buy)
    public void buy(){
        getDialog().dismiss();
        //将商品数据传输到提交订单界面
        OrderConfirmFragment.cart = new Cart();
        List<CartGoods> goodsList = new ArrayList<>();
        CartGoods cartGoods = new CartGoods();
        cartGoods.setGoods(goods);
        cartGoods.setCount(buyCount);
        goodsList.add(cartGoods);
        OrderConfirmFragment.cart.setGoodsList(goodsList);
        state = true;
        FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new OrderConfirmFrameFragment());
    }
}
