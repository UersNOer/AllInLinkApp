package com.arcsoft.arcfacedemo.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.arcsoft.arcfacedemo.model.UserFace;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.topevery.common.splite.Storage;

import java.io.File;
import java.sql.SQLException;


public class FaceSqliteHelper extends OrmLiteSqliteOpenHelper {

    private Context context;


    public FaceSqliteHelper(Context context) {
        //更新表结构后一定要加versoin
        super(new DBContext(context), "t_face.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UserFace.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            try {
                TableUtils.dropTable(connectionSource, UserFace.class, true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            onCreate(sqLiteDatabase, connectionSource);

        }
    }


    private static FaceSqliteHelper instance;

    public static FaceSqliteHelper getInstance(Context context) {
        if (instance == null || !instance.isOpen()) {
            instance = new FaceSqliteHelper(context);
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
