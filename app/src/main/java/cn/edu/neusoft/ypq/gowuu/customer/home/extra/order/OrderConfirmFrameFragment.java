package cn.edu.neusoft.ypq.gowuu.customer.home.extra.order;

import android.view.View;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.customer.me.bean.Order;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/30
 * 功能:OrderConfirmFrameFragment
 */
public class OrderConfirmFrameFragment extends BaseFragment<Void> {

    public static List<Order> orderList;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_order_confirm_frame, null);
        ButterKnife.bind(this, view);
        FragmentUtils.changeRbFragment(getActivity(), R.id.order_cfm_frame_layout, new OrderConfirmFragment());
        return view;
    }

    @OnClick(R.id.order_cfm_frame_ib_back)
    public void back(){
        OrderConfirmFragment.cart = null;
        FragmentUtils.popBack(getActivity());
    }

}
