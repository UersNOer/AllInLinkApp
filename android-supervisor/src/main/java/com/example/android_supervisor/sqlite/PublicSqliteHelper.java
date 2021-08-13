package com.example.android_supervisor.sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.android_supervisor.entities.Area;
import com.example.android_supervisor.entities.Attendance;
import com.example.android_supervisor.entities.CaseLevel;
import com.example.android_supervisor.entities.CaseSourceRes;
import com.example.android_supervisor.entities.Contact;
import com.example.android_supervisor.entities.Dictionary;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.entities.GestureLock;
import com.example.android_supervisor.entities.GpsConfig;
import com.example.android_supervisor.entities.LayerConfig;
import com.example.android_supervisor.entities.PhotoEntity;
import com.example.android_supervisor.entities.UserInfo;
import com.example.android_supervisor.entities.WaterMarkSet;
import com.example.android_supervisor.entities.WordRes;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.topevery.common.splite.Storage;

import java.io.File;
import java.sql.SQLException;


public class PublicSqliteHelper extends OrmLiteSqliteOpenHelper {

    private Context context;


    public PublicSqliteHelper(Context context) {
        //更新表结构后一定要加versoin
        super(new DBContext(context), "t_um.db", null, 24);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
//            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, EventType.class);//类型、大小类表
            TableUtils.createTable(connectionSource, CaseLevel.class);//案件级别
            TableUtils.createTable(connectionSource, PhotoEntity.class);
            TableUtils.createTable(connectionSource, GpsConfig.class);
            TableUtils.createTable(connectionSource, LayerConfig.class);
            TableUtils.createTable(connectionSource, Attendance.class);

            TableUtils.createTable(connectionSource, Area.class);//区域网格数据

            TableUtils.createTable(connectionSource, GestureLock.class);
        //    TableUtils.createTable(connectionSource, GlobalConfig.class);//配置数据（文件夹id)

            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, Dictionary.class);

            TableUtils.createTable(connectionSource, CaseSourceRes.class);

            TableUtils.createTable(connectionSource, WordRes.class);
            TableUtils.createTable(connectionSource, WaterMarkSet.class);

//            EventBus.getDefault().post(1);
            /**
             * 私有数据表
             */
//            TableUtils.createTable(connectionSource, .class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            try {
//                TableUtils.dropTable(connectionSource, Account.class, true);
                TableUtils.dropTable(connectionSource, GestureLock.class, true);
                //TableUtils.dropTable(connectionSource, GlobalConfig.class, true);
                TableUtils.dropTable(connectionSource, Area.class, true);
                TableUtils.dropTable(connectionSource, Contact.class, true);
                TableUtils.dropTable(connectionSource, Dictionary.class, true);
                TableUtils.dropTable(connectionSource, EventType.class, true);
                TableUtils.dropTable(connectionSource, CaseLevel.class, true);
                TableUtils.dropTable(connectionSource, Attendance.class,true);
                TableUtils.dropTable(connectionSource, PhotoEntity.class, true);
                TableUtils.dropTable(connectionSource, GpsConfig.class, true);
                TableUtils.dropTable(connectionSource, LayerConfig.class,true);
                TableUtils.dropTable(connectionSource, CaseSourceRes.class,true);

                TableUtils.dropTable(connectionSource, WordRes.class,true);
                TableUtils.createTable(connectionSource, WaterMarkSet.class);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            onCreate(sqLiteDatabase, connectionSource);
            //发送基础数据通知
//            EventBus.getDefault().post(0);

        }
    }

//    public <D extends RuntimeExceptionDao<Account, String>> D getAccountDao() {
//        return super.getRuntimeExceptionDao(Account.class);
//    }

    public <D extends RuntimeExceptionDao<GestureLock, String>> D getGestureLockDao() {
        return super.getRuntimeExceptionDao(GestureLock.class);
    }

//    public <D extends RuntimeExceptionDao<GlobalConfig, String>> D getGlobalConfigDao() {
//        return super.getRuntimeExceptionDao(GlobalConfig.class);
//    }

    public <D extends RuntimeExceptionDao<Area, String>> D getAreaDao() {
        return super.getRuntimeExceptionDao(Area.class);
    }

    public <D extends RuntimeExceptionDao<Contact, String>> D getContactDao() {
        return super.getRuntimeExceptionDao(Contact.class);
    }

    public <D extends RuntimeExceptionDao<Dictionary, String>> D getDictionaryDao() {
        return super.getRuntimeExceptionDao(Dictionary.class);
    }

    public <D extends RuntimeExceptionDao<EventType, String>> D getEventTypeDao() {

        return super.getRuntimeExceptionDao(EventType.class);
    }

    public <D extends RuntimeExceptionDao<GpsConfig, String>> D getGpsConfigDao() {

        return super.getRuntimeExceptionDao(GpsConfig.class);
    }


    public <D extends RuntimeExceptionDao<UserInfo, String>> D getUserInfoDao() {

        return super.getRuntimeExceptionDao(UserInfo.class);
    }

    public <D extends RuntimeExceptionDao<CaseLevel, String>> D getCaseLevelDao() {

        return super.getRuntimeExceptionDao(CaseLevel.class);
    }

    public <D extends RuntimeExceptionDao<PhotoEntity, String>> D getPhotoSettingDao() {

        return super.getRuntimeExceptionDao(PhotoEntity.class);
    }

    public <D extends RuntimeExceptionDao<LayerConfig, String>> D getLayerConfigDao() {

        return super.getRuntimeExceptionDao(LayerConfig.class);
    }

    public <D extends RuntimeExceptionDao<Attendance, String>> D getAttendanceSettingDao() {

        return super.getRuntimeExceptionDao(Attendance.class);
    }

    public <D extends RuntimeExceptionDao<CaseSourceRes, String>> D getCaseSourcesDao() {

        return super.getRuntimeExceptionDao(CaseSourceRes.class);
    }


    public <D extends RuntimeExceptionDao<WordRes, String>> D getWordDao() {

        return super.getRuntimeExceptionDao(WordRes.class);
    }

    public <D extends RuntimeExceptionDao<WaterMarkSet, String>> D getWaterSetDao() {

        return super.getRuntimeExceptionDao(WaterMarkSet.class);
    }
    private static PublicSqliteHelper instance;

    public static PublicSqliteHelper getInstance(Context context) {
        if (instance == null || !instance.isOpen()) {
            instance = new PublicSqliteHelper(context);
        }
        return instance;
    }

    static class DBContext extends ContextWrapper {
        private final String dbdir;

        public DBContext(Context context) {
            super(context);
            this.dbdir = Storage.getDataDir(context);
        }

        @Override
        public File getDatabasePath(String name) {
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

    public void cleanDb() throws SQLException {

    }
}
