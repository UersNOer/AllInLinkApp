package com.unistrong.api.mapcore;

import java.util.concurrent.CopyOnWriteArrayList;

class MapMessageQueueDecode
{
  MapDelegateImp map; // a
  
  public MapMessageQueueDecode(MapDelegateImp map)
  {
    this.map = map;
  }
  
  public MapMessageQueueDecode() {}
  
  public synchronized void destory()
  {
    if (this.b != null)
    {
      this.b.clear();
      this.b = null;
    }
    if (this.c != null)
    {
      this.c.clear();
      this.c = null;
    }
  }
  
  private CopyOnWriteArrayList<CameraUpdateFactoryDelegate> b = new CopyOnWriteArrayList<CameraUpdateFactoryDelegate>(); //b
  private CopyOnWriteArrayList<MapMessageDecode> c = new CopyOnWriteArrayList<MapMessageDecode>(); //c
  
  public synchronized void addMapMessage(MapMessageDecode paramat) // a
  {
    this.map.setRunLowFrame(false);
    this.c.add(paramat);
    this.map.setRunLowFrame(false);
  }
  
  public MapMessageDecode getMapMessage() //b
  {
    if (c() == 0) {
      return null;
    }
    MapMessageDecode localat = (MapMessageDecode)this.c.get(0);
    this.c.remove(localat);
    return localat;
  }
  
  public synchronized int c() //c
  {
    return this.c.size();
  }
  
  public void addMessage(CameraUpdateFactoryDelegate paramo) // a
  {
    this.map.setRunLowFrame(false);
    this.b.add(paramo);
    this.map.setRunLowFrame(false);
  }
  
  public CameraUpdateFactoryDelegate getMessage() // d
  {
    if (e() == 0) {
      return null;
    }
    CameraUpdateFactoryDelegate localo = (CameraUpdateFactoryDelegate)this.b.get(0);
    this.b.remove(localo);
    this.map.setRunLowFrame(false);
    return localo;
  }
  
  public int e() //e
  {
    return this.b.size();
  }
  
  public void f() //f
  {
    this.b.clear();
  }
}
