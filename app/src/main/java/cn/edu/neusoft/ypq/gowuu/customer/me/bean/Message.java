package cn.edu.neusoft.ypq.gowuu.customer.me.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 信息实体类
 * @create 2022/3/14 - 14:41
 */
public class Message implements Serializable {
    private Integer id;
    private Integer rid;
    private Integer uid;
    private Integer type;
    private Integer state;
    private String detail;
    private String date;

    public Message() {
    }

    public Message(Integer id, Integer rid, Integer uid, Integer type, Integer state, String detail, String date) {
        this.id = id;
        this.rid = rid;
        this.uid = uid;
        this.type = type;
        this.state = state;
        this.detail = detail;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(rid, message.rid) && Objects.equals(uid, message.uid) && Objects.equals(type, message.type) && Objects.equals(state, message.state) && Objects.equals(detail, message.detail) && Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rid, uid, type, state, detail, date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", rid=" + rid +
                ", uid=" + uid +
                ", type=" + type +
                ", state=" + state +
                ", detail='" + detail + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
