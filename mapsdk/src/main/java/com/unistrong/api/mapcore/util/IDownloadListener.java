package com.unistrong.api.mapcore.util;

public abstract interface IDownloadListener//ah
{
  public abstract void startDownload();
  
  public abstract void onProgress(long paramLong1, long paramLong2);
  
  public abstract void downloadFinish();
  
  public abstract void onError(ExceptionType type);
  
  public abstract void onStopDownload();
  
  public static enum ExceptionType
  {
    map_exception,
    network_exception,
    file_io_exception,
    success_no_exception,
    cancel_no_exception,
    md5_exception;

  }
}
