package cn.edu.neusoft.ypq.gowuu.admin.fragment.manage;

import android.view.View;

import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:ManageUserFragment
 */
public class ManageUserFragment extends BaseFragment<Void> {
    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_rv, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
