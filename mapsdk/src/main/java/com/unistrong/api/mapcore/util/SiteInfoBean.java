package com.unistrong.api.mapcore.util;

public class SiteInfoBean//ae
{
    private String url;
    private String fileRootPath;
    private String downloadFileName;
    private int d;
    private String adcode;
    private String md5;
    private long citySize;

    public SiteInfoBean(String url, String fileRootPath, String downloadFileName, int paramInt, String adcode, String md5, long size) {
        this.url = url;
        this.fileRootPath = fileRootPath;
        this.downloadFileName = downloadFileName;
        this.d = paramInt;
        this.adcode = adcode;
        this.md5 = md5;
        this.citySize = size;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFileRootPath() {
        return this.fileRootPath;
    }

    public String getFileTempName() {
        return this.downloadFileName;
    }

    public int d() {
        return this.d;
    }

    public String getAdcode() {
        return this.adcode;
    }

    public String getMD5() {
        return md5;
    }

    public long getSize() {
        return citySize;
    }
}
