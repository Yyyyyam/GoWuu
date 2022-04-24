package cn.edu.neusoft.ypq.gowuu.customer.cart.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 购物车的实体类
 * @create 2022/3/8 - 15:37
 */
public class CartHeader implements Serializable {
    private String bznsName;

    public CartHeader() {
    }

    public CartHeader(String bznsName, boolean isSelect) {
        this.bznsName = bznsName;
    }

    public String getBznsName() {
        return bznsName;
    }

    public void setBznsName(String bznsName) {
        this.bznsName = bznsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartHeader that = (CartHeader) o;
        return Objects.equals(bznsName, that.bznsName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bznsName);
    }

    @Override
    public String toString() {
        return "CartHeader{" +
                "bznsName='" + bznsName + '\'' +
                '}';
    }
}
