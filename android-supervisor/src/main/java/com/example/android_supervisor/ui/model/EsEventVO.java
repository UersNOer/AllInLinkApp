package com.example.android_supervisor.ui.model;

import com.example.android_supervisor.entities.Attach;

import java.io.Serializable;
import java.util.List;

public class EsEventVO extends EsEvent implements Serializable, Cloneable{


    private List<Attach> beforeImgUrls;

    private List<Attach> afterImgUrls;

    public List<Attach> getBeforeImgUrls() {
        return beforeImgUrls;
    }

    public void setBeforeImgUrls(List<Attach> beforeImgUrls) {

        for (Attach attach:beforeImgUrls){
            attach.setFileUseType("0");
        }

        this.beforeImgUrls = beforeImgUrls;
    }

    public List<Attach> getAfterImgUrls() {
        return afterImgUrls;
    }

    public void setAfterImgUrls(List<Attach> afterImgUrls) {
        for (Attach attach:afterImgUrls){
            attach.setFileUseType("1");
        }
        this.afterImgUrls = afterImgUrls;
    }

//    @Override
//    public EsEventVO clone() {
//        try {
//            EsEventVO obj = (EsEventVO) super.clone();
//            if (this.beforeImgUrls != null) {
//                obj.beforeImgUrls = new ArrayList<>();
//                for (EsEventAttach attach : this.beforeImgUrls) {
//                    obj.beforeImgUrls.add(EsEventAttach.clone());
//                }
//            }
//            return obj;
//        } catch (CloneNotSupportedException e) {
//            return this;
//        }
//    }
}
