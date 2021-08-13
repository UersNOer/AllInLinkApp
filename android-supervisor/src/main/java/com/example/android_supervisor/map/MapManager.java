package com.example.android_supervisor.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.os.OperationCanceledException;
import android.util.Log;

import com.example.android_supervisor.common.ResultCallback;
import com.example.android_supervisor.ui.fragment.ResultDummyFragment;

public class MapManager {
    static final double centerX = 112.93869000014;
    static final double centerY = 27.83320500001;
    static final float initZoom = 18;

    public static void show(Context context) {
        show(context, null, null);
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
            Intent intent = new Intent(getActivity(), MapActivity.class);
            Bundle bundle = getArguments();
            if (bundle != null) {
                intent.putExtras(bundle);
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
