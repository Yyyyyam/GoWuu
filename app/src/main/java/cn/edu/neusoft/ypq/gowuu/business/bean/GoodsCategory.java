package cn.edu.neusoft.ypq.gowuu.business.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe
 * @create 2022/3/13 - 10:43
 */
public class GoodsCategory implements Serializable {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer status;
    private Integer sortOrder;
    private Integer isParent;

    public GoodsCategory() {
    }

    public GoodsCategory(Integer id, Integer parentId, String name, Integer status, Integer sortOrder, Integer isParent) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.status = status;
        this.sortOrder = sortOrder;
        this.isParent = isParent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsCategory that = (GoodsCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(name, that.name) && Objects.equals(status, that.status) && Objects.equals(sortOrder, that.sortOrder) && Objects.equals(isParent, that.isParent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name, status, sortOrder, isParent);
    }

    @Override
    public String toString() {
        return "GoodsCategory{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", sortOrder=" + sortOrder +
                ", isParent=" + isParent +
                '}';
    }
}
