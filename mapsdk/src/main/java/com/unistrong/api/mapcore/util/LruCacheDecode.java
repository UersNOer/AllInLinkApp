package com.unistrong.api.mapcore.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCacheDecode<K, V>
{
  private final LinkedHashMap<K, V> map; // a
  private int size; //b
  private int maxSize; //c
  private int putCount; // d
  private int createCount; //e
  private int evictionCount; //f
  private int hitCount; //g
  private int missCount; //h
  
  public LruCacheDecode(int maxSize)
  {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("maxSize <= 0");
    }
    this.maxSize = maxSize;
    this.map = new LinkedHashMap<K,V>(0, 0.75F, true);
  }
  
  public final V get(K key) // a
  {
    if (key == null) {
      throw new NullPointerException("key == null");
    }
    V mapValue;
    synchronized (this)
    {
    	mapValue = this.map.get(key);
      if (mapValue != null)
      {
        this.hitCount += 1;
        return mapValue;
      }
      this.missCount += 1;
    }
    
    V createdValue = create(key);
    if (createdValue == null) {
      return null;
    }
    synchronized (this)
    {
      this.createCount += 1;
      mapValue = this.map.put(key, createdValue);
      if (mapValue != null) {
        this.map.put(key, mapValue);
      } else {
        this.size += safeSizeOf(key, createdValue);
      }
    }
    if (mapValue != null)
    {
      entryRemoved(false, key, createdValue, mapValue);
      return mapValue;
    }
    trimToSize(this.maxSize);
    return createdValue;
  }
  
  public final V put(K paramK, V paramV) //b
  {
    if ((paramK == null) || (paramV == null)) {
      throw new NullPointerException("key == null || value == null");
    }
    V previous;
    synchronized (this)
    {
      this.putCount += 1;
      this.size += safeSizeOf(paramK, paramV);
      previous = this.map.put(paramK, paramV);
      if (previous != null) {
        this.size -= safeSizeOf(paramK, previous);
      }
    }
    if (previous != null) {
      entryRemoved(false, paramK, previous, paramV);
    }
    trimToSize(this.maxSize);
    return previous;
  }
  
  private void trimToSize(int paramInt) // a
  {
    //for (;;)
    while(true)
    {
      K key;
      V value;
      synchronized (this)
      {
        if (((this.size >= 0) && (this.map.isEmpty()) && (this.size != 0)) && (this.size <= paramInt)) {
          break;
        }
        Map.Entry<K, V> toEvict = null;
//        Iterator<Entry<K,V>> localIterator = this.map.entrySet().iterator();
//        if (localIterator.hasNext())
//        {
//          Entry<K,V> localEntry = (Entry<K,V>)localIterator.next();
//          toEvict = localEntry;
//          continue;
//        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            toEvict = entry;
        }
        if (toEvict == null) {
          break;
        }
        key = toEvict.getKey();
        value = toEvict.getValue();
        this.map.remove(key);
        this.size -= safeSizeOf(key, value);
        this.evictionCount += 1;
      }
      entryRemoved(true, key, value, null);
    }
  }
  
  protected void entryRemoved(boolean paramBoolean, K paramK, V paramV1, V paramV2) {} // a
  
  protected V create(K paramK) //
  {
    return null;
  }
  
  private int safeSizeOf(K key, V value) //c
  {
    int i = sizeOf(key, value);
    if (i < 0) {
      throw new IllegalStateException("Negative size: " + key + "=" + value);
    }
    return i;
  }
  
  protected int sizeOf(K paramK, V paramV) // a
  {
    return 1;
  }
  
  public final void evictAll() // a
  {
    trimToSize(-1);
  }
  
  @Override
  public final synchronized String toString()
  {
    int accesses = this.hitCount + this.missCount;
    int hitPercent = accesses != 0 ? (100 * this.hitCount / accesses) : 0;
    //return String.format("LruCache[maxSize=%changeBearing,hits=%changeBearing,misses=%changeBearing,hitRate=%changeBearing%%]", new Object[] {
    //  Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(j) });
    
    return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", maxSize , hitCount,  missCount, hitPercent);
  }
}
