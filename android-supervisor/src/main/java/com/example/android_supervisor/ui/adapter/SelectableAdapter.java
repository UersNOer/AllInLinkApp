package com.example.android_supervisor.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
//import androidx.annotation.LayoutRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.cncoderx.recyclerviewhelper.utils.Array;
import com.example.android_supervisor.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author wujie
 */
public class SelectableAdapter<T> extends ObjectAdapter<T> {
    private boolean selectable;
    private Array<Boolean> mStates = new Array<>();

    public SelectableAdapter(@LayoutRes int resource) {
        super(resource);
    }

    public SelectableAdapter(@LayoutRes int resource, @NonNull T[] objects) {
        super(resource, objects);
        mStates.addAll(obtainBoolCollection(objects.length));
    }

    public SelectableAdapter(@LayoutRes int resource, @NonNull Collection<? extends T> objects) {
        super(resource, objects);
        mStates.addAll(obtainBoolCollection(objects.size()));
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = super.onCreateView(inflater, parent, viewType);
        FrameLayout container = (FrameLayout) inflater.inflate(R.layout.item_selectable_list, parent, false);
        container.addView(view);
        return container;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, T object, int position) {
        Context context = holder.itemView.getContext();

        FrameLayout container = (FrameLayout) holder.itemView;
        View view = container.getChildAt(1);

        boolean selected = isSelected(position);
        CheckBox selectBox = holder.getView(R.id.select_box, CheckBox.class);
        if (selected) {
            selectBox.setChecked(true);
        } else {
            selectBox.setChecked(false);
        }

        int translation = view.getResources().getDimensionPixelOffset(R.dimen.selectable_item_translation);

        if (isSelectable()) {
            if (view.getTranslationX() == 0) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, translation);
                animator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
                animator.start();
            }
        } else {
            if (view.getTranslationX() == translation) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", translation, 0);
                animator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
                animator.start();
            }
        }
    }

    private Collection<Boolean> obtainBoolCollection(int size) {
        ArrayList<Boolean> arrayList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(Boolean.FALSE);
        }
        return arrayList;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        selectAll(false);
    }

    public boolean isSelected(int position) {
        return mStates.get(position);
    }
    public boolean isHasSelected(){
        int size = mStates.size();
        for (int i = 0;i < size;i++){
            if (mStates.get(i)){
                return true;
            }
        }
        return false;
    }

    public void setSelect(int position, boolean selected) {
        mStates.set(position, selected);
        if (isNotifyOnChange()) {
            notifyItemChanged(position);
        }
    }

    public void selectAll(boolean selected) {
        int size = mStates.size();
        for (int i = 0; i < size; i++) {
            mStates.set(i, selected);
            if (isNotifyOnChange()) {
                notifyItemChanged(i);
            }
        }
    }

    public List<T> getSelectedItems() {
        int size = size();
        List<T> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (isSelected(i)) {
                items.add(get(i));
            }
        }
        return items;
    }

    @Override
    public void add(@NonNull T object) {
        super.add(object);
        mStates.add(Boolean.FALSE);
    }

    @Override
    public void add(int index, @NonNull T object) {
        super.add(index, object);
        mStates.add(index, Boolean.FALSE);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> objects) {
        super.addAll(objects);
        mStates.addAll(obtainBoolCollection(objects.size()));
    }

    @Override
    public void addAll(int index, @NonNull Collection<? extends T> objects) {
        super.addAll(index, objects);
        mStates.addAll(index, obtainBoolCollection(objects.size()));
    }

    @Override
    public void remove(int index) {
        super.remove(index);
        mStates.remove(index);
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        mStates.removeRange(fromIndex, toIndex);
    }

    @Override
    public void swap(int index, int index2) {
        super.swap(index, index2);
        mStates.swap(index, index2);
    }

    @Override
    public void sort(@NonNull Comparator<? super T> comparator) {
        super.sort(comparator);
    }

    @Override
    public void clear() {
        super.clear();
        mStates.clear();
    }
}

