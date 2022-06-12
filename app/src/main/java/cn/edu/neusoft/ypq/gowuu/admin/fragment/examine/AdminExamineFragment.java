package cn.edu.neusoft.ypq.gowuu.admin.fragment.examine;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:AdminExamineFragment
 */
public class AdminExamineFragment extends BaseFragment<Void> {

    @BindView(R.id.pages_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.pages_viewpager2)
    ViewPager2 viewPager2;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_pages, null);
        ButterKnife.bind(this, view);

        viewPager2.setAdapter(new FragmentStateAdapter((FragmentActivity) mContext) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0: return new ExamineApplyFragment();
                    case 1: return new ExamineReportFragment();
                    default: return null;
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("申请");
                    break;
                case 1:
                    tab.setText("举报");
                    break;
            }
        }).attach();

        return view;
    }
}
