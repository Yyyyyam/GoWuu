package cn.edu.neusoft.ypq.gowuu.business.fragment.home;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.business.bean.Business;
import cn.edu.neusoft.ypq.gowuu.business.fragment.BusinessFragment;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:BusinessHomeFragment
 */
public class BusinessHomeFragment extends BaseFragment<Void> {
    @BindView(R.id.bzns_home_tv_title)
    TextView tvBznsName;
    @BindView(R.id.bzns_home_tv_score1)
    TextView tvScore1;
    @BindView(R.id.bzns_home_tv_score2)
    TextView tvScore2;
    @BindView(R.id.bzns_home_tv_score3)
    TextView tvScore3;
    @BindView(R.id.bzns_home_tv_violation)
    TextView tvViolation;
    @BindView(R.id.bzns_home_tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.bzns_home_tv_goods_discount)
    TextView tvDisCount;
    @BindView(R.id.bzns_home_tv_goods_state1)
    TextView tvState1;
    @BindView(R.id.bzns_home_tv_goods_state2)
    TextView tvState2;
    @BindView(R.id.bzns_home_tv_order_count)
    TextView tvOrderCount;
    @BindView(R.id.bzns_home_tv_order_finished)
    TextView tvOrderFinished;
    @BindView(R.id.bzns_home_tv_order_not_pay)
    TextView tvOrderNotPay;
    @BindView(R.id.bzns_home_tv_order_not_send)
    TextView tvOrderNotSend;
    @BindView(R.id.bzns_home_tv_statistic_finished)
    TextView tvStatisticFinished;
    @BindView(R.id.bzns_home_tv_statistic_not_finished)
    TextView tvStatisticNotFinished;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_bzns_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        initBusinessInfo();
    }

    private void initBusinessInfo() {
        tvBznsName.setText(BusinessFragment.business.getName());
        tvScore1.setText("描述相符:"+BusinessFragment.business.getScore1());
        tvScore2.setText("物流服务:"+BusinessFragment.business.getScore2());
        tvScore3.setText("服务态度:"+BusinessFragment.business.getScore3());
        tvViolation.setText("违规次数:"+BusinessFragment.business.getViolation());
        tvGoodsCount.setText("商品总数:"+BusinessFragment.business.getData().getGoodsNum());
        tvDisCount.setText("折扣商品:"+BusinessFragment.business.getData().getDiscountNum());
        tvState1.setText("上架商品:"+BusinessFragment.business.getData().getGoodsState1());
        tvState2.setText("下架商品:"+BusinessFragment.business.getData().getGoodsState2());
        tvOrderCount.setText("订单总数:"+BusinessFragment.business.getData().getOrderNum());
        tvOrderFinished.setText("收款订单:"+BusinessFragment.business.getData().getOrderFinished());
        tvOrderNotPay.setText("未支付订单:"+BusinessFragment.business.getData().getOrderNotPay());
        tvOrderNotSend.setText("未发货订单:"+BusinessFragment.business.getData().getOrderNotSend());
        tvStatisticFinished.setText("已入账￥"+BusinessFragment.business.getData().getEarnings());
        tvStatisticNotFinished.setText("未入账￥"+BusinessFragment.business.getData().getNotEarnings());
    }

    @OnClick(R.id.bzns_home_constraint_goods)
    public void toGoods(){
        RadioButton rbGoods = getActivity().findViewById(R.id.rb_2);
        rbGoods.setChecked(true);
    }

    @OnClick(R.id.bzns_home_constraint_order)
    public void toOrder(){
        RadioButton rbOrder = getActivity().findViewById(R.id.rb_3);
        rbOrder.setChecked(true);
    }

    @OnClick(R.id.bzns_home_constraint_statistic)
    public void toStatistic(){
        RadioButton rbStatistic = getActivity().findViewById(R.id.rb_4);
        rbStatistic.setChecked(true);
    }
}
