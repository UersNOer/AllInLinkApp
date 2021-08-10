package com.leador.mapcore;

import java.util.ArrayList;

public class NormalMapLoader
        extends BaseMapLoader {
    public NormalMapLoader(MapCore paramMapCore, BaseMapCallImplement paramBaseMapCallImplement, int datasource) {
        this.datasource = datasource;
        this.mapCore = paramMapCore;
        this.mMapCallback = paramBaseMapCallImplement;
        this.createtime = System.currentTimeMillis();
    }

    public String getGridParmaV4() {
        String str1 = null;
        String str2 = ";";
        for (int i = 0; i < this.mapTiles.size(); i++) {
            String str4 = ((MapSourceGridData) this.mapTiles.get(i)).getGridName();
            if ((str4 != null) && (str4.length() != 0) &&
                    (!containllegal(str4)) &&
                    (isAssic(str4))) {
//        if ((this.datasource == DATA_SOURCE_TYPE_DATA_VEC_TMC) && //原始值为4
//                (((MapSourceGridData)this.mapTiles.get(i)).obj != null))
//        {
//          String str5 = (String)((MapSourceGridData)this.mapTiles.get(i)).obj;
//          try
//          {
//            str4 = str4 + "-" + URLEncoder.encode(str5, "utf-8");
//          }
//          catch (UnsupportedEncodingException localUnsupportedEncodingException)
//          {
//            continue;
//          }
//        }
                if (str1 == null) {
                    str1 = str4 + str2;
                } else {
                    str1 = str1 + str4 + str2;
                }
            }
        }
        String str3 = str1;
        if (str3 == null) {
            return null;
        }
        if (str3.length() > 0) {
            while ((str3 != null) && ((str3.endsWith(str2)) || (str3.endsWith(" ")))) {
                str3 = str1.substring(0, str3.length() - 1);
            }
        }
        if (str3.length() <= 0) {
            return null;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_ROAD) { //原始值为0
            //return "mapdataver=5&type=20&mesh=" + str3;
            return "t=VMMV4&type=20&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_GEO_BUILDING) {//原始值为1
            //return "mapdataver=5&type=11&mesh=" + str3;
            return "t=VMMV4&type=11&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_STANDARD) { //原始值为7
            //return "mapdataver=5&type=1&mesh=" + str3;
            return "t=VMMV4&type=1&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_POI) {//原始值为8
            //return "mapdataver=5&type=13&mesh=" + str3;
            return "t=VMMV4&type=13&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_VERSION) {//原始值为9
            //return "mapdataver=5&type=40&mesh=" + str3;
            return "t=VMMV4&type=40&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_BMP_BASEMAP) {//原始值为2
            //return "t=BMPBM&mapdataver=5&mesh=" + str3;
            return "t=BMPBM&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_SATELLITE) {//原始值为3
            //return "mapdataver=5&mesh=" + str3;
            return "t=BMTI&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_VEC_TMC) {//原始值为4
            //return "mapdataver=5&v=6.0.0&bver=2&mesh=" + str3;
            return "t=TMCX&v=6.0.0&bver=2&mesh=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_3DOBJECT) {//原始值为6
            //return "t=VMMV3&mapdataver=5&type=mod&cp=0&mid=" + str3;
            return "t=VMMV3&type=mod&cp=0&mid=" + str3;
        }
        if (this.datasource == DATA_SOURCE_TYPE_DATA_STREETVIEW) {//街景路网
            return "t=BMPR&mesh=" + str3;
        }
        return null;
    }

    @Override
    protected String getUserGridURL(String mesh) {
        String url = mMapCallback.getUserGridURL(mesh);
        return url;
    }

    protected String getGridParma() {
        return getGridParmaV4();
    }

    protected String getMapSvrPath() {
        return "";
    }

    protected String getMapAddress() {
        return this.mMapCallback.getMapSvrAddress();
    }

    public boolean isRequestValid() {
        return this.mMapCallback.isGridsInScreen(this.mapTiles, this.datasource);
    }

    protected void processRecivedVersionOrScenicWidgetData() {
        if (this.datasource == DATA_SOURCE_TYPE_DATA_VERSION) {//原始值为9
            processRecivedVersionData(this.recievedDataBufferBackup, 0, this.receivedDataSizeBackup);

            //处理完成后清除内存
            this.recievedDataBufferBackup = null;
            this.receivedDataSizeBackup = 0;
        }
    }

    protected void processRecivedDataByType() {
        if ((this.datasource == DATA_SOURCE_TYPE_DATA_ROAD) || (this.datasource == DATA_SOURCE_TYPE_DATA_GEO_BUILDING) ||
                (this.datasource == DATA_SOURCE_TYPE_DATA_POI) || (this.datasource == DATA_SOURCE_TYPE_DATA_STANDARD)) {//原始值为0,1,8,7
            processReceivedDataV4();
        } else {
            super.processRecivedData();
        }
    }

    protected boolean processReceivedDataHeader(int paramInt) {
        if (this.recievedDataSize > 7) {
            int i = 0;

            int j = Convert.getInt(this.recievedDataBuffer, i);
            if (j != 0) {
                doCancel();

                return false;
            }
            i += 4;

            i += 4;

            Convert.moveArray(this.recievedDataBuffer, i, this.recievedDataBuffer, 0, paramInt - 8);

            this.recievedDataSize -= i;

            this.nextImgDataLength = 0;
            this.recievedHeader = true;
            if ((this.datasource == DATA_SOURCE_TYPE_DATA_ROAD) || (this.datasource == DATA_SOURCE_TYPE_DATA_GEO_BUILDING) ||
                    (this.datasource == DATA_SOURCE_TYPE_DATA_POI) || (this.datasource == DATA_SOURCE_TYPE_DATA_STANDARD)) {//原始值为0,1,8,7
                processReceivedDataV4();
            } else {
                super.processRecivedData();
            }
            return true;
        }
        return false;
    }

    protected boolean isNeedProcessReturn() {
        if (this.datasource == DATA_SOURCE_TYPE_DATA_VERSION) { //原始值为9
            return true;
        }
        return false;
    }

    void processRecivedTileData(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (0 == paramInt1) {
            super.processRecivedTileData(paramArrayOfByte, paramInt1, paramInt2);
        } else if ((this.datasource == DATA_SOURCE_TYPE_DATA_BMP_BASEMAP) || (this.datasource == DATA_SOURCE_TYPE_DATA_SATELLITE) || this.datasource == DATA_SOURCE_TYPE_DATA_STREETVIEW || this.datasource == DATA_SOURCE_TYPE_DATA_BMP_USER_GIRDSMAP) { //原始值为2,3
            processRecivedTileDataBmp(paramArrayOfByte, paramInt1, paramInt2);
        } else if (this.datasource == DATA_SOURCE_TYPE_DATA_VEC_TMC) { //原始值为4
            processRecivedTileDataVTmc(paramArrayOfByte, paramInt1, paramInt2);
        } else if (this.datasource == DATA_SOURCE_TYPE_DATA_3DOBJECT) {//原始值为6
            processRecivedModels(paramArrayOfByte, paramInt1, paramInt2);
        } else {
            super.processRecivedTileData(paramArrayOfByte, paramInt1, paramInt2);
        }
    }

    void processRecivedTileDataBmp(byte[] mapData, int paramInt1, int paramInt2) {
        int i = paramInt1;

        paramInt1 += 4;
        int j = mapData[(paramInt1++)];

        String str = "";
        if ((j > 0) && (paramInt1 + j - 1 < paramInt2)) {
            str = new String(mapData, paramInt1, j);
        }
        paramInt1 += j;
        if (!this.mapCore.isMapEngineValid()) {
            return;
        }
        if (paramInt2 <= i) {
            return;
        }
        int k = !this.mMapCallback.isGridInScreen(this.datasource, str) ? 1 : 0;
        if (this.mapCore.putMapData(mapData, i, paramInt2 - i, this.datasource, 0)) {
            VMapDataCache.getInstance().putRecoder(null, str, this.datasource);
        }
        if (k != 0) {
            doCancel();
        }
    }

    void processRecivedTileDataVTmc(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        int i = paramInt1;

        paramInt1 += 4;
        int j = paramArrayOfByte[(paramInt1++)];
        if ((paramInt1 + j > paramArrayOfByte.length) || (paramInt1 > paramArrayOfByte.length - 1) || (j < 0)) {
            return;
        }
        String str = new String(paramArrayOfByte, paramInt1, j);

        paramInt1 += j;

        int k = paramArrayOfByte[(paramInt1++)];

        paramInt1 += k;

        paramInt1 += 4;
        if (!this.mapCore.isMapEngineValid()) {
            return;
        }
        VTMCDataCache localVTMCDataCache = VTMCDataCache.getInstance();
        if (paramInt2 <= i) {
            return;
        }
        byte[] arrayOfByte = new byte[paramInt2 - i];
        System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, paramInt2 - i);
        CacheData localf = localVTMCDataCache.putData(arrayOfByte);

        int m = !this.mMapCallback.isGridInScreen(this.datasource, str) ? 1 : 0;
        if (localf != null) {
            this.mapCore.putMapData(localf.mapData, 0, localf.mapData.length, this.datasource, localf.createTime);
        }
        if (m != 0) {
            doCancel();
        }
    }

    void processRecivedModels(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        int i = paramInt1;
        int j = paramArrayOfByte[(paramInt1++)];
        if (j < 0) {
            return;
        }
        String str = new String(paramArrayOfByte, paramInt1, j);
        if (!this.mapCore.isMapEngineValid()) {
            return;
        }
        if (paramInt2 <= i) {
            return;
        }
        int k = !this.mMapCallback.isGridInScreen(this.datasource, str) ? 1 : 0;

        this.mapCore.putMapData(paramArrayOfByte, i, paramInt2 - i, this.datasource, 0);
        if (k != 0) {
            doCancel();
        }
    }

    /**
     * 处理版本查询结果
     *
     * @param data
     * @param index
     * @param dataSize
     */
    void processRecivedVersionData(byte[] data, int index, int dataSize) {
        if (null == data)
            return;

        if (0 < dataSize && dataSize <= data.length) {
            // 保存已下载的数据
            int pos = 0;
            int flag = Convert.getInt(data, pos);
            pos += 4;
            // 判断数据标志位
            if (0 != flag)
                return;

            int reserve = Convert.getInt(data, pos);
            pos += 4;
            // 判断数据标志位
            if (0 != reserve)
                return;

            // 获取图块数量
            int gridSize = Convert.getInt(data, pos);
            pos += 4;

            boolean valid = true;
            ArrayList<String> tileNameList = new ArrayList<String>();
            for (int i = 0; i < gridSize; i++) {
                int tileNameLength = 0;
                String tileName = "";
                if (pos < dataSize)
                    tileNameLength = data[pos++];
                else {
                    valid = false;
                    break;
                }

                if (tileNameLength > 0 && (pos + tileNameLength) < dataSize) {
                    tileName = new String(data, pos, tileNameLength);
                    tileNameList.add(tileName);
                } else {
                    valid = false;
                    break;
                }
                pos += tileNameLength;
                // int version = Convert.getInt(data, pos);
                pos += 4;

            }

            // 根据数据合法性进行相应保存
            if (valid) {
                for (int i = 0; i < tileNameList.size(); i++) {
                    String tileName = tileNameList.get(i);
                    VMapDataCache.getInstance().putRecoder(null, tileName, datasource);
                }
                mapCore.putMapData(data, 0, dataSize, datasource, 0);
            }

        }
    }
}
