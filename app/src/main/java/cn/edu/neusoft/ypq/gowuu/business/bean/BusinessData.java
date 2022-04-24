package cn.edu.neusoft.ypq.gowuu.business.bean;

import java.io.Serializable;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/4/15 - 9:34
 */
public class BusinessData implements Serializable {
    private Integer goodsNum;
    private Integer discountNum;
    private Integer goodsState1;
    private Integer goodsState2;
    private Integer orderNum;
    private Integer orderFinished;
    private Integer orderNotPay;
    private Integer orderNotSend;
    private Double earnings;
    private Double notEarnings;

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(Integer discountNum) {
        this.discountNum = discountNum;
    }

    public Integer getGoodsState1() {
        return goodsState1;
    }

    public void setGoodsState1(Integer goodsState1) {
        this.goodsState1 = goodsState1;
    }

    public Integer getGoodsState2() {
        return goodsState2;
    }

    public void setGoodsState2(Integer goodsState2) {
        this.goodsState2 = goodsState2;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderFinished() {
        return orderFinished;
    }

    public void setOrderFinished(Integer orderFinished) {
        this.orderFinished = orderFinished;
    }

    public Integer getOrderNotPay() {
        return orderNotPay;
    }

    public void setOrderNotPay(Integer orderNotPay) {
        this.orderNotPay = orderNotPay;
    }

    public Integer getOrderNotSend() {
        return orderNotSend;
    }

    public void setOrderNotSend(Integer orderNotSend) {
        this.orderNotSend = orderNotSend;
    }

    public Double getEarnings() {
        return earnings;
    }

    public void setEarnings(Double earnings) {
        this.earnings = earnings;
    }

    public Double getNotEarnings() {
        return notEarnings;
    }

    public void setNotEarnings(Double notEarnings) {
        this.notEarnings = notEarnings;
    }

    @Override
    public String toString() {
        return "BusinessData{" +
                "goodsNum=" + goodsNum +
                ", discountNum=" + discountNum +
                ", goodsState1=" + goodsState1 +
                ", goodsState2=" + goodsState2 +
                ", orderNum=" + orderNum +
                ", orderFinished=" + orderFinished +
                ", orderNotPay=" + orderNotPay +
                ", orderNotSend=" + orderNotSend +
                ", earnings=" + earnings +
                ", notEarnings=" + notEarnings +
                '}';
    }
}
