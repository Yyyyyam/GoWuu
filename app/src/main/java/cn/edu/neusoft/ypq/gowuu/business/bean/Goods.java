package cn.edu.neusoft.ypq.gowuu.business.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 商品实体类
 * @create 2022/3/13 - 10:43
 */
public class Goods implements Serializable {
    private Integer gid;
    private Integer bid;
    private String name;
    private Double price;
    private Integer count;
    private Integer cid1;
    private Integer cid2;
    private Integer cid3;
    private String category;
    private Integer state;
    private Double discount;
    private Integer sale;
    private Integer favorite;
    private List<String> pathList;

    public Goods() {
    }

    public Goods(Integer gid, Integer bid, String name, Double price, Integer count, Integer cid1, Integer cid2, Integer cid3, String category, Integer state, Double discount, Integer sale, Integer favorite, List<String> pathList) {
        this.gid = gid;
        this.bid = bid;
        this.name = name;
        this.price = price;
        this.count = count;
        this.cid1 = cid1;
        this.cid2 = cid2;
        this.cid3 = cid3;
        this.category = category;
        this.state = state;
        this.discount = discount;
        this.sale = sale;
        this.favorite = favorite;
        this.pathList = pathList;
    }

    public void setGoods(Goods goods){
        gid = goods.getGid();
        bid = goods.getBid();
        name = goods.getName();
        price = goods.getPrice();
        count = goods.getCount();
        cid1 = goods.getCid1();
        cid2 = goods.getCid2();
        cid3 = goods.getCid3();
        category = goods.getCategory();
        state = goods.getState();
        discount = goods.getDiscount();
        sale = goods.getSale();
        favorite = goods.getFavorite();
        pathList = goods.getPathList();
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCid1() {
        return cid1;
    }

    public void setCid1(Integer cid1) {
        this.cid1 = cid1;
    }

    public Integer getCid2() {
        return cid2;
    }

    public void setCid2(Integer cid2) {
        this.cid2 = cid2;
    }

    public Integer getCid3() {
        return cid3;
    }

    public void setCid3(Integer cid3) {
        this.cid3 = cid3;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return Objects.equals(gid, goods.gid) && Objects.equals(bid, goods.bid) && Objects.equals(name, goods.name) && Objects.equals(price, goods.price) && Objects.equals(count, goods.count) && Objects.equals(cid1, goods.cid1) && Objects.equals(cid2, goods.cid2) && Objects.equals(cid3, goods.cid3) && Objects.equals(category, goods.category) && Objects.equals(state, goods.state) && Objects.equals(discount, goods.discount) && Objects.equals(sale, goods.sale) && Objects.equals(favorite, goods.favorite) && Objects.equals(pathList, goods.pathList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gid, bid, name, price, count, cid1, cid2, cid3, category, state, discount, sale, favorite, pathList);
    }

    @Override
    public String toString() {
        return "Goods{" +
                "gid=" + gid +
                ", bid=" + bid +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", cid1=" + cid1 +
                ", cid2=" + cid2 +
                ", cid3=" + cid3 +
                ", category='" + category + '\'' +
                ", state=" + state +
                ", discount=" + discount +
                ", sale=" + sale +
                ", favorite=" + favorite +
                ", pathList=" + pathList +
                '}';
    }

}
