package cn.edu.neusoft.ypq.gowuu.utils;

import com.google.gson.Gson;

/**
 * 作者:颜培琦
 * 时间:2022/3/3
 * 功能:接收通过网络获取的信息
 */
public class PostMessage<T> {
    private int state;
    private String message;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(String strJson) {
        new Gson().fromJson(strJson,data.getClass());
    }

    public PostMessage() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "PostMessage{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
