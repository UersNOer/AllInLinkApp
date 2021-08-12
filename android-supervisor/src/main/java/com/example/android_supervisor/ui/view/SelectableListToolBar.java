package com.example.android_supervisor.ui.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.adapter.SelectableAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wujie
 */
public class SelectableListToolBar extends FrameLayout {
    @BindView(R.id.ll_float_toolbar_delete)
    LinearLayout llDelete;

    @BindView(R.id.ll_float_toolbar_select)
    LinearLayout llSelect;

    @BindView(R.id.llReport)
    LinearLayout llReport;
    @BindView(R.id.ivReport)
    ImageView ivReport;
    @BindView(R.id.tvReport)
    TextView tvReport;

    private SelectableAdapter mAdapter;

    public SelectableListToolBar(Context context) {
        this(context, null);
    }

    public SelectableListToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.selectable_list_toolbar, this, true);
        ButterKnife.bind(this, view);
    }

    public void bindAdapter(SelectableAdapter adapter) {
        mAdapter = adapter;
    }


    public void show() {
        setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.list_toolbar_show);
        startAnimation(animation);

        if (mAdapter != null) {
            mAdapter.setSelectable(true);
        }
    }

    public void hide() {
        setVisibility(View.GONE);
        llSelect.setSelected(false);

        if (mAdapter != null) {
            mAdapter.selectAll(false);
            mAdapter.setSelectable(false);
        }
    }

    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    @OnClick(R.id.ll_float_toolbar_delete)
    public void onDelete(View v) {
        if (mAdapter != null) {
            List selectedItems = mAdapter.getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(v.getContext(), "请选择需要删除的项", Toast.LENGTH_SHORT).show();
            } else {
                for (Object item : selectedItems) {
                    mAdapter.remove(item);
                }
                hide();
            }
        }
    }

    @OnClick(R.id.ll_float_toolbar_select)
    public void onSelectAll(View v) {
        boolean selected = v.isSelected();
        if (selected) {
            v.setSelected(false);
            if (mAdapter != null) {
                mAdapter.selectAll(false);
            }
        } else {
            v.setSelected(true);
            if (mAdapter != null) {
                mAdapter.selectAll(true);
            }
        }
        checkReport();
    }

    @OnClick(R.id.llReport)
    void onReportClick(View view){
        if (onReportClickListener != null){
            onReportClickListener.onReportClick();
        }
    }

    @OnClick(R.id.fl_float_toolbar_close)
    public void onClose(View v) {
        hide();
    }

    public boolean onItemClick(RecyclerView parent, View view, int position, long id) {
        if (mAdapter.isSelectable()) {
            boolean selected = mAdapter.isSelected(position);
            mAdapter.setSelect(position, !selected);
            checkReport();
            return true;
        }
        return false;
    }

    private void checkReport(){
        if (mAdapter.isHasSelected()){
            tvReport.setTextColor(getContext().getResources().getColor(R.color.text_blue));
            ivReport.setColorFilter(getContext().getResources().getColor(R.color.text_blue));
        }else {
            ivReport.setColorFilter(getContext().getResources().getColor(R.color.text_gray));
            tvReport.setTextColor(getContext().getResources().getColor(R.color.text_gray));

        }
    }
    public boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
        if (!isShowing()) {
            show();
            return true;
        }
        return false;
    }
    public void setIsShowReport(OnReportClickListener onReportClickListener){
        llReport.setVisibility(VISIBLE);
        this.onReportClickListener = onReportClickListener;
    }
    private OnReportClickListener onReportClickListener;
    public interface OnReportClickListener{
        void onReportClick();
    }
}
