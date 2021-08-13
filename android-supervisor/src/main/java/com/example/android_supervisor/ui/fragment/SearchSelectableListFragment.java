package com.example.android_supervisor.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.adapter.SelectableAdapter;
import com.example.android_supervisor.ui.view.SelectableListToolBar;
import com.example.android_supervisor.utils.SystemUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * @author wujie
 */
public class SearchSelectableListFragment extends ListFragment {
    @BindView(R.id.et_recycler_view_search)
    protected EditText etSearch;

    @BindView(R.id.iv_recycler_view_search)
    protected ImageView ivSearch;

    @BindView(R.id.tv_recycler_view_info)
    protected TextView tvSearchInfo;

    @BindView(R.id.selectable_list_toolbar)
    protected SelectableListToolBar mSelectToolBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_search_selectable_layout, container, false);
    }

    @OnClick(R.id.iv_recycler_view_search)
    public void onSearch(View view) {
        SystemUtils.hideSoftInput(getContext(), view);
        String searchKey = etSearch.getText().toString().trim();
        onSearch(searchKey);
    }

    @OnEditorAction(R.id.et_recycler_view_search)
    public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String searchKey = etSearch.getText().toString().trim();
            onSearch(searchKey);
            return true;
        }
        return false;
    }

    public void onSearch(String searchKey) {
        // Todo
    }

    public String getSearchKey() {
        return etSearch.getText().toString();
    }

    public void setSearchKey(String searchKey) {
        etSearch.setText(searchKey);
    }

    public void setSearchInfo(String infoFormat, int number) {
        int numIndex = infoFormat.indexOf("%d");
        if (numIndex == -1) {
            tvSearchInfo.setText(infoFormat);
        } else {
            int lastLength = infoFormat.length() - numIndex - 2;
            String info = String.format(infoFormat, number);
            SpannableString text = new SpannableString(info);
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), numIndex,
                    info.length() - lastLength, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            tvSearchInfo.setText(text);
        }
    }

    public void setAdapter(SelectableAdapter adapter) {
        mSelectToolBar.bindAdapter(adapter);
        super.setAdapter(adapter);
    }

    public void showSelectToolBar() {
        mSelectToolBar.show();
    }

    public void hideSelectToolBar() {
        mSelectToolBar.hide();
    }

    public boolean isShowSelectToolBar() {
        return mSelectToolBar.isShowing();
    }

    @Override
    public final void onItemClick(RecyclerView parent, View view, int position, long id) {
        if (!mSelectToolBar.onItemClick(parent, view, position, id)) {
            onSelectableItemClick(parent, view, position, id);
        }
    }

    public void onSelectableItemClick(RecyclerView parent, View view, int position, long id) {
        // Todo
    }

    @Override
    public final boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
        if (!mSelectToolBar.onItemLongClick(parent, view, position, id)) {
            return onSelectableItemLongClick(parent, view, position, id);
        }
        return true;
    }

    public boolean onSelectableItemLongClick(RecyclerView parent, View view, int position, long id) {
        // Todo
        return false;
    }
}
