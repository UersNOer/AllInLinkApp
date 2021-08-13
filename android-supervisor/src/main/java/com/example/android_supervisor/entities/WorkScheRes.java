package com.example.android_supervisor.entities;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author wujie
 */
public class WorkScheRes {
    private String id;

    private String schId;

    @SerializedName("schName")
    private String name;

    @JsonAdapter(TimeAdapter.class)
    private String beginTime;

    @JsonAdapter(TimeAdapter.class)
    private String endTime;

    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchId() {
        return schId;
    }

    public void setSchId(String schId) {
        this.schId = schId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    static class TimeAdapter extends TypeAdapter<String> {

        @Override
        public void write(JsonWriter out, String source) throws IOException {
            if (source == null) {
                out.nullValue();
            } else {
                out.value(source);
            }
        }

        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String source = in.nextString();
            if (!TextUtils.isEmpty(source)) {
                int index = source.indexOf(" ");
                if (index != -1) {
                    source = source.substring(index + 1);
                }
            }
            return source;
        }
    }
}
