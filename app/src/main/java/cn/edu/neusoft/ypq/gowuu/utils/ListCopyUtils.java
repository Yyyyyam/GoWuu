package cn.edu.neusoft.ypq.gowuu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 作者:颜培琦
 * 时间:2022/4/20
 * 功能:ListCopyUtils
 */
public class ListCopyUtils {
    /**
     * 深度复制-不影响原来的list
     * @return 返回一个全新的List
     */
    public static  <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
