package cn.edu.neusoft.ypq.gowuu.business.fragment.goods;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
 * 时间:2022/3/10
 * 功能:GoodsBrowseFragment
 */
public class BusinessGoodsFragment extends BaseFragment<Void> {
    public static boolean isEdit = false;

    @BindView(R.id.pages_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.pages_viewpager2)
    ViewPager2 viewPager2;
    @BindView(R.id.pages_flt_bt_add)
    FloatingActionButton btAdd;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_pages, null);
        ButterKnife.bind(this, view);
        btAdd.setVisibility(View.VISIBLE);
        viewPager2.setAdapter(new FragmentStateAdapter((FragmentActivity) mContext) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0: return new GoodsBrowseFragment();
                    case 1: return new GoodsManageFragment();
                    default: return null;
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("商品浏览");
                        break;
                    case 1:
                        tab.setText("商品管理");
                        break;
                }
            }
        }).attach();
        return view;
    }

    @OnClick(R.id.pages_flt_bt_add)
    public void addGoods(){
        isEdit = false;
        FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new GoodsEditFragment());
    }

}
