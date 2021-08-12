package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;


@DatabaseTable(tableName = "t_work_plan_point")
public class WorkPlanData implements Serializable {



//      "id": "1192712416546701314",
//              "createId": "1176680734874714113",
//              "createTime": "2019-11-08 15:56:14",
//              "updateId": null,
//              "updateTime": null,
//              "pointId": "1191601433421938690",
//              "workPlanId": "1192685281090142209",
//              "workGridCode": "440309001007A0",
//              "punchStatus": "1",
//              "punchX": "28.233002",
//              "punchY": "112.86957",
//              "punchTime": "2019-11-08 15:54:38",
//              "userId": "1176680734874714113"


    /**
     * workPlanId : 1191681539771359233
     * workDate : 上午班(09:00:00-11:50:00)
     * workGridCode : 440309001007A0
     * workBeginTime : 09:00:00
     * workEndTime : 11:50:00
     * pointList : [{"pointId":null,"userId":null,"absX":"113.9436326567203700","absY":"113.9436326567203700","punchX":null,"punchY":null,"pointName":"A0-1","punchStatus":"0","punchTime":null},{"pointId":null,"userId":null,"absX":"113.9448089724908200","absY":"113.9448089724908200","punchX":null,"punchY":null,"pointName":"A0-2","punchStatus":"0","punchTime":null}]
     */

    @DatabaseField(id = true)
    private String workPlanId;

    @DatabaseField
    private String workDate;

    @DatabaseField
    private String workGridCode;

    @DatabaseField
    private String workBeginTime;

    @DatabaseField
    private String workEndTime;

    @DatabaseField
    private int offset = 30 ;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<PointListBean> pointList;


    public String getWorkPlanId() {
        return workPlanId;
    }

    public void setWorkPlanId(String workPlanId) {
        this.workPlanId = workPlanId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getWorkGridCode() {
        return workGridCode;
    }

    public void setWorkGridCode(String workGridCode) {
        this.workGridCode = workGridCode;
    }

    public String getWorkBeginTime() {
        return workBeginTime;
    }

    public void setWorkBeginTime(String workBeginTime) {
        this.workBeginTime = workBeginTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public ArrayList<PointListBean> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<PointListBean> pointList) {
        this.pointList = pointList;
    }

    public static class PointListBean implements Serializable {
        /**
         * pointId : null
         * userId : null
         * absX : 113.9436326567203700
         * absY : 113.9436326567203700
         * punchX : null
         * punchY : null
         * pointName : A0-1
         * punchStatus : 0
         * punchTime : null
         */

//          "id": "1192712416546701314",
//                  "createId": "1176680734874714113",
//                  "createTime": "2019-11-08 15:56:14",
//                  "updateId": null,
//                  "updateTime": null,
//                  "pointId": "1191601433421938690",
//                  "workPlanId": "1192685281090142209",
//                  "workGridCode": "440309001007A0",
//                  "punchStatus": "1",
//                  "punchX": "28.233002",
//                  "punchY": "112.86957",
//                  "punchTime": "2019-11-08 15:54:38",
//                  "userId": "1176680734874714113"


        private String pointId;
//        private String userId;
        private String absX;
        private String absY;
        private String punchX;
        private String punchY;
        private String pointName;
        private String punchStatus;
        private String punchTime;

        private String workPlanId;//扩展字段
        private String workGridCode;

        public String getPunchX() {
            return punchX;
        }

        public void setPunchX(String punchX) {
            this.punchX = punchX;
        }

        public String getPunchY() {
            return punchY;
        }

        public void setPunchY(String punchY) {
            this.punchY = punchY;
        }

        public String getWorkPlanId() {
            return workPlanId;
        }

        public void setWorkPlanId(String workPlanId) {
            this.workPlanId = workPlanId;
        }

        public String getWorkGridCode() {
            return workGridCode;
        }

        public void setWorkGridCode(String workGridCode) {
            this.workGridCode = workGridCode;
        }

        public String getPointId() {
            return pointId;
        }

        public void setPointId(String pointId) {
            this.pointId = pointId;
        }


        public String getAbsX() {
            return absX;
        }

        public void setAbsX(String absX) {
            this.absX = absX;
        }

        public String getAbsY() {
            return absY;
        }

        public void setAbsY(String absY) {
            this.absY = absY;
        }




        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getPunchStatus() {
            return punchStatus;
        }

        public void setPunchStatus(String punchStatus) {
            this.punchStatus = punchStatus;
        }

        public String getPunchTime() {
            return punchTime;
        }

        public void setPunchTime(String punchTime) {
            this.punchTime = punchTime;
        }
    }
}
