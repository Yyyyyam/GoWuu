package cn.edu.neusoft.ypq.gowuu.customer.me.extra.change_info;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;

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
import cn.edu.neusoft.ypq.gowuu.user.bean.User;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:ChangeUsrFragment
 */
public class ChangeUsrFragment extends BaseFragment {

    private int position;
    private User user;
    private boolean isManage;

    @BindView(R.id.usr_info_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.usr_info_viewpager)
    ViewPager2 viewPager2;

    public ChangeUsrFragment(int position, User user, boolean isManage){
        this.position = position;
        this.user = user;
        this.isManage = isManage;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_info, null);
        ButterKnife.bind(this, view);
        viewPager2.setAdapter(new FragmentStateAdapter((FragmentActivity) mContext) {
            @Override
            public androidx.fragment.app.Fragment createFragment(int position) {
                switch (position){
                    case 0: return new ChangeInfoFragment(user, isManage);
                    case 1: return new ChangePasFragment(user, isManage);
                    case 2: return new ChangeAvatarFragment(user, isManage);
                    default: return null;
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("修改资料");
                        break;
                    case 1:
                        tab.setText("修改密码");
                        break;
                    case 2:
                        tab.setText("修改头像");
                        break;
                }
            }
        }).attach();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tabLayout.getTabAt(position).select();
            }
        },100);
        return view;
    }

    @OnClick(R.id.usr_info_ib_back)
    public void back(){
        FragmentUtils.popBack(getActivity());
    }
}
