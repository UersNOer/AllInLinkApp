package com.leador.mapcore;

class e
{
  String a;
  int b;
  int c;
  int d = 0;
  
  public e(String paramString, int paramInt)
  {
    try
    {
      if ((paramString == null) || (paramString.length() == 0)) {
        return;
      }
      this.b = ((int)(System.currentTimeMillis() / 1000L));
      this.c = paramInt;
      this.a = paramString;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      this.a = null;
    }
  }
}
