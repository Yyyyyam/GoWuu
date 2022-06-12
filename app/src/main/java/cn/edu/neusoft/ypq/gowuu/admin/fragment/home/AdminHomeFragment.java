package cn.edu.neusoft.ypq.gowuu.admin.fragment.home;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Admin;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.utils.CheckUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:AdminHomeFragment
 */
public class AdminHomeFragment extends BaseFragment<Void> {
    private final Admin admin;

    @BindView(R.id.admin_home_tv_user_count)
    TextView tvUserCount;
    @BindView(R.id.admin_home_tv_bzns_count)
    TextView tvBznsCount;
    @BindView(R.id.admin_home_tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.admin_home_tv_order_count)
    TextView tvOrderCount;
    @BindView(R.id.admin_home_tv_request_count)
    TextView tvRequestCount;
    @BindView(R.id.admin_home_tv_report_count)
    TextView tvReportCount;
    @BindView(R.id.admin_home_tv_unhandled_request_count)
    TextView tvRequestUnhandled;
    @BindView(R.id.admin_home_tv_unhandled_report_count)
    TextView tvReportUnhandled;
    @BindView(R.id.admin_home_tv_dealt_count)
    TextView tvDealtCount;
    @BindView(R.id.admin_home_tv_dealt_money)
    TextView tvDealtMoney;
    @BindView(R.id.admin_home_tv_dealing_count)
    TextView tvDealingCount;
    @BindView(R.id.admin_home_tv_dealing_money)
    TextView tvDealingMoney;

    public AdminHomeFragment(Admin admin){
        this.admin = admin;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_admin_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        initAdmin();
    }

    public void initAdmin(){
        tvUserCount.setText("用户数量:"+admin.getUserCount());
        tvBznsCount.setText("商家数量:"+admin.getBznsCount());
        tvGoodsCount.setText("商品数量:"+admin.getGoodsCount());
        tvOrderCount.setText("订单数量:"+admin.getOrderCount());
        tvRequestCount.setText("申请数量:"+admin.getRequestCount());
        tvRequestUnhandled.setText("未处理申请:"+admin.getRequestUnHandledCount());
        tvReportCount.setText("举报数量"+admin.getReportCount());
        tvReportUnhandled.setText("未处理举报:"+admin.getReportUnHandledCount());
        tvDealtCount.setText("成交数量:"+admin.getDealtCount());
        tvDealtMoney.setText("成交额度￥"+CheckUtils.doubleTrim(admin.getDealtMoney()));
        tvDealingCount.setText("交易中数量:"+admin.getDealingCount());
        tvDealingMoney.setText("交易中额度￥"+ CheckUtils.doubleTrim(admin.getDealingMoney()));
    }

    @OnClick(R.id.admin_home_constraint_request)
    public void toRequest(){
        RadioButton rbRequest = requireActivity().findViewById(R.id.rb_2);
        rbRequest.setChecked(true);
    }

    @OnClick(R.id.admin_home_constraint_statistic)
    public void toStatistic(){
        RadioButton rbStatistic = requireActivity().findViewById(R.id.rb_4);
        rbStatistic.setChecked(true);
    }
}
