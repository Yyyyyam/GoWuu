package cn.edu.neusoft.ypq.gowuu.business.fragment;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.bean.Business;
import cn.edu.neusoft.ypq.gowuu.business.fragment.home.BusinessHomeFragment;
import cn.edu.neusoft.ypq.gowuu.business.fragment.goods.BusinessGoodsFragment;
import cn.edu.neusoft.ypq.gowuu.business.fragment.order.BusinessOrderFragment;
import cn.edu.neusoft.ypq.gowuu.business.fragment.statistic.BusinessStasticFragment;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:BusinessFragment
 */
public class BusinessFragment extends BaseFragment<Void>{

    public static Business business = new Business();

    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;

    private final Drawable[] drawables = new Drawable[6];


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        //监听RadioGroup
        initListener();
        Log.d("business", "initView: "+business);
        return view;
    }

    //作用：监听RadioGroup点击事件，初始化图标并根据点击位置更改Fragment
    private void initListener(){
        drawables[0] = ContextCompat.getDrawable(mContext, R.drawable.business_ic_goods);
        drawables[1] = ContextCompat.getDrawable(mContext, R.drawable.business_ic_goods_selected);
        drawables[2] = ContextCompat.getDrawable(mContext, R.drawable.business_ic_order);
        drawables[3] = ContextCompat.getDrawable(mContext, R.drawable.business_ic_order_selected);
        drawables[4] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_statistic);
        drawables[5] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_statistic_selected);
        rb2.setText("商品");
        rb2.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
        rb3.setText("订单");
        rb3.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2], null, null);
        rb4.setText("统计");
        rb4.setCompoundDrawablesWithIntrinsicBounds(null, drawables[4], null, null);

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new BusinessHomeFragment());
                }
            }
        });
        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb2.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1], null, null);
                    FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new BusinessGoodsFragment());
                } else {
                    rb2.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
                }
            }
        });
        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb3.setCompoundDrawablesWithIntrinsicBounds(null, drawables[3], null, null);
                    FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new BusinessOrderFragment());
                } else {
                    rb3.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2], null, null);
                }
            }
        });
        rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb4.setCompoundDrawablesWithIntrinsicBounds(null, drawables[5], null, null);
                    FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new BusinessStasticFragment());
                } else {
                    rb4.setCompoundDrawablesWithIntrinsicBounds(null, drawables[4], null, null);
                }
            }
        });
        rb1.setChecked(true);
    }
}
