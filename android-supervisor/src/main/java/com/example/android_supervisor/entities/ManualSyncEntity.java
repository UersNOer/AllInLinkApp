package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.List;

public class ManualSyncEntity implements Serializable {
    //GlobalConfig
   // private List<GlobalConfig> SettingConfig;
    //图层
    private List<LayerConfig> LayerConfig;
    //区域数据
    private List<Area> Area;
    //拍照设置
    private PhotoEntity SettingPhoto;
    // 考勤设置
    private Attendance AttendanceSetting;
    // 案件等级
    private List<CaseLevel> caseLevel;
    //立案标准
    private List<EventType> Standard;
    //案件类别(事部件 大小类）
    private List<EventType> CaseClass;

    //Gps配置
    private List<GpsConfig> SettingConfig;


    //今日排班数据
   // private List<WorkPlan1> WorkPlan1;

    //今日工作网格
    private List<WorkGridSys> WorkGridSys;

    //区域单元网格
    private List<WorkGridSys> areaGridSys;

    //配置
    private ConfigEntity Config;
    private WaterMarkSet SettingWatermark;

    public WaterMarkSet getSettingWatermark() {
        return SettingWatermark;
    }

    public void setSettingWatermark(WaterMarkSet settingWatermark) {
        SettingWatermark = settingWatermark;
    }

    public List<GpsConfig> getSettingConfig() {
        return SettingConfig;
    }

    public void setSettingConfig(List<GpsConfig> settingConfig) {
        SettingConfig = settingConfig;
    }

    public ConfigEntity getConfig() {
        return Config;
    }

    public void setConfig(ConfigEntity config) {
        Config = config;
    }

//    public List<com.example.android_supervisor.entities.WorkPlan1> getWorkPlan() {
//        return WorkPlan1;
//    }
//
//    public void setWorkPlan(List<com.example.android_supervisor.entities.WorkPlan1> workPlan) {
//        WorkPlan1 = workPlan;
//    }

    public List<WorkGridSys> getWorkGridSys() {
        return WorkGridSys;
    }

    public void setWorkGridSys(List<WorkGridSys> workGridSys) {
        WorkGridSys = workGridSys;
    }

    public List<WorkGridSys> getAreaGridSys() {
        return areaGridSys;
    }

    public void setAreaGridSys(List<WorkGridSys> areaGridSys) {
        this.areaGridSys = areaGridSys;
    }

//    public List<GlobalConfig> getSettingConfig() {
//        return SettingConfig;
//    }
//
//    public void setSettingConfig(List<GlobalConfig> settingConfig) {
//        SettingConfig = settingConfig;
//    }

    public List<LayerConfig> getLayerConfig() {
        return LayerConfig;
    }

    public void setLayerConfig(List<LayerConfig> layerConfig) {
        LayerConfig = layerConfig;
    }

    public List<Area> getArea() {
        return Area;
    }

    public void setArea(List<Area> area) {
        Area = area;
    }

    public PhotoEntity getSettingPhoto() {
        return SettingPhoto;
    }

    public void setSettingPhoto(PhotoEntity settingPhoto) {
        SettingPhoto = settingPhoto;
    }

    public Attendance getAttendanceSetting() {
        return AttendanceSetting;
    }

    public void setAttendanceSetting(Attendance attendanceSetting) {
        AttendanceSetting = attendanceSetting;
    }

    public List<CaseLevel> getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(List<CaseLevel> caseLevel) {
        this.caseLevel = caseLevel;
    }

    public List<EventType> getStandard() {
        return Standard;
    }

    public void setStandard(List<EventType> standard) {
        Standard = standard;
    }

    public List<EventType> getCaseClass() {
        return CaseClass;
    }

    public void setCaseClass(List<EventType> caseClass) {
        CaseClass = caseClass;
    }
}
