package cn.edu.neusoft.ypq.gowuu.business.bean;

import java.util.Objects;

/**
 * @author yanpeiqi
 * @describe 商家实体类
 * @create 2022/3/13 - 18:14
 */
public class Business {
    private Integer bid;
    private Integer uid;
    private String name;
    private Integer violation;
    private Double score1;
    private Integer totalScore1;
    private Double score2;
    private Integer totalScore2;
    private Double score3;
    private Integer totalScore3;
    private Integer count;
    private BusinessData data;

    public Business() {
    }

    public Business(Integer bid, Integer uid, String name, Integer violation, Double score1, Integer totalScore1, Double score2, Integer totalScore2, Double score3, Integer totalScore3, Integer count) {
        this.bid = bid;
        this.uid = uid;
        this.name = name;
        this.violation = violation;
        this.score1 = score1;
        this.totalScore1 = totalScore1;
        this.score2 = score2;
        this.totalScore2 = totalScore2;
        this.score3 = score3;
        this.totalScore3 = totalScore3;
        this.count = count;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
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

    public Integer getViolation() {
        return violation;
    }

    public void setViolation(Integer violation) {
        this.violation = violation;
    }

    public Double getScore1() {
        return score1;
    }

    public void setScore1(Double score1) {
        this.score1 = score1;
    }

    public Double getScore2() {
        return score2;
    }

    public void setScore2(Double score2) {
        this.score2 = score2;
    }

    public Double getScore3() {
        return score3;
    }

    public void setScore3(Double score3) {
        this.score3 = score3;
    }

    public Integer getTotalScore1() {
        return totalScore1;
    }

    public void setTotalScore1(Integer totalScore1) {
        this.totalScore1 = totalScore1;
    }

    public Integer getTotalScore2() {
        return totalScore2;
    }

    public void setTotalScore2(Integer totalScore2) {
        this.totalScore2 = totalScore2;
    }

    public Integer getTotalScore3() {
        return totalScore3;
    }

    public void setTotalScore3(Integer totalScore3) {
        this.totalScore3 = totalScore3;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BusinessData getData() {
        return data;
    }

    public void setData(BusinessData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Business{" +
                "bid=" + bid +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", violation=" + violation +
                ", score1=" + score1 +
                ", totalScore1=" + totalScore1 +
                ", score2=" + score2 +
                ", totalScore2=" + totalScore2 +
                ", score3=" + score3 +
                ", totalScore3=" + totalScore3 +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
