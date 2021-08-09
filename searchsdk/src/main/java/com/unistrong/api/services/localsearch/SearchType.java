package com.unistrong.api.services.localsearch;

/**
 * 搜索类型枚举。
 */
public class SearchType {
    /**
     * 0 - POI名称首字母检索。
     */
    private final static int SEARCH_TYPE_POIFIRSTLETTER = 0;

    /**
     * 1 - POI地址首字母检索。
     */
    private final static int SEARCH_TYPE_ADDRFIRSTLETTER = 1;

    /**
     * 2 - POI名称关键字检索。
     */
    public final static int SEARCH_TYPE_POINAME = 2;

    /**
     * 3 - POI地址关键字检索。
     */
    public final static int SEARCH_TYPE_ADDRNAME = 3;
}
