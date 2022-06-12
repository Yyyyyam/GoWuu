package cn.edu.neusoft.ypq.gowuu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 作者:颜培琦
 * 时间:2022/6/12
 * 功能:OrderStateChangeReceiver
 */
public class OrderChangeReceiver extends BroadcastReceiver {


    public static String ORDER_STATE_CANCELED = "cn.edu.neusoft.ypq.gowuu.CANCELED";
    public static String ORDER_PRICE_MODIFIED = "cn.edu.neusoft.ypq.gowuu.PRICE_MODIFIED";
    public static String ORDER_STATE_PAID = "cn.edu.neusoft.ypq.gowuu.PAID";
    public static String ORDER_STATE_SEND = "cn.edu.neusoft.ypq.gowuu.SEND";
    public static String ORDER_STATE_RECEIVED = "cn.edu.neusoft.ypq.gowuu.RECEIVED";
    public static String ORDER_STATE_EVALUATION = "cn.edu.neusoft.ypq.gowuu.EVALUATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ORDER_STATE_CANCELED.equals(intent.getAction())) {
            canceledListener.canceled(intent);
        } else if (ORDER_PRICE_MODIFIED.equals(intent.getAction())) {
           modifiedListener.modify(intent);
        } else if (ORDER_STATE_PAID.equals(intent.getAction())) {
            paidListener.paid(intent);
        } else if (ORDER_STATE_SEND.equals(intent.getAction())) {
            sendListener.send(intent);
        } else if (ORDER_STATE_RECEIVED.equals(intent.getAction())) {
            receivedListener.received(intent);
        }
    }
    // 订单付款状态
    private PaidListener paidListener;
    public interface PaidListener{
        void paid(Intent intent);
    }
    public void paid(PaidListener paidListener) {
        this.paidListener = paidListener;
    }

    // 订单修改价格状态
    private PriceModifiedListener modifiedListener;
    public interface PriceModifiedListener{
        void modify(Intent intent);
    }
    public void modify(PriceModifiedListener modifiedListener) {
        this.modifiedListener = modifiedListener;
    }

    // 订单发货状态
    private SendListener sendListener;
    public interface SendListener{
        void send(Intent intent);
    }
    public void send(SendListener sendListener) {
        this.sendListener = sendListener;
    }

    // 订单收货状态
    private ReceivedListener receivedListener;
    public interface ReceivedListener{
        void received(Intent intent);
    }
    public void received(ReceivedListener receivedListener) {
        this.receivedListener = receivedListener;
    }

    // 订单取消状态
    private CanceledListener canceledListener;
    public interface CanceledListener{
        void canceled(Intent intent);
    }
    public void canceled(CanceledListener canceledListener) {
        this.canceledListener = canceledListener;
    }

    // 订单评价状态
    private EvaluationListener evaluationListener;
    public interface EvaluationListener{
        void evaluation(Intent intent);
    }
    public void evaluation(EvaluationListener evaluationListener) {
        this.evaluationListener = evaluationListener;
    }
}
