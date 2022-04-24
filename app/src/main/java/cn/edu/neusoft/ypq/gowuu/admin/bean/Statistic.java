package cn.edu.neusoft.ypq.gowuu.admin.bean;

import java.io.Serializable;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/4/20 - 13:25
 */
public class Statistic implements Serializable {
    Integer totalDealtCount;
    Double totalDealtPrice;
    Integer totalDealingCount;
    Double totalDealingPrice;
    Integer weekDealtCount;
    Double weekDealtPrice;
    Integer weekDealingCount;
    Double weekDealingPrice;
    Integer monthDealtCount;
    Double monthDealtPrice;
    Integer monthDealingCount;
    Double monthDealingPrice;
    Integer yearDealtCount;
    Double yearDealtPrice;
    Integer yearDealingCount;
    Double yearDealingPrice;

    public Integer getTotalDealtCount() {
        return totalDealtCount;
    }

    public void setTotalDealtCount(Integer totalDealtCount) {
        this.totalDealtCount = totalDealtCount;
    }

    public Double getTotalDealtPrice() {
        return totalDealtPrice;
    }

    public void setTotalDealtPrice(Double totalDealtPrice) {
        this.totalDealtPrice = totalDealtPrice;
    }

    public Integer getTotalDealingCount() {
        return totalDealingCount;
    }

    public void setTotalDealingCount(Integer totalDealingCount) {
        this.totalDealingCount = totalDealingCount;
    }

    public Double getTotalDealingPrice() {
        return totalDealingPrice;
    }

    public void setTotalDealingPrice(Double totalDealingPrice) {
        this.totalDealingPrice = totalDealingPrice;
    }

    public Integer getWeekDealtCount() {
        return weekDealtCount;
    }

    public void setWeekDealtCount(Integer weekDealtCount) {
        this.weekDealtCount = weekDealtCount;
    }

    public Double getWeekDealtPrice() {
        return weekDealtPrice;
    }

    public void setWeekDealtPrice(Double weekDealtPrice) {
        this.weekDealtPrice = weekDealtPrice;
    }

    public Integer getWeekDealingCount() {
        return weekDealingCount;
    }

    public void setWeekDealingCount(Integer weekDealingCount) {
        this.weekDealingCount = weekDealingCount;
    }

    public Double getWeekDealingPrice() {
        return weekDealingPrice;
    }

    public void setWeekDealingPrice(Double weekDealingPrice) {
        this.weekDealingPrice = weekDealingPrice;
    }

    public Integer getMonthDealtCount() {
        return monthDealtCount;
    }

    public void setMonthDealtCount(Integer monthDealtCount) {
        this.monthDealtCount = monthDealtCount;
    }

    public Double getMonthDealtPrice() {
        return monthDealtPrice;
    }

    public void setMonthDealtPrice(Double monthDealtPrice) {
        this.monthDealtPrice = monthDealtPrice;
    }

    public Integer getMonthDealingCount() {
        return monthDealingCount;
    }

    public void setMonthDealingCount(Integer monthDealingCount) {
        this.monthDealingCount = monthDealingCount;
    }

    public Double getMonthDealingPrice() {
        return monthDealingPrice;
    }

    public void setMonthDealingPrice(Double monthDealingPrice) {
        this.monthDealingPrice = monthDealingPrice;
    }

    public Integer getYearDealtCount() {
        return yearDealtCount;
    }

    public void setYearDealtCount(Integer yearDealtCount) {
        this.yearDealtCount = yearDealtCount;
    }

    public Double getYearDealtPrice() {
        return yearDealtPrice;
    }

    public void setYearDealtPrice(Double yearDealtPrice) {
        this.yearDealtPrice = yearDealtPrice;
    }

    public Integer getYearDealingCount() {
        return yearDealingCount;
    }

    public void setYearDealingCount(Integer yearDealingCount) {
        this.yearDealingCount = yearDealingCount;
    }

    public Double getYearDealingPrice() {
        return yearDealingPrice;
    }

    public void setYearDealingPrice(Double yearDealingPrice) {
        this.yearDealingPrice = yearDealingPrice;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "totalDealtCount=" + totalDealtCount +
                ", totalDealtPrice=" + totalDealtPrice +
                ", totalDealingCount=" + totalDealingCount +
                ", totalDealingPrice=" + totalDealingPrice +
                ", weekDealtCount=" + weekDealtCount +
                ", weekDealtPrice=" + weekDealtPrice +
                ", weekDealingCount=" + weekDealingCount +
                ", weekDealingPrice=" + weekDealingPrice +
                ", monthDealtCount=" + monthDealtCount +
                ", monthDealtPrice=" + monthDealtPrice +
                ", monthDealingCount=" + monthDealingCount +
                ", monthDealingPrice=" + monthDealingPrice +
                ", yearDealtCount=" + yearDealtCount +
                ", yearDealtPrice=" + yearDealtPrice +
                ", yearDealingCount=" + yearDealingCount +
                ", yearDealingPrice=" + yearDealingPrice +
                '}';
    }
}
