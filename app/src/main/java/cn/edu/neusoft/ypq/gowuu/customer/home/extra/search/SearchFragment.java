package cn.edu.neusoft.ypq.gowuu.customer.home.extra.search;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.neusoft.ypq.gowuu.R;
import cn.edu.neusoft.ypq.gowuu.base.BaseFragment;
import cn.edu.neusoft.ypq.gowuu.utils.FragmentUtils;

/**
 * 作者:颜培琦
 * 时间:2022/3/4
 * 功能:SearchFragment
 */
public class SearchFragment extends BaseFragment<String> {

    public static String searchString;

    @BindView(R.id.search_et_goods_name)
    EditText etName;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_cstm_search, null);
        ButterKnife.bind(this, view);
        Log.e("TAG", "SearchFragment的UI被初始化了");

        if (searchString != null){
            etName.setText(searchString);
            etName.setSelection(searchString.length());
        }

        //自动弹出软键盘
        new Handler().postDelayed(() -> {
            etName.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
        }, 100);
        //键盘输入的搜索监听
        etName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            // 按下按钮，这里和xml文件中的EditText中属性imeOptions对应;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return false;
            }
            return true;//返回true，保留软键盘;false，隐藏软键盘
        });
        return view;
    }

    @OnClick(R.id.search_ib_back)
    public void back(){
        searchString = null;
        hideKeyBoard(requireActivity());
        FragmentUtils.removeFragment(requireActivity(), this);
        FragmentUtils.popBack(requireActivity());

    }

    @OnClick(R.id.search_bt_search)
    public void search(){
        if (!etName.getText().toString().isEmpty()) {
            SearchResultFragment.searchName = etName.getText().toString().trim();
            FragmentUtils.changeRbFragment(requireActivity(), R.id.main_frameLayout, new SearchResultFragment());
        } else {
            Toast.makeText(mContext, "请输入搜索信息", Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
