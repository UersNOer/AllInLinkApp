package com.example.android_supervisor.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.adapter.SelectableAdapter;
import com.example.android_supervisor.ui.view.SelectableListToolBar;

import butterknife.BindView;

/**
 * @author wujie
 */
public class SelectableListFragment extends ListFragment {

    @BindView(R.id.selectable_list_toolbar)
    protected SelectableListToolBar mSelectToolBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_selectable_layout, container, false);
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
