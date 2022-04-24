package cn.edu.neusoft.ypq.gowuu.customer.cart.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 作者:颜培琦
 * 时间:2022/3/29
 * 功能:Cart
 */
public class Cart implements Serializable {
    private Integer cid;
    private Integer uid;
    private Integer bid;
    private boolean select;
    private int position;
    private CartHeader header;
    private List<CartGoods> goodsList;

    public Cart() {
    }

    public Cart(CartHeader header, List<CartGoods> goodsList) {
        this.header = header;
        this.goodsList = goodsList;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CartHeader getHeader() {
        return header;
    }

    public void setHeader(CartHeader header) {
        this.header = header;
    }

    public List<CartGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CartGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return  Objects.equals(header, cart.header) && Objects.equals(goodsList, cart.goodsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, goodsList);
    }

    @Override
    public String toString() {
        return "Cart{" +
                ", header=" + header +
                ", goodsList=" + goodsList +
                '}';
    }
}

