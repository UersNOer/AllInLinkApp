package com.example.android_supervisor.entities;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

/**
 * @author wujie
 */
@JsonAdapter(Attach.AdapterFactory.class)
public class Attach implements Serializable, Cloneable {
    @SerializedName(value = "attchType",alternate = {"fileType"})//,alternate = {"fileType"}
    private Integer type; // 附件类型（0：图片，1：音频，2：视频）

    @SerializedName("attchUsage")
    private int usage; // 附件用途 (0案件上报附件 1核实附件 2核查附件 3专业部门上传附件 4其它附件)"

    @SerializedName("attchFileName")
    private String fileName;

    @SerializedName("attchFileThumbnailPath")
    private String thumbUrl; // 缩略图地址


    @SerializedName(value = "attchFilePath")
    private String url;

    //疫情
    private String fileUseType;

    private String filePath;

//    private String fileType;
//
//    public String getFileType() {
//        return fileType;
//    }
//
//    public void setFileType(String fileType) {
//        this.fileType = fileType;
//    }

    public Attach() {
    }

    public String getFileUseType() {
        return fileUseType;
    }

    public void setFileUseType(String fileUseType) {
        this.fileUseType = fileUseType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attach) {
            Attach other = (Attach) obj;
            if (this.type != other.type) {
                return false;
            }
            if (this.usage != other.usage) {
                return false;
            }
            if (!equals(this.fileName, other.fileName)) {
                return false;
            }
            if (!equals(this.url, other.url)) {
                return false;
            }
            if (!equals(this.thumbUrl, other.thumbUrl)) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    @Override
    public Attach clone() {
        try {
            return (Attach) super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    static class AdapterFactory implements TypeAdapterFactory {
        final TypeAdapterFactory mAdapterFactory;

        public AdapterFactory() {
            ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.EMPTY_MAP);
            JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
            mAdapterFactory = new ReflectiveTypeAdapterFactory(
                    constructorConstructor,
                    FieldNamingPolicy.IDENTITY,
                    Excluder.DEFAULT,
                    jsonAdapterFactory);
        }

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            TypeAdapter<T> adapter = mAdapterFactory.create(gson, type);
            return new Adapter<>(adapter);
        }

        public static class Adapter<T> extends TypeAdapter<T> {
            final TypeAdapter<T> adapter;

            public Adapter(TypeAdapter<T> adapter) {
                this.adapter = adapter;
            }


            @Override
            public void write(JsonWriter out, T value) throws IOException {
                if (value instanceof Attach) {
                    Attach attach = (Attach) value;
                    if (attach.getType() == 0) {
                        String url = attach.getUrl();
                        String thumbUrl = attach.getThumbUrl();
                        if (TextUtils.isEmpty(thumbUrl)) {
                            attach.setThumbUrl(url);
                        }
                    }
                }
                adapter.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                T value = adapter.read(in);
                if (value instanceof Attach) {
                    Attach attach = (Attach) value;
                    if (attach.getType() == 0) {
                        String url = attach.getUrl();
                        String thumbUrl = attach.getThumbUrl();
                        if (TextUtils.isEmpty(thumbUrl)) {
                            attach.setThumbUrl(url);
                        }
                    }
                }
                return value;
            }
        }
    }
}
