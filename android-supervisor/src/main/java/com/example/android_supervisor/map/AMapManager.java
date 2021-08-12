package com.example.android_supervisor.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.os.OperationCanceledException;
import android.util.Log;

import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.ui.fragment.ResultDummyFragment;

/**
 * Created by yj on 2019/7/24.
 */
public class AMapManager {

    private static int mMapConpany =0;

    public static void show(Context context) {
        show(context, null, null);
    }

    /**
     * 扩展方法 切换第三方地图
     * @param mapConpany
     */
    public static void setMapCompany(int mapConpany) {
        mMapConpany = mapConpany;
    }

    public static void show(Context context, Bundle bundle) {
        show(context, bundle, null);
    }

    public static void show(Context context, ResultCallback<Intent> callback) {
        show(context, null, callback);
    }

    public static void show(Context context, Bundle bundle, ResultCallback<Intent> callback) {
        MapResultFragment fragment = new MapResultFragment();
        fragment.setArguments(bundle);
        fragment.setCallback(callback);
        fragment.show(context, "map");
    }

    public static class MapResultFragment extends ResultDummyFragment {
        private ResultCallback<Intent> mCallback;

        public void setCallback(ResultCallback<Intent> resultCallback) {
            mCallback = resultCallback;
        }

        @Override
        protected Intent onIntentCreated() {
            Intent intent = new Intent(getActivity(), AMapActivity.class);
            switch (mMapConpany){
                case 0:
                    intent.setClass(getActivity(), AMapActivity.class);
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    break;
            }
            return intent;
        }

        @Override
        protected void onResultOk(Intent data) {
            if (mCallback != null) {
                mCallback.onResult(data);
            }
        }

        @Override
        protected void onResultCancel() {
            Log.w("MapManager", "Error:operation canceled.");
            if (mCallback != null) {
                mCallback.onError(new OperationCanceledException());
            }
        }
    }
}
