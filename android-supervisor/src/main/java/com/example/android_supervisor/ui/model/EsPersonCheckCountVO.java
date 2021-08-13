package com.example.android_supervisor.ui.model;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name="统计")
public class EsPersonCheckCountVO {


    /**
     * bayonetId : 0
     * bayonetName : string
     * discomfortCarNumber : 0
     * discomfortNumber : 0
     * id : 0
     * inCarNumber : 0
     * inNumber : 0
     * title : string
     * total : 0
     * touchCarNumber : 0
     * touchNumber : 0
     */

    private Long bayonetId;
    @SmartColumn(id =1,name = "卡点名称")
    private String bayonetName;
    private int discomfortCarNumber;
    private int discomfortNumber;
    private Long id;
    @SmartColumn(id =2,name = "涉疫人员数量")
    private int inCarNumber;
    @SmartColumn(id =3,name = "涉疫车辆数量")
    private int inNumber;
    private String title;
    private int total;

    private int touchCarNumber;

    private int touchNumber;

    public Long getBayonetId() {
        return bayonetId;
    }

    public void setBayonetId(Long bayonetId) {
        this.bayonetId = bayonetId;
    }

    public String getBayonetName() {
        return bayonetName;
    }

    public void setBayonetName(String bayonetName) {
        this.bayonetName = bayonetName;
    }

    public int getDiscomfortCarNumber() {
        return discomfortCarNumber;
    }

    public void setDiscomfortCarNumber(int discomfortCarNumber) {
        this.discomfortCarNumber = discomfortCarNumber;
    }

    public int getDiscomfortNumber() {
        return discomfortNumber;
    }

    public void setDiscomfortNumber(int discomfortNumber) {
        this.discomfortNumber = discomfortNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInCarNumber() {
        return inCarNumber;
    }

    public void setInCarNumber(int inCarNumber) {
        this.inCarNumber = inCarNumber;
    }

    public int getInNumber() {
        return inNumber;
    }

    public void setInNumber(int inNumber) {
        this.inNumber = inNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTouchCarNumber() {
        return touchCarNumber;
    }

    public void setTouchCarNumber(int touchCarNumber) {
        this.touchCarNumber = touchCarNumber;
    }

    public int getTouchNumber() {
        return touchNumber;
    }

    public void setTouchNumber(int touchNumber) {
        this.touchNumber = touchNumber;
    }
}
