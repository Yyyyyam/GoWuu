package cn.edu.neusoft.ypq.gowuu.admin.fragment.examine;

import android.view.View;

import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Examine;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:ExamineReportFragment
 */
public class ExamineReportFragment extends BaseFragment<Examine> {

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_for_rv, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
