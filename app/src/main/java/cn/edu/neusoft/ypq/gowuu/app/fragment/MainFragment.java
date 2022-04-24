package cn.edu.neusoft.ypq.gowuu.app.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.app.MainActivity;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.customer.cart.fragment.CartFragment;
import cn.edu.neusoft.ypq.gowuu.customer.classify.fragment.ClassifyFragment;
import cn.edu.neusoft.ypq.gowuu.customer.home.fragment.HomeFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.fragment.MeFragment;
import cn.edu.neusoft.ypq.gowuu.user.fragment.LoginFragment;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:MainFragment
 */
public class MainFragment extends BaseFragment<Void> {

    public static List<BaseFragment> fragmentList;

    @BindView(R.id.f_main_frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        fragmentList = new ArrayList<>();

        //监听RadioGroup
        initListener();
        return view;
    }

    //作用：监听RadioGroup点击事件，并根据点击位置更改Fragment
    private void initListener() {
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FragmentUtils.changeRbFragment(getActivity(), R.id.f_main_frameLayout, new HomeFragment());
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FragmentUtils.changeRbFragment(getActivity(), R.id.f_main_frameLayout, new ClassifyFragment());
                }
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.user.getUid() != null && rb3.isChecked()) {
                    FragmentUtils.changeRbFragment(getActivity(), R.id.f_main_frameLayout, new CartFragment());
                } else {
                    rb4.setChecked(true);
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    FragmentUtils.changeFragment(getActivity(), R.id.main_frameLayout, new LoginFragment());
                }
            }
        });

        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (MainActivity.user.getUid()!= null && isChecked){
                    FragmentUtils.changeRbFragment(getActivity(), R.id.f_main_frameLayout, new CartFragment());
                }
            }
        });

        rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FragmentUtils.changeRbFragment(getActivity(), R.id.f_main_frameLayout, new MeFragment());
                }
            }
        });
        rb1.setChecked(true);
    }
}
