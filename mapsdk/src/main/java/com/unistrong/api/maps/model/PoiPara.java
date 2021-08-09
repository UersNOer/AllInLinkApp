package com.unistrong.api.maps.model;

/**
 * 唤起地图周边搜索功能的参数类。
 */
 class PoiPara
{
  private LatLng a;
  private String b;

    /**
     * 获取周边检索中心点。
     * @return 周边检索中心点。
     */
  public LatLng getCenter()
  {
    return this.a;
  }

    /**
     *设置poi周边检索中心点。
     * @param center poi周边检索中心点
     */
  public void setCenter(LatLng center)
  {
    this.a = center;
  }

    /**
     * 获取关键字。
     * @return 关键字。
     */
  public String getKeywords()
  {
    return this.b;
  }

    /**
     *设置poi 检索关键字”|”分割。
     * @param paramString - 关键字。
     */
  public void setKeywords(String paramString)
  {
    this.b = paramString;
  }
}
