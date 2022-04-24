package cn.edu.neusoft.ypq.gowuu.admin.fragment.statistic;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Examine;
import cn.edu.neusoft.ypq.gowuu.admin.bean.Statistic;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.utils.Constants;
import cn.edu.neusoft.ypq.gowuu.utils.PostMessage;
import cz.msebera.android.httpclient.Header;

/**
 * 作者:颜培琦
 * 时间:2022/3/10
 * 功能:AdminStatisticFragment
 */
public class AdminStatisticFragment extends BaseFragment<Void> {

    @BindView(R.id.admin_statistic_week_tv_dealt_count)
    TextView tvWeekDealtCount;
    @BindView(R.id.admin_statistic_week_tv_dealt_money)
    TextView tvWeekDealtMoney;
    @BindView(R.id.admin_statistic_week_tv_dealing_count)
    TextView tvWeekDealingCount;
    @BindView(R.id.admin_statistic_week_tv_dealing_money)
    TextView tvWeekDealingMoney;
    @BindView(R.id.admin_statistic_month_tv_dealt_count)
    TextView tvMonthDealtCount;
    @BindView(R.id.admin_statistic_month_tv_dealt_money)
    TextView tvMonthDealtMoney;
    @BindView(R.id.admin_statistic_month_tv_dealing_count)
    TextView tvMonthDealingCount;
    @BindView(R.id.admin_statistic_month_tv_dealing_money)
    TextView tvMonthDealingMoney;
    @BindView(R.id.admin_statistic_year_tv_dealt_count)
    TextView tvYearDealtCount;
    @BindView(R.id.admin_statistic_year_tv_dealt_money)
    TextView tvYearDealtMoney;
    @BindView(R.id.admin_statistic_year_tv_dealing_count)
    TextView tvYearDealingCount;
    @BindView(R.id.admin_statistic_year_tv_dealing_money)
    TextView tvYearDealingMoney;
    @BindView(R.id.admin_statistic_all_tv_dealt_count)
    TextView tvAllDealtCount;
    @BindView(R.id.admin_statistic_all_tv_dealt_money)
    TextView tvAllDealtMoney;
    @BindView(R.id.admin_statistic_all_tv_dealing_count)
    TextView tvAllDealingCount;
    @BindView(R.id.admin_statistic_all_tv_dealing_money)
    TextView tvAllDealingMoney;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_statistic, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        String url = Constants.ORDER_URL+"/get_statistic";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                Type type = new TypeToken<PostMessage<Statistic>>() {
                }.getType();
                PostMessage<Statistic> postMessage = new Gson().fromJson(response, type);
                if (postMessage.getMessage() == null){
                    bindData(postMessage.getData());
                } else {
                    Toast.makeText(mContext,postMessage.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext,"ExamineApplyFragment(99):请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bindData(Statistic statistic) {
        tvWeekDealtCount.setText("成交数量:"+statistic.getWeekDealtCount());
        tvWeekDealtMoney.setText("成交额度￥"+statistic.getWeekDealtPrice());
        tvWeekDealingCount.setText("交易中数量:"+statistic.getWeekDealingCount());
        tvWeekDealingMoney.setText("交易中额度￥"+statistic.getWeekDealingPrice());
        tvMonthDealtCount.setText("成交数量:"+statistic.getMonthDealtCount());
        tvMonthDealtMoney.setText("成交额度￥"+statistic.getMonthDealtPrice());
        tvMonthDealingCount.setText("交易中数量:"+statistic.getMonthDealingCount());
        tvMonthDealingMoney.setText("交易中额度￥"+statistic.getMonthDealingPrice());
        tvYearDealtCount.setText("成交数量:"+statistic.getYearDealtCount());
        tvYearDealtMoney.setText("成交额度￥"+statistic.getYearDealtPrice());
        tvYearDealingCount.setText("交易中数量:"+statistic.getYearDealingCount());
        tvYearDealingMoney.setText("交易中额度￥"+statistic.getYearDealingPrice());
        tvAllDealtCount.setText("成交数量:"+statistic.getTotalDealtCount());
        tvAllDealtMoney.setText("成交额度￥"+statistic.getTotalDealtPrice());
        tvAllDealingCount.setText("交易中数量:"+statistic.getTotalDealingCount());
        tvAllDealingMoney.setText("交易中额度￥"+statistic.getTotalDealingPrice());
    }
}
