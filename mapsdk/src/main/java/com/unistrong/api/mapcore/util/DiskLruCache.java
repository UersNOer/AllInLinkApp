package com.unistrong.api.mapcore.util;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 ******************************************************************************
 * Taken from the JB source code, can be found in:
 * libcore/luni/src/main/java/libcore/io/DiskLruCache.java or direct link:
 * https://android.googlesource.com/platform/libcore/+/android-4.1
 * .1_r1/luni/src/main/java/libcore/io/DiskLruCache.java
 ******************************************************************************
 *
 * A cache that uses a bounded amount of space on a filesystem. Each cache entry
 * has a string key and a fixed number of values. Values are byte sequences,
 * accessible as streams or files. Each value must be between {@code 0} and
 * {@code Integer.MAX_VALUE} bytes in length.
 *
 * <p>
 * The cache stores its data in a directory on the filesystem. This directory
 * must be exclusive to the cache; the cache may delete or overwrite files from
 * its directory. It is an error for multiple processes to use the same cache
 * directory at the same time.
 *
 * <p>
 * This cache limits the number of bytes that it will store on the filesystem.
 * When the number of stored bytes exceeds the limit, the cache will remove
 * entries in the background until the limit is satisfied. The limit is not
 * strict: the cache may temporarily exceed it while waiting for files to be
 * deleted. The limit does not include filesystem overhead or the cache journal
 * so space-sensitive applications should set a conservative limit.
 *
 * <p>
 * Clients call {@link #edit} to create or update the values of an entry. An
 * entry may have only one editor at one time; if a value is not available to be
 * edited then {@link #edit} will return null.
 * <ul>
 * <li>When an entry is being <strong>created</strong> it is necessary to supply
 * a full set of values; the empty value should be used as a placeholder if
 * necessary.
 * <li>When an entry is being <strong>edited</strong>, it is not necessary to
 * supply data for every value; values default to their previous value.
 * </ul>
 * Every {@link #edit} call must be matched by a call to {@link Editor#commit}
 * or {@link Editor#abort}. Committing is atomic: a read observes the full set
 * of values as they were before or after the commit, but never a mix of values.
 *
 * <p>
 * Clients call {@link #get} to read a snapshot of an entry. The read will
 * observe the value at the time that {@link #get} was called. Updates and
 * removals after the call do not impact ongoing reads.
 *
 * <p>
 * This class is tolerant of some I/O errors. If files are missing from the
 * filesystem, the corresponding entries will be dropped from the cache. If an
 * error occurs while writing a cache value, the edit will fail silently.
 * Callers should handle other problems by catching {@code IOException} and
 * responding appropriately.
 */
public final class DiskLruCache
  implements Closeable
{
  static final Pattern a = Pattern.compile("[a-z0-9_-]{1,120}"); // a
  private final File directory; //c
  private final File journalFile; // d
  private final File journalFileTmp; //e
  private final File journalFileBkp; //f
  private final int appVersion; //g
  private long maxSize; //h
  private final int valueCount; //i
  private long j = 0L; //j
  private Writer writer; //k
  private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75F, true); //l
  private int redundantOpCount; //m
  private FileOperationListenerDecode n; //n
  
  public void a(FileOperationListenerDecode paramdh) // a
  {
    this.n = paramdh;
  }
  
  private long o = 0L; //o
  final ThreadPoolExecutor b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue()); //b
  private final Callable<Void> p = new df(this); //p
  
  private DiskLruCache(File paramFile, int paramInt1, int paramInt2, long paramLong)
  {
    this.directory = paramFile;
    this.appVersion = paramInt1;
    this.journalFile = new File(paramFile, "journal");
    this.journalFileTmp = new File(paramFile, "journal.tmp");
    this.journalFileBkp = new File(paramFile, "journal.bkp");
    this.valueCount = paramInt2;
    this.maxSize = paramLong;
  }

  /**
   * 缓存设置。
   * @param directory 缓存地址
   * @param appVersion 程序版本号
   * @param valueCount 一个KEY可以对应多少个缓存文件，基本传1.
   * @param maxSize 最多可以缓存多少字节的数据。
   * @return
   * @throws IOException
   */
  public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize) // a
    throws IOException
  {
    if (maxSize <= 0L) {
      throw new IllegalArgumentException("maxSize <= 0");
    }
    if (valueCount <= 0) {
      throw new IllegalArgumentException("valueCount <= 0");
    }
    File bkpFile = new File(directory, "journal.bkp");
    if (bkpFile.exists())
    {
      File journalFile = new File(directory, "journal");
      if (((File)journalFile).exists()) {
        bkpFile.delete();
      } else {
        rename(bkpFile, (File)journalFile, false);
      }
    }
    Object localObject = new DiskLruCache(directory, appVersion, valueCount, maxSize);
    if (((DiskLruCache)localObject).journalFile.exists()) {
      try
      {
        ((DiskLruCache)localObject).e();
        ((DiskLruCache)localObject).processJournal();
        ((DiskLruCache)localObject).writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(((DiskLruCache)localObject).journalFile, true), dj.a));
        
        return (DiskLruCache)localObject;
      }
      catch (IOException localIOException)
      {
        ((DiskLruCache)localObject).delete();
      }
    }
    directory.mkdirs();
    
    localObject = new DiskLruCache(directory, appVersion, valueCount, maxSize);
    ((DiskLruCache)localObject).g();
    return (DiskLruCache)localObject;
  }
  
  private void e() //e
    throws IOException
  {
	    int lineCount = 0;
	    StrictLineReader reader = new StrictLineReader(new FileInputStream(this.journalFile), dj.a);
	    try {
          String str1 = reader.readLine();
          String str2 = reader.readLine();
          String str3 = reader.readLine();
          String str4 = reader.readLine();
          String str5 = reader.readLine();
          if ((!("libcore.io.DiskLruCache".equals(str1))) || (!("1".equals(str2))) || (!(Integer.toString(this.appVersion).equals(str3))) || (!(Integer.toString(this.valueCount).equals(str4))) || (!("".equals(str5)))) {
            throw new IOException("unexpected journal header: [" + str1 + ", " + str2 + ", " + str4 + ", " + str5 + "]");
          }
          try {
            lineCount = 0;
            while (true) {
              this.readJournalLine(reader.readLine());
              lineCount++;
            }
          } catch (Exception e) {
            this.redundantOpCount = (lineCount - this.lruEntries.size());
          }
          return ;
        }finally
	    {
	      dj.close(reader);
	    }
  }
  
  private void readJournalLine(String line) // d
    throws IOException
  {
    int firstSpace = line.indexOf(' ');
    if (firstSpace == -1) {
      throw new IOException("unexpected journal line: " + line);
    }
    int keyBegin = firstSpace + 1;
    int secondSpace = line.indexOf(' ', keyBegin);
    String key;
    if (secondSpace == -1)
    {
      key = line.substring(keyBegin);
      if ((firstSpace == "REMOVE".length()) && (line.startsWith("REMOVE"))) {
        this.lruEntries.remove(key);
      }
    }
    else
    {
      key = line.substring(keyBegin, secondSpace);
    }
    Entry entry = (Entry)this.lruEntries.get(key);
    if (entry == null)
    {
      entry = new Entry(key);
      this.lruEntries.put(key, entry);
    }
    if ((secondSpace != -1) && (firstSpace == "CLEAN".length()) && (line.startsWith("CLEAN")))
    {
      String[] arrayOfString = line.substring(secondSpace + 1).split(" ");
      entry.readable = true;
      entry.currentEditor = null;
      entry.setLengths(arrayOfString);
    }
    else if ((secondSpace == -1) && (firstSpace == "DIRTY".length()) && (line.startsWith("DIRTY")))
    {
      entry.currentEditor = new Editor(entry);
    }
    else if ((secondSpace != -1) || (firstSpace != "READ".length()) || (!line.startsWith("READ")))
    {
      throw new IOException("unexpected journal line: " + line);
    }
  }
  
  private void processJournal() //f
    throws IOException
  {
    deleteIfExists(this.journalFileTmp);
    for (Iterator<Entry> iterator = this.lruEntries.values().iterator(); iterator.hasNext();)
    {
      Entry localc = (Entry)iterator.next();
      int t;
      if (localc.currentEditor == null) //上一句翻译
      {
        for (t = 0; t < this.valueCount; t++) {
          this.j += localc.lengths[t]; //上一句翻译
        }
      }
      else
      {
    	localc.currentEditor = null;  //上一句翻译
        for (t = 0; t < this.valueCount; t++)
        {
          deleteIfExists(localc.getCleanFile(t));
          deleteIfExists(localc.getDirtyFile(t));
        }
        iterator.remove();
      }
    }
  }
  
  private synchronized void g() //g
    throws IOException
  {
    if (this.writer != null) {
      this.writer.close();
    }
    BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), dj.a));
    try
    {
      bufWriter.write("libcore.io.DiskLruCache");
      bufWriter.write("\n");
      bufWriter.write("1");
      bufWriter.write("\n");
      bufWriter.write(Integer.toString(this.appVersion));
      bufWriter.write("\n");
      bufWriter.write(Integer.toString(this.valueCount));
      bufWriter.write("\n");
      bufWriter.write("\n");
      for (Entry localc : this.lruEntries.values()) {
        //if (Innerc.a(localc) != null) {
        if (localc.currentEditor != null) { //上一句翻译
          //localBufferedWriter.write("DIRTY " + c.c(localc) + '\n');
          bufWriter.write("DIRTY " + localc.key + '\n');//上一句翻译
        } else {
          //localBufferedWriter.write("CLEAN " + c.c(localc) + localc.a() + '\n');
          bufWriter.write("CLEAN " + localc.key + localc.gertLengths() + '\n'); //上一句翻译
        }
      }
    }
    finally
    {
      bufWriter.close();
    }
    if (this.journalFile.exists()) {
      rename(this.journalFile, this.journalFileBkp, true);
    }
    rename(this.journalFileTmp, this.journalFile, false);
    this.journalFileBkp.delete();
    
    this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), dj.a));
  }
  
  private static void deleteIfExists(File paramFile)    throws IOException
  {
    if ((paramFile.exists()) && (!paramFile.delete())) {
      throw new IOException();
    }
  }
  
  private static void rename(File paramFile1, File paramFile2, boolean paramBoolean) // a
    throws IOException
  {
    if (paramBoolean) {
      deleteIfExists(paramFile2);
    }
    if (!paramFile1.renameTo(paramFile2)) {
      throw new IOException();
    }
  }
  
  public synchronized Snapshot get(String key) // a
    throws IOException
  {
    isClose();
    e(key);
    Entry localc = (Entry)this.lruEntries.get(key);
    if (localc == null) {
      return null;
    }
    //if (!Innerc.changeBearing(localc)) {
    if (!localc.readable) { //上一句翻译
      return null;
    }
    InputStream[] arrayOfInputStream = new InputStream[this.valueCount];
    try
    {
      for (int i1 = 0; i1 < this.valueCount; i1++) {
        arrayOfInputStream[i1] = new FileInputStream(localc.getCleanFile(i1));
      }
    }
    catch (FileNotFoundException e)
    {
      for (int i2 = 0; i2 < this.valueCount; i2++)
      {
        if (arrayOfInputStream[i2] == null) {
          break;
        }
        dj.close(arrayOfInputStream[i2]);
      }
      return null;
    }
    this.redundantOpCount += 1;
    this.writer.append("READ " + key + '\n');
    if (h()) {
      this.b.submit(this.p);
    }
    //return new Snapshot(paramString, Innerc.e(localc), arrayOfInputStream, Innerc.b(localc), null);
    return new Snapshot(key, localc.sequenceNumber, arrayOfInputStream, localc.lengths); //上一句翻译
  }
  
  public Editor edit(String key) //b
    throws IOException
  {
    return a(key, -1L);
  }
  
  private synchronized Editor a(String key, long paramLong) // a
    throws IOException
  {
    isClose();
    e(key);
    Entry localc = (Entry)this.lruEntries.get(key);
    //if ((paramLong != -1L) && ((localc == null) || (Innerc.e(localc) != paramLong))) {
    if ((paramLong != -1L) && ((localc == null) || (localc.sequenceNumber != paramLong))) {//上一句翻译
      return null;
    }
    if (localc == null)
    {
      //localc = new Innerc(paramString, null);
      localc = new Entry(key);//上一句翻译
      this.lruEntries.put(key, localc);
    }
    //else if (Innerc.a(localc) != null)
    else if (localc.currentEditor != null)
    {
      return null;
    }
    // a locala = new a(localc, null);
    Editor locala = new Editor(localc);//上一句翻译
    //Innerc.a(localc, locala);
    localc.currentEditor = locala;//上一句翻译
    
    this.writer.write("DIRTY " + key + '\n');
    this.writer.flush();
    return locala;
  }
  
  private synchronized void completeEdit(Editor parama, boolean paramBoolean) // a
    throws IOException
  {
    //Innerc localc = a.a(parama);
    Entry localc = parama.entry; //上一句翻译
    //if (Innerc.a(localc) != parama) {
    if (localc.currentEditor != parama) {//上一句翻译
      throw new IllegalStateException();
    }
    //if ((paramBoolean) && (!Innerc.changeBearing(localc))) {
    if ((paramBoolean) && (!localc.readable)) {//上一句翻译
      for (int i1 = 0; i1 < this.valueCount; i1++)
      {
        //if (a.b(parama)[i1] == 0)
        if (!parama.c[i1])//上一句翻译
        {
          parama.abort();
          throw new IllegalStateException("Newly created entry didn't create value for index " + i1);
        }
        if (!localc.getDirtyFile(i1).exists())
        {
          parama.abort();
          return;
        }
      }
    }
    for (int i1 = 0; i1 < this.valueCount; i1++)
    {
      File localFile1 = localc.getDirtyFile(i1);
      if (paramBoolean)
      {
        if (localFile1.exists())
        {
          File localFile2 = localc.getCleanFile(i1);
          localFile1.renameTo(localFile2);
          
          //long l1 = Innerc.b(localc)[i1];
          long l1 = localc.lengths[i1];//上一句翻译
          long l2 = localFile2.length();
          //Innerc.b(localc)[i1] = l2;
          localc.lengths[i1] = l2;//上一句翻译
          this.j = (this.j - l1 + l2);
        }
      }
      else {
        deleteIfExists(localFile1);
      }
    }
    this.redundantOpCount += 1;
    //Innerc.a(localc, null);
    localc.currentEditor = null;//上一句翻译
    //if ((Innerc.changeBearing(localc) | paramBoolean))
    if ( localc.readable | paramBoolean )//上一句翻译
    {
    	//Innerc.a(localc, true);
    	localc.readable = true;//上一句翻译
      //this.k.write("CLEAN " + Innerc.c(localc) + localc.a() + '\n');
      this.writer.write("CLEAN " + localc.key + localc.gertLengths() + '\n');//上一句翻译
      if (paramBoolean) {
    	  //Innerc.a(localc, this.o++);
    	  localc.sequenceNumber = this.o++;//上一句翻译
      }
    }
    else
    {
      //this.l.remove(Innerc.c(localc));
      //this.k.write("REMOVE " + c.c(localc) + '\n');
      this.lruEntries.remove(localc.key);//上两句翻译
      this.writer.write("REMOVE " + localc.key + '\n');
    }
    this.writer.flush();
    if ((this.j > this.maxSize) || (h())) {
      this.b.submit(this.p);
    }
  }
  
  private boolean h() //h
  {
    int i1 = 2000;
    return (this.redundantOpCount >= 2000) && (this.redundantOpCount >= this.lruEntries.size());
  }
  
  public synchronized boolean remove(String paramString) //c
    throws IOException
  {
    isClose();
    e(paramString);
    Entry localc = (Entry)this.lruEntries.get(paramString);
    //if ((localc == null) || (Innerc.a(localc) != null)) {
    if ((localc == null) || (localc.currentEditor != null)) {//上一句翻译
      return false;
    }
    for (int i1 = 0; i1 < this.valueCount; i1++)
    {
      File localFile = localc.getCleanFile(i1);
      if ((localFile.exists()) && (!localFile.delete())) {
        throw new IOException("failed to delete " + localFile);
      }
      //this.j -= Innerc.b(localc)[i1];
      this.j -= localc.lengths[i1];//上一句翻译
      //Innerc.b(localc)[i1] = 0L;
      localc.lengths[i1] = 0L;//上一句翻译
    }
    this.redundantOpCount += 1;
    this.writer.append("REMOVE " + paramString + '\n');
    this.lruEntries.remove(paramString);
    if (h()) {
      this.b.submit(this.p);
    }
    return true;
  }
  
  public synchronized boolean isClosed() // a
  {
    return this.writer == null;
  }
  
  private void isClose() //i
  {
    if (this.writer == null) {
      throw new IllegalStateException("cache is closed");
    }
  }
  
  public synchronized void flush() //b
    throws IOException
  {
    isClose();
    j();
    this.writer.flush();
  }
  
  public synchronized void close()
    throws IOException
  {
    if (this.writer == null) {
      return;
    }
    
    /*for (Innerc localc : new ArrayList(this.l.values())) {
      if (Innerc.a(localc) != null) {
    	  Innerc.a(localc).isZoomControlsEnabled();
      }
    }*/ //如下代码是从jeb中拷贝出来的，为本段注释代码的实现
    
    Iterator v1 = new ArrayList(this.lruEntries.values()).iterator();
    while(v1.hasNext()) {
        Entry v0_1 = (Entry) v1.next();
        if(v0_1.currentEditor != null) {
        	v0_1.currentEditor.abort();
        }
    }
    
    j();
    this.writer.close();
    this.writer = null;
  }
  
  private void j() //j
    throws IOException
  {
    while (this.j > this.maxSize)
    {
      Map.Entry localEntry = (Map.Entry)this.lruEntries.entrySet().iterator().next();
      
      String str = (String)localEntry.getKey();
      remove(str);
      if (this.n != null) {
        this.n.a(str);
      }
    }
  }
  
  public void delete() //c
    throws IOException
  {
    close();
    dj.a(this.directory);
  }
  
  private void e(String key) //e
  {
    Matcher localMatcher = a.matcher(key);
    if (!localMatcher.matches()) {
      throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + key + "\"");
    }
  }
  
  public final class Snapshot //b
    implements Closeable
  {
    private final String key; //b
    private final long sequenceNumber; //c
    private final InputStream[] ins; // d
    private final long[] e; //e
    
    private Snapshot(String paramString, long paramLong, InputStream[] paramArrayOfInputStream, long[] paramArrayOfLong)
    {
      this.key = paramString;
      this.sequenceNumber = paramLong;
      this.ins = paramArrayOfInputStream;
      this.e = paramArrayOfLong;
    }
    
    public InputStream getInputStream(int paramInt)
    {
      return this.ins[paramInt];
    }
    
    public void close()
    {
      for (InputStream localInputStream : this.ins) {
        dj.close(localInputStream);
      }
    }
  }
  
  final static class dg extends OutputStream {
    dg() {
      super();
    }
    public void write(int arg1) throws IOException {

    }

  }
  private static final OutputStream q = new dg();
  public final class Editor
  {
    private final Entry entry;
    private final boolean[] c;
    private boolean hasErrors;
    private boolean e;
    
    private Editor(Entry paramc)
    {
      this.entry = paramc;
      //this.c = (DiskLruCacheDecode.Innerc.changeBearing(paramc) ? null : new boolean[DiskLruCacheDecode.e(DiskLruCacheDecode.this)]);
      this.c = (paramc.readable ? null : new boolean[valueCount]); //上一句的翻译
    }
    
    public OutputStream newOutputStream(int index)     throws IOException
    {
      //if ((paramInt < 0) || (paramInt >= DiskLruCacheDecode.e(DiskLruCacheDecode.this))) {
      if ((index < 0) || (index >= valueCount)) {//上一句的翻译
        //throw new IllegalArgumentException("Expected index " + paramInt + " to " + "be greater than 0 and less than the maximum value count " + "of " + DiskLruCacheDecode.setRunLowFrame(DiskLruCacheDecode.this));
        throw new IllegalArgumentException("Expected index " + index + " to " + "be greater than 0 and less than the maximum value count " + "of " + valueCount); // 上一句翻译
      }
      synchronized (DiskLruCache.this)
      {
        //if (DiskLruCacheDecode.c.a(this.b) != this) {
        if (entry.currentEditor != this) { //上一句翻译
          throw new IllegalStateException();
        }
        //if (!DiskLruCacheDecode.c.changeBearing(this.b)) {
        if (!entry.readable) { //上一句翻译
          this.c[index] = true;
        }
        File localFile = this.entry.getDirtyFile(index);
        FileOutputStream localFileOutputStream;
        try
        {
          localFileOutputStream = new FileOutputStream(localFile);
          return new FaultHidingOutputStream(localFileOutputStream);
        }
        catch (FileNotFoundException localFileNotFoundException1)
        {
          //DiskLruCacheDecode.f(DiskLruCacheDecode.this).mkdirs();
          directory.mkdirs(); //上一句翻译
          try
          {
            localFileOutputStream = new FileOutputStream(localFile);
          }
          catch (FileNotFoundException notfound)
          {
            return q;
          }
        }
        //return new a(localFileOutputStream, null);
        return new FaultHidingOutputStream(localFileOutputStream); //上一句翻译
      }
    }
    /**
     * Commits this edit so it is visible to readers. This releases the edit
     * lock so another edit may be started on the same key.
     */
    public void commit()
      throws IOException
    {
      if (this.hasErrors)
      {
    	completeEdit(this, false);
        remove(this.entry.key);
      }
      else
      {
    	completeEdit(this, true);
      }
      this.e = true;
    }
    /**
     * Aborts this edit. This releases the edit lock so another edit may be
     * started on the same key.
     */
    public void abort()
      throws IOException
    {
    	completeEdit(this, false);
    }
    
    private class FaultHidingOutputStream
      extends FilterOutputStream
    {
      private FaultHidingOutputStream(OutputStream out)
      {
        super(out);
      }
      
      public void write(int oneByte)
      {
        try
        {
          this.out.write(oneByte);
        }
        catch (IOException localIOException)
        {
          hasErrors = true;
        }
      }
      
      public void write(byte[] buf, int offset, int length)
      {
        try
        {
          this.out.write(buf, offset, length);
        }
        catch (IOException localIOException)
        {
        	hasErrors = true;
        }
      }
      
      public void close()
      {
        try
        {
          this.out.close();
        }
        catch (IOException localIOException)
        {
        	hasErrors = true;
        }
      }
      
      public void flush()
      {
        try
        {
          this.out.flush();
        }
        catch (IOException localIOException)
        {
          hasErrors = true;
        }
      }
    }
  }
  
  private final class Entry //c
  {
    private final String key;
    /** Lengths of this entry's files. */
    private final long[] lengths;
    /** True if this entry has ever been published */
    private boolean readable;
    /** The ongoing edit or null if this entry is not being edited. */
    private Editor currentEditor;
    /**
     * The sequence number of the most recently committed edit to this
     * entry.
     */
    private long sequenceNumber;
    
    private Entry(String key)
    {
      this.key = key;
      //this.c = new long[DiskLruCacheDecode.e(DiskLruCacheDecode.this)];
      this.lengths = new long[valueCount]; //上一句翻译
    }
    
    public String gertLengths()
      throws IOException
    {
      StringBuilder result = new StringBuilder();
      for (long size : this.lengths) {
        result.append(' ').append(size);
      }
      return result.toString();
    }
    /**
     * Set lengths using decimal numbers like "10123".
     */
    private void setLengths(String[] strings)
      throws IOException
    {
      //if (paramArrayOfString.length != DiskLruCacheDecode.e(DiskLruCacheDecode.this)) {
      if (strings.length != valueCount ) { //上一句翻译
        throw invalidLengths(strings);
      }
      try
      {
        for (int i = 0; i < strings.length; i++) {
          this.lengths[i] = Long.parseLong(strings[i]);
        }
      }
      catch (NumberFormatException e)
      {
        throw invalidLengths(strings);
      }
    }
    
    private IOException invalidLengths(String[] strings)
      throws IOException
    {
      throw new IOException("unexpected journal line: " + Arrays.toString(strings));
    }
    
    public File getCleanFile(int i)
    {
      //return new File(DiskLruCacheDecode.f(DiskLruCacheDecode.this), this.b + "." + paramInt);
      return new File(directory, this.key + "." + i);//上一句翻译
    }
    
    public File getDirtyFile(int i)
    {
      //return new File(DiskLruCacheDecode.f(DiskLruCacheDecode.this), this.b + "." + paramInt + ".tmp");
      return new File(directory, this.key + "." + i + ".tmp");
    }
  }
  
  
  
  
  
  
  
  class df
  implements Callable<Void>
	{
	  DiskLruCache a;
	  df(DiskLruCache paramde) { a = paramde;}
	  
	  public Void a()
	    throws Exception
	  {
	    synchronized (this.a)
	    {
	      //if (DiskLruCacheDecode.a(this.a) == null) {
	      if ( a.writer == null) { //上一句翻译
	        return null;
	      }
	      //de.b(this.a);
	      a.j();//上一句翻译
	      //if (DiskLruCacheDecode.c(this.a))
	      if (a.h())//上一句翻译
	      {
	        //DiskLruCacheDecode.changeBearing(this.a);
	        //DiskLruCacheDecode.a(this.a, 0);
	        a.g(); //上两句翻译
	        a.redundantOpCount = 0;
	      }
	    }
	    return null;
	  }

	@Override
	public Void call() throws Exception {
		// TODO Auto-generated method stub
		return this.a();
	}
	}
   
  
}
