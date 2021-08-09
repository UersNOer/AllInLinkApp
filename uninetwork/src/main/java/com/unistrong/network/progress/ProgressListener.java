package com.unistrong.network.progress;

/**
 * Description: ProgressListener
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface ProgressListener {
    /**
     * 监听数据传输的进度
     *
     * @param soFarBytes
     * @param totalBytes
     */
    void onProgress(long soFarBytes, long totalBytes);

    /**
     * 数据传输异常
     *
     * @param throwable
     */
    void onError(Throwable throwable);
}
