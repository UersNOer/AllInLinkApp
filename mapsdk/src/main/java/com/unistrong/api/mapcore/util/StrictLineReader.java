package com.unistrong.api.mapcore.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class StrictLineReader//di
  implements Closeable
{
  private final InputStream inputStream;
  private final Charset charset;
  private byte[] c;
  private int d;
  private int e;
  
  public StrictLineReader(InputStream paramInputStream, Charset paramCharset)
  {
    this(paramInputStream, 8192, paramCharset);
  }
  
  public StrictLineReader(InputStream paramInputStream, int paramInt, Charset paramCharset)
  {
    if ((paramInputStream == null) || (paramCharset == null)) {
      throw new NullPointerException();
    }
    if (paramInt < 0) {
      throw new IllegalArgumentException("capacity <= 0");
    }
    if (!paramCharset.equals(dj.a)) {
      throw new IllegalArgumentException("Unsupported encoding");
    }
    this.inputStream = paramInputStream;
    this.charset = paramCharset;
    this.c = new byte[paramInt];
  }
  
  public void close()
    throws IOException
  {
    synchronized (this.inputStream)
    {
      if (this.c != null)
      {
        this.c = null;
        this.inputStream.close();
      }
    }
  }
  
  public String readLine()
    throws IOException
  {
    synchronized (this.inputStream)
    {
      if (this.c == null) {
        throw new IOException("LineReader is closed");
      }
      if (this.d >= this.e) {
        b();
      }
      for (int i = this.d; i != this.e; i++) {
        if (this.c[i] == 10)
        {
          int j = (i != this.d) && (this.c[(i - 1)] == 13) ? i - 1 : i;
          String str = new String(this.c, this.d, j - this.d, this.charset.name());
          
          this.d = (i + 1);
          return str;
        }
      }
      ByteArrayOutputStream local1 = new ByteArrayOutputStream(this.e - this.d + 80)
      {
        public String toString()
        {
          int i = (this.count > 0) && (this.buf[(this.count - 1)] == 13) ? this.count - 1 : this.count;
          try
          {
            //return new String(this.buf, 0, i, di.a(di.this).name());
            return new String(this.buf, 0, i, StrictLineReader.this.charset.name()); // 前一个循环用的是b, 此处原来用的是a明显不正确
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException)
          {
            throw new AssertionError(localUnsupportedEncodingException);
          }
        }
      };
      local1.write(this.c, this.d, this.e - this.d);
      
      this.e = -1;
      b();
      for (int j = this.d; j != this.e; j++) {
        if (this.c[j] == 10)
        {
          if (j != this.d) {
            local1.write(this.c, this.d, j - this.d);
          }
          this.d = (j + 1);
          return local1.toString();
        }
      }
    }
    
    return ""; //这个是新增的语句，反编译后没有
  }
  
  private void b()
    throws IOException
  {
    int i = this.inputStream.read(this.c, 0, this.c.length);
    if (i == -1) {
      throw new EOFException();
    }
    this.d = 0;
    this.e = i;
  }
}
