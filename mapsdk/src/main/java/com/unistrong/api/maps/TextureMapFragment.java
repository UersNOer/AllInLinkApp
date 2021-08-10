package com.unistrong.api.maps;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.unistrong.api.mapcore.IMapDelegate;
import com.unistrong.api.mapcore.IMapFragmentDelegate;
import com.unistrong.api.mapcore.MapFragmentDelegateImp;
import com.unistrong.api.maps.model.RuntimeRemoteException;

/**
 * MapFragment 是一个地图的容器。这个碎片（fragment）是在APP 中显示地图最简单的方法。MapFragment 类已经自动处理了一个View 运行的生命同期。用户可以在activity 的layout 文件里加入一段简单的XML 代码来实现MapFragment。
 */
public class TextureMapFragment
  extends Fragment
{
  private MapController lmap;
  private IMapFragmentDelegate iMapFragmentDelegate;

    /**
     * 使用默认的选项创建MapFragment。
     * @return 一个TextureMapFragment对象。
     */
  public static TextureMapFragment newInstance()
  {
    return newInstance(new MapOptions());
  }

    /**
     * 根据用户传入的MapOptions 创建MapFragment。
     * @param options  - 地图初始化的AMapOptions对象。
     * @return 一个TextureMapFragment对象。
     */
  public static TextureMapFragment newInstance(MapOptions options)
  {
    TextureMapFragment textureMapFragment = new TextureMapFragment();
    Bundle bundle = new Bundle();
    try
    {
      Parcel parcel = Parcel.obtain();
        options.writeToParcel(parcel, 0);
      bundle.putByteArray("MapOptions", parcel.marshall());
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    textureMapFragment.setArguments(bundle);
    return textureMapFragment;
  }
  
  protected IMapFragmentDelegate getMapFragmentDelegate()
  {
    if (this.iMapFragmentDelegate == null) {
      this.iMapFragmentDelegate = new MapFragmentDelegateImp(MapFragmentDelegateImp.TEXTURE_VIEW);
    }
    if (getActivity() != null) {
      this.iMapFragmentDelegate.setContext(getActivity());
    }
    return this.iMapFragmentDelegate;
  }

    /**
     * 返回一个TextureMapFragment 所使用LMap 的对象。
     * @return 返回LMap 对象，返回一个TextureMapFragment 初始化失败，则返回null。
     */
  public MapController getMap() throws UnistrongException {
    IMapFragmentDelegate localaf = getMapFragmentDelegate();
    if (localaf == null) {
      return null;
    }
    IMapDelegate localaa;
    try
    {
      localaa = localaf.getMapDelegate();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
    if (localaa == null) {
      return null;
    }
    if (this.lmap == null) {
      this.lmap = new MapController(localaa);
    }
    return this.lmap;
  }
  
  public void onAttach(Activity activity)
  {
    super.onAttach(activity);
  }
  
  public void onInflate(Activity activity, AttributeSet attrs, Bundle bundle)
  {
    super.onInflate(activity, attrs, bundle);
    try
    {
      getMapFragmentDelegate().onInflate(activity, new MapOptions(), bundle);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
  
  public void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    try
    {
      getMapFragmentDelegate().onCreate(bundle);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle)
  {
    try
    {
      if (bundle == null) {
        bundle = getArguments();
      }
      return getMapFragmentDelegate().onCreateView(inflater, viewGroup, bundle);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    } catch (UnistrongException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public void onResume()
  {
    super.onResume();
    try
    {
      getMapFragmentDelegate().onResume();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
  
  public void onPause()
  {
    super.onPause();
    try
    {
      getMapFragmentDelegate().onPause();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
  
  public void onDestroyView()
  {
    try
    {
      getMapFragmentDelegate().onDestroyView();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    super.onDestroyView();
  }
  
  public void onDestroy()
  {
    try
    {
      IMapFragmentDelegate localaf = getMapFragmentDelegate();
      localaf.onDestroy();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    super.onDestroy();
  }
  
  public void onLowMemory()
  {
    super.onLowMemory();
    try
    {
      getMapFragmentDelegate().onLowMemory();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }
  
  public void onSaveInstanceState(Bundle bundle)
  {
    try
    {
      getMapFragmentDelegate().onSaveInstanceState(bundle);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    super.onSaveInstanceState(bundle);
  }
  
  public void setArguments(Bundle bundle)
  {
    super.setArguments(bundle);
  }
  
  public void setUserVisibleHint(boolean isVisibleToUser)
  {
    if (isVisibleToUser) {
      getMapFragmentDelegate().setVisible(View.VISIBLE);
    } else {
      getMapFragmentDelegate().setVisible(View.GONE);
    }
  }
}
