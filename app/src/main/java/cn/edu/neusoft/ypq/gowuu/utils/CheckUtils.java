package cn.edu.neusoft.ypq.gowuu.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者:颜培琦
 * 时间:2022/3/3
 * 功能:CheckUtils
 */
public class CheckUtils {
    public static boolean isPhone(String number) {
        Pattern p = Pattern.compile("^[1][34578][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String str = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static void startEmailCheck(EditText editText, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!isEmail(editText.getText().toString().trim())){
                    textView.setVisibility(View.VISIBLE);
                }else {
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }

    public static void startPhoneCheck(EditText editText, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!isPhone(editText.getText().toString().trim())){
                    textView.setVisibility(View.VISIBLE);
                }else {
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }

    public static String doubleToString(Double value) {
        BigDecimal b = BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP);
        return b.toString();
    }

    public static Double doubleTrim(Double value){
        BigDecimal b = new BigDecimal(value);
        value = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

}
