package com.example.android_supervisor.utils;

import android.content.Context;

import com.example.android_supervisor.entities.CaseLevel;
import com.example.android_supervisor.entities.CaseSourceRes;
import com.example.android_supervisor.entities.ChooseData;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.view.MenuItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yj on 2019/9/4.
 */
public class CaseBindUtils {



    public static void bindCaseLevel(Context context,MenuItemView tv){

        try{
            PublicSqliteHelper sqliteHelper  = PublicSqliteHelper.getInstance(context);
            if ( sqliteHelper.getCaseLevelDao()!=null){
                List<CaseLevel> caseLevels =  sqliteHelper.getCaseLevelDao().queryForAll();
                List<ChooseData> chooseDatas =  new ArrayList<>();
                for (CaseLevel caseLevel:caseLevels) {
                    ChooseData chooseData = new ChooseData();
                    chooseData.setId(caseLevel.getDictCode());
                    chooseData.setName(caseLevel.getDictName());
                    chooseDatas.add(chooseData);
                }
                tv.setChooseData(chooseDatas);
                tv.setDefault();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void bindCaseStandard(Context context,String evtTypeId,MenuItemView tv){

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
        final List<EventType> data = sqliteHelper.getEventTypeDao().queryForEq("pid", evtTypeId);
        final int dataSize = data.size();
        if (dataSize == 0) {
            ToastUtils.show(context, "当前选择的案件类别没有立案标准");
            return;
        }

        List<ChooseData> chooseDatas =  new ArrayList<>();
        for (EventType evtType : data) {
            ChooseData chooseData = new ChooseData();
            chooseData.setId(evtType.getId());
            chooseData.setName(evtType.getName());
            chooseDatas.add(chooseData);
        }
        tv.setChooseData(chooseDatas);
    }


    public static EventType bindCaseUIById(Context context,String evtTypePid,MenuItemView tv){
        EventType eventType =  PublicSqliteHelper.getInstance(context).getEventTypeDao().queryForId(evtTypePid);
        if (eventType!=null){
            tv.setValue(eventType.getName());
        }
        return eventType;
    }

    public static EventType CaseById(Context context,String evtTypePid){
        EventType eventType =  PublicSqliteHelper.getInstance(context).getEventTypeDao().queryForId(evtTypePid);
        return eventType;
    }



    public static CaseLevel bindLevleUIById(Context context,String evtTypeId,MenuItemView tv){
        List<CaseLevel> eventType_levels =  PublicSqliteHelper.getInstance(context).getCaseLevelDao().queryForAll();
        if (eventType_levels!=null&& eventType_levels.size()>0){
            for (CaseLevel caseLevel:eventType_levels) {
                if (caseLevel.getDictCode().equals(evtTypeId)){
                    tv.setValue(caseLevel.getDictName());
                    return caseLevel;
                }
            }
        }
        return null;

    }


    /**
     * 根据code 值转换 案件来源
     */
    public static String getSourceNameByCode(Context context,String code) {
        String source = "";
        try {

            PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(context);
            CaseSourceRes caseSourceRes = sqliteHelper.getCaseSourcesDao().queryForId(code);

            if (caseSourceRes!=null){
                source = caseSourceRes.getDictName();
            }else {
                ToastUtils.show(context,"请进行数据同步后操作");
            }


//            int SourceCode = Integer.valueOf(code);
//            switch (SourceCode) {
//                case ConstantEntity.CASE_WEB:
//                    source = context.getResources().getString(R.string.case_web);
//                break;
//                case ConstantEntity.CASE_PHONE:
//                    source =  context.getResources().getString(R.string.case_phone);
//                break;
//                case ConstantEntity.CASE_WECHAT:
//                    source =  context.getResources().getString(R.string.case_wechat);
//                break;
//                case ConstantEntity.CASE_NORMAL:
//                    source =  context.getResources().getString(R.string.case_normal);
//                break;
//                case ConstantEntity.CASE_FAST:
//                    source = context.getResources().getString(R.string.case_fast);
//                break;
//                case ConstantEntity.CASE_LEADER:
//                    source =  context.getResources().getString(R.string.case_leader);
//                break;
//                case ConstantEntity.CASE_CZT:
//                    source =  context.getResources().getString(R.string.case_czt);
//                break;
//                case ConstantEntity.CASE_MASTER:
//                    source =  context.getResources().getString(R.string.case_master);
//                break;
//                case ConstantEntity.CASE_PUBLIC:
//                    source =  context.getResources().getString(R.string.case_public);
//                break;
//                case ConstantEntity.CASE_DcCaseReport:
//                    source = "督查员上报";
//                    break;
//
//            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;

    }
}
