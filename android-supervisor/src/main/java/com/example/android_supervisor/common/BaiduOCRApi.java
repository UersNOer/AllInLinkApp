package com.example.android_supervisor.common;

import android.content.Context;
import android.content.Intent;
import androidx.core.os.OperationCanceledException;

import com.baidu.ocr.camera.CameraActivity;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.example.android_supervisor.ui.fragment.ResultDummyFragment;

import java.io.File;

import static com.xuexiang.xui.XUI.getContext;

/**
 * @author wujie
 */
public class BaiduOCRApi {
    private static BaiduOCRApi ocrApi;

    public static BaiduOCRApi getInstance() {
        if (ocrApi == null) {
            ocrApi = new BaiduOCRApi();
        }
        return ocrApi;
    }

    public void recognizeIDCard(Context context, ResultCallback<IDCardResult> callback) {
        CameraResultFragment fragment = new CameraResultFragment();
        fragment.setCallback(callback);
        fragment.show(context, "ocr_id_card");
    }

    public static class CameraResultFragment extends ResultDummyFragment
            implements OnResultListener<IDCardResult> {

        private ResultCallback<IDCardResult> callback;
        private File outFile;

        public void setCallback(ResultCallback<IDCardResult> callback) {
            this.callback = callback;
        }

        @Override
        protected Intent onIntentCreated() {
            Context context = getContext();
            String tempDir = Storage.getTempDir(context);
            outFile = new File(tempDir, "ocr_id_card");

            Intent intent = new Intent(context, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
            intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN, OCR.getInstance(context).getLicense());
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, outFile.getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
            return intent;
        }

        @Override
        protected void onResultOk(Intent data) {
            String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                recognizeIDCard(outFile.getAbsolutePath());
            } else {
                if (callback != null) {
                    callback.onError(new IllegalArgumentException("Error content type."));
                }
            }
        }

        @Override
        protected void onResultCancel() {
            if (callback != null) {
                callback.onError(new OperationCanceledException());
            }
        }

        private void recognizeIDCard(String path) {
            IDCardParams param = new IDCardParams();
            param.setImageFile(new File(path));
            param.setDetectDirection(true);
            param.setIdCardSide(IDCardParams.ID_CARD_SIDE_FRONT);
            OCR.getInstance(getContext()).recognizeIDCard(param, this);
        }

        @Override
        public void onResult(IDCardResult result) {
            if (result == null) {
                if (callback != null) {
                    callback.onError(new NullPointerException("no result"));
                }
            } else {
                if (callback != null) {
                    callback.onResult(result);
                }
            }
        }

        @Override
        public void onError(OCRError error) {
            if (callback != null) {
                callback.onError(error.getCause());
            }
        }
    }
}
