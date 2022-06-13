package cn.edu.neusoft.ypq.gowuu.customer.me.extra.order;

import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/8
 * 功能:UsrOrderFragment
 */
public class UsrOrderFragment extends BaseFragment<Void> {

    private int position = 0;

    @BindView(R.id.usr_odr_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.usr_odr_viewpager)
    ViewPager2 viewPager2;

    public UsrOrderFragment() {
    }

    public UsrOrderFragment(int position){
        this.position = position;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_order, null);
        ButterKnife.bind(this, view);

        viewPager2.setOffscreenPageLimit(1);
        viewPager2.setAdapter(new FragmentStateAdapter((FragmentActivity) mContext) {
            @NonNull
            @Override
            public androidx.fragment.app.Fragment createFragment(int position) {
                switch (position){
                    case 0: return new OrderAll();
                    case 1: return new OrderNotPay();
                    case 2: return new OrderNotSend();
                    case 3: return new OrderNotReceive();
                    case 4: return new OrderReceived();
                    default: return new OrderAll();
                }
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("全部");
                    break;
                case 1:
                    tab.setText("待付款");
                    break;
                case 2:
                    tab.setText("待发货");
                    break;
                case 3:
                    tab.setText("待收货");
                    break;
                case 4:
                    tab.setText("已完成");
                    break;
            }
        }).attach();

        new Handler().postDelayed(() -> tabLayout.getTabAt(position).select(),100);
        return view;
    }

    @OnClick(R.id.usr_odr_ib_back)
    public void back(){
        FragmentUtils.popBack(requireActivity());
    }
}
