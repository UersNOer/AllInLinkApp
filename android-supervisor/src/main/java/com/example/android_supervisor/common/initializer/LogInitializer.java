package com.example.android_supervisor.common.initializer;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.Storage;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 初始化 Logger 日志类
 *
 * @author wujie
 */
public class LogInitializer implements Initializer {

    public void initialize(Context context) {
        addPrintLogStrategy(context);
        addDiskLogStrategy(context);
    }

    private void addPrintLogStrategy(Context context) {
        final String logTag = context.getString(R.string.app_label);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .tag(logTag)
                .build();

        LogAdapter logAdapter = new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        };

        Logger.addLogAdapter(logAdapter);
    }

    private void addDiskLogStrategy(Context context) {
        final String logTag = context.getString(R.string.app_label);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .logStrategy(new DiskLogStrategy(context))
                .showThreadInfo(false)
                .tag(logTag)
                .build();

        LogAdapter logAdapter = new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return priority == Logger.ERROR;
            }
        };

        Logger.addLogAdapter(logAdapter);
    }

    class DiskLogStrategy implements LogStrategy {
        private String folder;
        private SimpleDateFormat dateFormat;

        @SuppressLint("SimpleDateFormat")
        public DiskLogStrategy(Context context) {
            this.folder = Storage.getLogsDir(context);
            this.dateFormat = new SimpleDateFormat("yyyyMMdd");
        }

        @Override
        public void log(int priority, @Nullable String tag, @NonNull String message) {
            FileWriter fileWriter = null;
            File logFile = getLogFile();
            try {
                fileWriter = new FileWriter(logFile, true);
                fileWriter.append(message);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e1) { /* fail silently */ }
                }
            }
        }

        private File getLogFile() {
            String date = dateFormat.format(new Date());
            File file = new File(folder, "LOG_" + date + ".txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) { /* fail silently */ }
            }
            return file;
        }
    }
}
