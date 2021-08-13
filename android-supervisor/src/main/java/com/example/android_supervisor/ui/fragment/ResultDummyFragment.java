package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.orhanobut.logger.Logger;

/**
 * @author wujie
 */
public abstract class ResultDummyFragment extends Fragment {
    private boolean dismissed;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = onIntentCreated();
        if (intent != null) {
            startActivityForResult(intent, 1);
        } else {
            Logger.w("onIntentCreated return null.");
        }
    }

    protected abstract Intent onIntentCreated();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                onResultOk(data);
            } else {
                onResultCancel();
            }
        }
        dismissInternal(true);
    }

    protected abstract void onResultOk(Intent data);

    protected abstract void onResultCancel();

    public void show(Context context, String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        show(fragmentManager, tag);
    }

    public void show(FragmentManager manager, String tag) {
        dismissed = false;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    public void dismiss() {
        dismissInternal(false);
    }

    public void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    void dismissInternal(boolean allowStateLoss) {
        if (dismissed) {
            return;
        }
        dismissed = true;

        onDismiss();

        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(this);
            if (allowStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
        }
    }

    protected void onDismiss() {
        // dismiss
    }
}
