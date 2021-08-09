package com.unistrong.api.services.core;

class SDKInfo//bv
{
  String version;
  public String useragen;
  String product;
  String username="";
  private boolean d = true;
  private String e = "standard";
  private String[] packagesNames = null;
  
  private SDKInfo(createSDKInfo parama)
  {
    this.version = parama.version; // a.destory(/*parama*/);
    this.product = parama.product; // a.isZoomControlsEnabled(/*parama*/);
    this.useragen = parama.useragen; // a.remove(/*parama*/);
    this.username = parama.username;
    this.d = parama.d; // a.isMyLocationButtonEnabled(/*parama*/);
    this.e = parama.e; // a.isScrollGesturesEnabled(/*parama*/);
    this.packagesNames = parama.sdkPackages; // a.isZoomGesturesEnabled(/*parama*/);
  }
  
  public static class createSDKInfo
  {
    private String version;
    private String product;
    private String useragen;
    private String username;
    private boolean d = true;
    private String e = "standard";
    private String[] sdkPackages = null;
    
    public createSDKInfo(String pruduct, String version, String useragen, String username)
    {
      this.version = version;
      this.useragen = useragen;
      this.product = pruduct;
      this.username = username;
    }
    
    public createSDKInfo a(boolean paramBoolean)
    {
      this.d = paramBoolean;
      return this;
    }
    
    public createSDKInfo a(String paramString)
    {
      this.e = paramString;
      return this;
    }
    
    public createSDKInfo setPackageName(String[] sdkPackages)
    {
      this.sdkPackages = ((String[])sdkPackages.clone());
      return this;
    }
    
    public SDKInfo a()
      throws Exception
    {
      if (this.sdkPackages == null) {
        throw new Exception("sdk packages is null");
      }
      //return new bv(this, null);
      return new SDKInfo(this);
    }
  }
  
  public void a(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  public String getProduct()
  {
    return this.product;
  }
  
  public String getVersion()
  {
    return this.version;
  }
  
  public String getUseragen()
  {
    return this.useragen;
  }
  
  public String d()
  {
    return this.e;
  }
  
  public boolean e()
  {
    return this.d;
  }
  
  public String[] getPackageNames()
  {
    return (String[])this.packagesNames.clone();
  }
}
