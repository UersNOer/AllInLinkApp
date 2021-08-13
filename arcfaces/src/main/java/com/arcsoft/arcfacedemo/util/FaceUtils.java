package com.arcsoft.arcfacedemo.util;

import android.content.Context;

import com.arcsoft.arcfacedemo.common.FaceSqliteHelper;
import com.arcsoft.arcfacedemo.model.UserFace;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FaceUtils {


    public static boolean writeFace(Context context,UserFace userFace) {

        boolean success = false;
        try {
           FaceSqliteHelper.getInstance(context).getDao(UserFace.class)
                    .createOrUpdate(userFace);
           success = true;
//            Toast.makeText(context, "人脸写入成功", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static List<UserFace> getFace(Context context,String userName) {
        List<UserFace> temp = new ArrayList<>();
        try {
            List<UserFace> userFaces = FaceSqliteHelper.getInstance(context).getDao(UserFace.class).queryBuilder()
                    .where().eq("userName",userName).query();

             if (userFaces!=null && userFaces.size()>0){
                 temp.add(userFaces.get(0));
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(temp ==null){
            temp = new ArrayList<>();
        }
        return temp;
    }

    public static List<UserFace> deleteFace(Context context,String userName) {


        return null;
    }




}
