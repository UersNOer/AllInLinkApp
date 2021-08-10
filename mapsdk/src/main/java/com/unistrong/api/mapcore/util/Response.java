package com.unistrong.api.mapcore.util;

import java.util.List;
import java.util.Map;

public abstract interface Response
{
  public abstract void a();
  
  public abstract void a(Map<String, List<String>> paramMap, byte[] paramArrayOfByte);
  
  public abstract void a(IMMapCoreException parambl);
}
