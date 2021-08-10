package com.leador.mapcore;

import java.util.Hashtable;

class d
{
  private Hashtable<String, c> gridnames = new Hashtable();
  int a = 0;
  long b;
  boolean c = true;
  
  public void a(String paramString)
  {
    this.gridnames.remove(paramString);
  }
  
  public boolean b(String paramString)
  {
    return this.gridnames.get(paramString) != null;
  }
  
  public void c(String gridName)
  {
    this.gridnames.put(gridName, new c(gridName, 0));
  }
  
  public void clearGrids()
  {
    this.gridnames.clear();
  }
  
  public d()
  {
    b();
  }
  
  public void b()
  {
    this.b = System.currentTimeMillis();
  }
}
