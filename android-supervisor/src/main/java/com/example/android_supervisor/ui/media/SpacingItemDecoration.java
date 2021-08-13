package com.example.android_supervisor.ui.media;

import android.graphics.Rect;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author wujie
 */
public class SpacingItemDecoration  extends RecyclerView.ItemDecoration{
    private int offset;
    private boolean hasPadding;

    public SpacingItemDecoration(int offset, boolean hasPadding) {
        this.offset = offset;
        this.hasPadding = hasPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            final int spanIndex = layoutParams.getSpanIndex();
            final int spanCount = layoutManager.getSpanCount();
            if (hasPadding) {
                int spanPadding = (spanCount + 1) * offset / spanCount;
                outRect.right = (spanPadding - offset) * (spanIndex + 1);
                outRect.left = spanPadding - outRect.right;
                outRect.top = offset;
            } else {
                int spanPadding = (spanCount - 1) * offset / spanCount;
                outRect.left = (offset - spanPadding) * spanIndex;
                outRect.right = spanPadding - outRect.left;
                outRect.top = offset / 2;
                outRect.bottom = offset / 2;
            }
        }
    }
}
