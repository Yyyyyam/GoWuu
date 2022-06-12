package cn.edu.neusoft.ypq.gowuu.admin.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Admin;
import cn.edu.neusoft.ypq.gowuu.admin.fragment.examine.AdminExamineFragment;
import cn.edu.neusoft.ypq.gowuu.admin.fragment.home.AdminHomeFragment;
import cn.edu.neusoft.ypq.gowuu.admin.fragment.manage.AdminManageFragment;
import cn.edu.neusoft.ypq.gowuu.admin.fragment.statistic.AdminStatisticFragment;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:AdminFragment
 */
public class AdminFragment extends BaseFragment<Void> {

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
    private final Admin admin;

    public AdminFragment(Admin admin) {
        this.admin = admin;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        //监听RadioGroup
        initListener();
        return view;
    }

    //作用：监听RadioGroup点击事件，初始化图标并根据点击位置更改Fragment
    private void initListener(){
        drawables[0] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_examine);
        drawables[1] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_examine_selected);
        drawables[2] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_manage);
        drawables[3] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_manage_selected);
        drawables[4] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_statistic);
        drawables[5] = ContextCompat.getDrawable(mContext, R.drawable.admin_ic_statistic_selected);
        rb2.setText("审核");
        rb2.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
        rb3.setText("管理");
        rb3.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2], null, null);
        rb4.setText("统计");
        rb4.setCompoundDrawablesWithIntrinsicBounds(null, drawables[4], null, null);

        rb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new AdminHomeFragment(admin));
            }
        });

        rb2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                rb2.setCompoundDrawablesWithIntrinsicBounds(null, drawables[1], null, null);
                FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new AdminExamineFragment());
            } else {
                rb2.setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
            }
        });
        rb3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                rb3.setCompoundDrawablesWithIntrinsicBounds(null, drawables[3], null, null);
                FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new AdminManageFragment());
            } else {
                rb3.setCompoundDrawablesWithIntrinsicBounds(null, drawables[2], null, null);
            }
        });
        rb4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                rb4.setCompoundDrawablesWithIntrinsicBounds(null, drawables[5], null, null);
                FragmentUtils.changeRbFragment(requireActivity(), R.id.f_main_frameLayout, new AdminStatisticFragment());
            } else {
                rb4.setCompoundDrawablesWithIntrinsicBounds(null, drawables[4], null, null);
            }
        });
        rb1.setChecked(true);
    }
}
