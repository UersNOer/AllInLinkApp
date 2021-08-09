package com.unistrong.api.services.localsearch;

import android.content.Context;

import com.unistrong.api.services.localsearch.model.SearchResultInfo;

/**
 * 本地搜索引擎接口。
 */
class LocalSearchAPI {

    private static boolean initover = false;
    private static LocalSearchAPI instance = null;

    /**
     * 获得搜索引擎实例
     * @return 引擎实例。
     */
    public static LocalSearchAPI getInstance(){
        if(instance == null)
        {
            instance = new LocalSearchAPI();
            initover = false;
        }

        return instance;
    }

    /**
     * 引擎初始化。
     * @param dataPath 本地搜索数据存储目录。请避免存储目录中有中文字符。
     * @return 初始化是否成功， 0 代表失败，1代表成功。
     */
    public int init(Context context, String dataPath, String snPath)
    {
        SearchCore.deviceid = DeviceIdManager.getDeviceID(context);
        int retvalue = SearchCore.nativeInit(dataPath,snPath);
        if(retvalue == 1)
            initover = true;

        return retvalue;
    }

    /**
     * 引擎释放。
     */
    public void exit()
    {
        SearchCore.nativeExit();
        initover = false;
    }

    /**
     * 获得SDK版本信息。
     * @return SDK版本信息。
     */
    public String getVersion()
    {
        return SearchCore.nativeGetVersion();
    }

    /**
     * 重置关键字检索功能。
     * @param typeCodes POI分类信息数组。
     * @param adCode 行政区划编码。注：SDK暂不对行政区划编码的有效性进行检验，如输入“110300”不存在的行政区划编码
     * @param adLevel 行政区划编码的等级。请参见 {@link com.unistrong.api.services.localsearch.ADCodeLevel }。
     * @param searchType 搜索类型，是名称关键字搜索还是地址关键字搜索。请参加 {@link com.unistrong.api.services.localsearch.SearchType }。
     * @return 重置是否成功。 0代表失败， 1代表成功,-1表示鉴权失败。
     */
    public int resetNameSearch(String [] typeCodes, String adCode, int adLevel, int searchType)
    {
        if(!initover)
            return 0;

        int typecount = (typeCodes == null) ? 0 : typeCodes.length;
        return SearchCore.nativeResetNameSearch(typeCodes, typecount, adCode, adLevel, searchType);
    }

    /**
     * 关键字搜索输入查找内容。
     * @param inputs 查找内容。
     * @return 可能获得的参考记录数,-1表示鉴权失败。
     */
    public int inputName(String inputs)
    {
        if(!initover)
            return 0;
        return SearchCore.nativeInputName(inputs);
    }

    /**
     * 开始关键字检索，获得实际可获得的记录数。
     * @return 返回可获得的实际记录数，最大值是100,-1表示鉴权失败。
     */
    public int startNameSearch()
    {
        if(!initover)
            return 0;
        return SearchCore.nativeStartNameSearch();
    }

    /**
     * 根据索引，获得关键字检索的结果记录。
     * @param index 检索索引。
     * @return 搜索结果值,NULL表示鉴权失败。
     */
    public SearchResultInfo getNameRecordByIndex(int index)
    {
        if(!initover)
            return null;

        return SearchCore.nativeGetNameRecordByIndex(index);
    }



    /**
     * 重置首拼检索功能。
     * @param typeCodes POI分类信息数组。
     * @param adCode 行政区划编码。注：SDK暂不对行政区划编码的有效性进行检验，如输入“110300”不存在的行政区划编码
     * @param adLevel 行政区划编码的等级。请参见 {@link com.unistrong.api.services.localsearch.ADCodeLevel }。
     * @param searchType 搜索类型，是名称首拼搜索还是地址首拼搜索。请参加 {@link com.unistrong.api.services.localsearch.SearchType }。
     * @return 重置是否成功。 0代表失败， 1代表成功,-1表示鉴权失败。
     */
    public int resetFirstLetterSearch(String [] typeCodes, String adCode, int adLevel, int searchType)
    {
        if(!initover)
            return 0;

        int typecount = (typeCodes == null) ? 0 : typeCodes.length;
        return SearchCore.nativeResetFirstLetterSearch(typeCodes, typecount, adCode, adLevel, searchType);
    }

    /**
     * 首拼搜索输入查找内容。
     * @param inputs 查找内容。注：输入的首拼需要大写，如皇冠假日酒店的首拼"HGJRJD"。
     * @return 可能获得的参考记录数,-1表示鉴权失败。
     */
    public int inputFirstLetters(String inputs)
    {
        if(!initover)
            return 0;
        return SearchCore.nativeInputFirstLetters(inputs);
    }

    /**
     * 开始首拼检索，获得实际可获得的记录数。
     * @return 返回可获得的实际记录数，最大值是100,-1表示鉴权失败。
     */
    public int startFirstLetterSearch()
    {
        if(!initover)
            return 0;
        return SearchCore.nativeStartFirstLetterSearch();
    }

    /**
     * 根据索引，获得首拼检索的结果记录。
     * @param index 检索索引。
     * @return 搜索结果值,NULL表示鉴权失败。
     */
    public SearchResultInfo getFirstLetterRecordByIndex(int index)
    {
        if(!initover)
            return null;

        return SearchCore.nativeGetFirstLetterRecordByIndex(index);
    }

    /**
     * 根据坐标查询ADCODE。
     * @param longitude 坐标经度,如116.123
     * @param latitude 坐标经度,39.456
     * @return 可能获得的参考ADCODE,0表示查询失败,-1表示鉴权失败。
     */
    public int getAdcode(double longitude,double latitude)
    {
        if(!initover)
            return 0;

        return SearchCore.nativeGetAdcode(longitude, latitude);
    }
}
