package cn.edu.neusoft.ypq.gowuu.customer.me.bean;

import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 收货地址实体类
 * @create 2022/3/8 - 13:47
 */
public class Address {
    private Integer aid;
    private Integer uid;
    private String name;
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String areaName;
    private String areaCode;
    private String detail;
    private String phone;
    private String tag;
    private Integer isDefault;

    public Address() {
    }

    public Address(Integer aid, Integer uid, String name, String provinceName, String provinceCode, String cityName, String cityCode, String areaName, String areaCode, String address, String phone, String tag, Integer isDefault) {
        this.aid = aid;
        this.uid = uid;
        this.name = name;
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.areaName = areaName;
        this.areaCode = areaCode;
        this.detail = address;
        this.phone = phone;
        this.tag = tag;
        this.isDefault = isDefault;
    }

    public void setAddress(Address address){
        this.aid = address.getAid();
        this.uid = address.getUid();
        this.name = address.getName();
        this.provinceName = address.getProvinceName();
        this.provinceCode = address.getProvinceCode();
        this.cityName = address.getCityName();
        this.cityCode = address.getCityCode();
        this.areaName = address.getAreaName();
        this.areaCode = address.getAreaCode();
        this.detail = address.getDetail();
        this.phone = address.getPhone();
        this.tag = address.getTag();
        this.isDefault = address.getIsDefault();
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Address{" +
                "aid=" + aid +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", areaName='" + areaName + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", address='" + detail + '\'' +
                ", phone='" + phone + '\'' +
                ", tag='" + tag + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Objects.equals(aid, address1.aid) && Objects.equals(uid, address1.uid) && Objects.equals(name, address1.name) && Objects.equals(provinceName, address1.provinceName) && Objects.equals(provinceCode, address1.provinceCode) && Objects.equals(cityName, address1.cityName) && Objects.equals(cityCode, address1.cityCode) && Objects.equals(areaName, address1.areaName) && Objects.equals(areaCode, address1.areaCode) && Objects.equals(detail, address1.detail) && Objects.equals(phone, address1.phone) && Objects.equals(tag, address1.tag) && Objects.equals(isDefault, address1.isDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aid, uid, name, provinceName, provinceCode, cityName, cityCode, areaName, areaCode, detail, phone, tag, isDefault);
    }
}
