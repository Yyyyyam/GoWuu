package cn.edu.neusoft.ypq.gowuu.admin.bean;

import java.io.Serializable;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/4/18 - 13:12
 */
public class Admin implements Serializable {
    private Integer userCount;
    private Integer bznsCount;
    private Integer goodsCount;
    private Integer orderCount;
    private Integer requestCount;
    private Integer reportCount;
    private Integer requestUnHandledCount;
    private Integer reportUnHandledCount;
    private Integer dealtCount;
    private Integer dealingCount;
    private Double dealtMoney;
    private Double dealingMoney;

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getBznsCount() {
        return bznsCount;
    }

    public void setBznsCount(Integer bznsCount) {
        this.bznsCount = bznsCount;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public Integer getRequestUnHandledCount() {
        return requestUnHandledCount;
    }

    public void setRequestUnHandledCount(Integer requestUnHandledCount) {
        this.requestUnHandledCount = requestUnHandledCount;
    }

    public Integer getReportUnHandledCount() {
        return reportUnHandledCount;
    }

    public void setReportUnHandledCount(Integer reportUnHandledCount) {
        this.reportUnHandledCount = reportUnHandledCount;
    }

    public Integer getDealtCount() {
        return dealtCount;
    }

    public void setDealtCount(Integer dealtCount) {
        this.dealtCount = dealtCount;
    }

    public Integer getDealingCount() {
        return dealingCount;
    }

    public void setDealingCount(Integer dealingCount) {
        this.dealingCount = dealingCount;
    }

    public Double getDealtMoney() {
        return dealtMoney;
    }

    public void setDealtMoney(Double dealtMoney) {
        this.dealtMoney = dealtMoney;
    }

    public Double getDealingMoney() {
        return dealingMoney;
    }

    public void setDealingMoney(Double dealingMoney) {
        this.dealingMoney = dealingMoney;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userCount=" + userCount +
                ", bznsCount=" + bznsCount +
                ", goodsCount=" + goodsCount +
                ", orderCount=" + orderCount +
                ", requestCount=" + requestCount +
                ", reportCount=" + reportCount +
                ", requestUnHandledCount=" + requestUnHandledCount +
                ", reportUnHandledCount=" + reportUnHandledCount +
                ", dealtCount=" + dealtCount +
                ", dealingCount=" + dealingCount +
                ", dealtMoney=" + dealtMoney +
                ", dealingMoney=" + dealingMoney +
                '}';
    }
}
