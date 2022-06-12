package cn.edu.neusoft.ypq.gowuu.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * 作者:颜培琦
 * 时间:2022/3/12
 * 功能:FragmentUtils
 */
public class FragmentUtils {
    public static void changeFragment(FragmentActivity activity, int res, Fragment fragment){
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(res, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void popBack(FragmentActivity activity){
        activity
                .getSupportFragmentManager()
                .popBackStack();
    }

    public static void changeRbFragment(FragmentActivity activity, int res, Fragment fragment){
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(res, fragment)
                .commit();
    }

    public static void removeFragment(FragmentActivity activity, Fragment fragment){
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment);
    }
}
