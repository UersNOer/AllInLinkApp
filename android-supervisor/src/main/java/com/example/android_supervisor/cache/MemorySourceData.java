package com.example.android_supervisor.cache;


import com.example.android_supervisor.entities.AutuSysData;
import com.example.android_supervisor.utils.Environments;

public class MemorySourceData implements SourceData {


    @Override
    public AutuSysData loadData() {

        if (Environments.autuSysData!=null){

            return Environments.autuSysData;
        }


        return null;

    }
}
