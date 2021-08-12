package com.example.android_supervisor.sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.MapGrid;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.entities.MessageTitle;
import com.example.android_supervisor.entities.UserInfo;
import com.example.android_supervisor.entities.WorkGridSys;
import com.example.android_supervisor.entities.WorkPlanData;
import com.example.android_supervisor.utils.LogUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

import static com.zlw.main.recorderlib.recorder.mp3.Mp3Encoder.close;

public class PrimarySqliteHelper extends OrmLiteSqliteOpenHelper {

    public PrimarySqliteHelper(Context context) {
        super(new DBContext(context), "t_um.db", null,22);//10
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UserInfo.class);
            TableUtils.createTable(connectionSource, Message.class);
            TableUtils.createTable(connectionSource, MessageTitle.class);
            TableUtils.createTable(connectionSource, CacheEvent.class);
            TableUtils.createTable(connectionSource, WorkPlanData.class);

          //  TableUtils.createTable(connectionSource, WorkGridSysBean.class);//工作网格
            TableUtils.createTable(connectionSource, MapGrid.class);
            TableUtils.createTable(connectionSource, WorkGridSys.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            try {
                TableUtils.dropTable(connectionSource, UserInfo.class, true);
                TableUtils.dropTable(connectionSource, Message.class, true);
                TableUtils.dropTable(connectionSource, MessageTitle.class, true);
                TableUtils.dropTable(connectionSource, CacheEvent.class, true);
                TableUtils.dropTable(connectionSource, WorkPlanData.class,true);

               // TableUtils.createTable(connectionSource, WorkGridSysBean.class);//工作网格

                TableUtils.dropTable(connectionSource, MapGrid.class, true);
                TableUtils.dropTable(connectionSource, WorkGridSys.class, true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            onCreate(sqLiteDatabase, connectionSource);
        }
    }

    public <D extends RuntimeExceptionDao<UserInfo, String>> D getUserInfoDao() {
        return super.getRuntimeExceptionDao(UserInfo.class);
    }

    public <D extends RuntimeExceptionDao<Message, String>> D getMessageDao() {
        return super.getRuntimeExceptionDao(Message.class);
    }

    public <D extends RuntimeExceptionDao<MessageTitle, String>> D getMessageTitleDao() {
        return super.getRuntimeExceptionDao(MessageTitle.class);
    }


    public <D extends RuntimeExceptionDao<CacheEvent, String>> D getCacheEventDao() {
        return super.getRuntimeExceptionDao(CacheEvent.class);
    }

    public <D extends RuntimeExceptionDao<MapGrid, String>> D getMapGridDao() {
        return super.getRuntimeExceptionDao(MapGrid.class);
    }

    public <D extends RuntimeExceptionDao<WorkGridSys, String>> D getAreaGridDao() {
        return super.getRuntimeExceptionDao(WorkGridSys.class);
    }


    public synchronized <D extends RuntimeExceptionDao<WorkPlanData, String>> D getWorkPointDao() {
        return super.getRuntimeExceptionDao(WorkPlanData.class);
    }

    private static PrimarySqliteHelper instance;

    public synchronized static PrimarySqliteHelper getInstance(Context context) {
        if (instance == null || !instance.isOpen()) {
            instance = new PrimarySqliteHelper(context);
        }
        return instance;
    }

    static class DBContext extends ContextWrapper {
        private final String dbdir;

        public DBContext(Context context) {
            super(context);
            this.dbdir = Storage.getUserDataDir(context);
        }

        @Override
        public File getDatabasePath(String name) {
            LogUtils.e("getDatabasePath   " + dbdir);
            return new File(dbdir, name);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
            return openOrCreateDatabase(name, mode, factory);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
            return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        }
    }

    public void exit(){
        close();
        instance =null;
    }
}
