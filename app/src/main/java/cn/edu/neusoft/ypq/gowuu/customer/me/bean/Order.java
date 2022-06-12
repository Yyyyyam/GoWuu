package cn.edu.neusoft.ypq.gowuu.customer.me.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 订单实体类
 * @create 2022/3/31 - 0:08
 */
public class Order implements Serializable {
    private Integer oid;
    private Integer uid;
    private Integer bid;
    private String recvName;
    private String recvPhone;
    private String recvProvince;
    private String recvCity;
    private String recvArea;
    private String recvAddress;
    private Double price;
    private Integer state;
    private String createTime;
    private String payTime;
    private String sendTime;
    private String receiveTime;
    private String bznsName;
    private List<OrderGoods> goodsList;
    private Integer position;

    public Order() {
    }

    public Order(Order order){
        this.oid = order.getOid();
        this.uid = order.getUid();
        this.bid = order.getBid();
        this.recvName = order.getRecvName();
        this.recvPhone = order.getRecvPhone();
        this.recvProvince = order.getRecvProvince();
        this.recvCity = order.getRecvCity();
        this.recvArea = order.getRecvArea();
        this.recvAddress = order.getRecvAddress();
        this.price = order.getPrice();
        this.state = order.getState();
        this.createTime = order.getCreateTime();
        this.payTime = order.getPayTime();
        this.goodsList = order.getGoodsList();
        this.sendTime = order.getSendTime();
        this.receiveTime  = order.getReceiveTime();
        this.bznsName = order.getBznsName();
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }

    public String getRecvPhone() {
        return recvPhone;
    }

    public void setRecvPhone(String recvPhone) {
        this.recvPhone = recvPhone;
    }

    public String getRecvProvince() {
        return recvProvince;
    }

    public void setRecvProvince(String recvProvince) {
        this.recvProvince = recvProvince;
    }

    public String getRecvCity() {
        return recvCity;
    }

    public void setRecvCity(String recvCity) {
        this.recvCity = recvCity;
    }

    public String getRecvArea() {
        return recvArea;
    }

    public void setRecvArea(String recvArea) {
        this.recvArea = recvArea;
    }

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getBznsName() {
        return bznsName;
    }

    public void setBznsName(String bznsName) {
        this.bznsName = bznsName;
    }

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(oid, order.oid) && Objects.equals(uid, order.uid) && Objects.equals(recvName, order.recvName) && Objects.equals(recvPhone, order.recvPhone) && Objects.equals(recvProvince, order.recvProvince) && Objects.equals(recvCity, order.recvCity) && Objects.equals(recvArea, order.recvArea) && Objects.equals(recvAddress, order.recvAddress) && Objects.equals(price, order.price) && Objects.equals(state, order.state) && Objects.equals(createTime, order.createTime) && Objects.equals(payTime, order.payTime) && Objects.equals(goodsList, order.goodsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid, uid, recvName, recvPhone, recvProvince, recvCity, recvArea, recvAddress, price, state, createTime, payTime, goodsList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", uid=" + uid +
                ", recvName='" + recvName + '\'' +
                ", recvPhone='" + recvPhone + '\'' +
                ", recvProvince='" + recvProvince + '\'' +
                ", recvCity='" + recvCity + '\'' +
                ", recvArea='" + recvArea + '\'' +
                ", recvAddress='" + recvAddress + '\'' +
                ", price=" + price +
                ", state=" + state +
                ", orderTime='" + createTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
