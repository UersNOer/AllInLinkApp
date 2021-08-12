package com.example.android_supervisor.ui.model;

import java.io.Serializable;

public class EsEventAttach implements Serializable,Cloneable{


    private  String filePath;

    private  String fileUseType;

    private  String fileType;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUseType() {
        return fileUseType;
    }

    public void setFileUseType(String fileUseType) {
        this.fileUseType = fileUseType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public  EsEventAttach clone() {
        try {
            return (EsEventAttach) super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }
}
