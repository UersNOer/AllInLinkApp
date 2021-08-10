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
 * SupportMapFragment 是一个地图的容器。这个碎片（fragment）是在APP 中显示地图最简单的方法。SupportMapFragment 类已经自动处理了一个View 运行的生命同期。用户可以在activity 的layout 文件里加入一段简单的XML 代码来实现SupportMapFragment。示例如下： 使用这个类之前必须在Android项目里引入Android Support Library。
 */
public class SupportMapFragment
  extends Fragment
{
  private MapController map;
  private IMapFragmentDelegate iMapFragmentDelegate;

    /**
     * 使用默认的选项创建SupportMapFragment。
     * @return 新创建的SupportMapFragment对象。
     */
  public static SupportMapFragment newInstance()
  {
    return newInstance(new MapOptions());
  }

    /**
     * 根据用户传入的MapOptions 创建SupportMapFragment。
     * @param options  - 地图初始化的options对象。
     * @return 一个SupportMapFragment对象。
     */
  public static SupportMapFragment newInstance(MapOptions options)
  {
    SupportMapFragment localSupportMapFragment = new SupportMapFragment();
    Bundle localBundle = new Bundle();
    try
    {
      Parcel localParcel = Parcel.obtain();
        options.writeToParcel(localParcel, 0);
      localBundle.putByteArray("MapOptions", localParcel.marshall());
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    localSupportMapFragment.setArguments(localBundle);
    return localSupportMapFragment;
  }

  protected IMapFragmentDelegate getMapFragmentDelegate()
  {
    if (this.iMapFragmentDelegate == null) {
      this.iMapFragmentDelegate = new MapFragmentDelegateImp(MapFragmentDelegateImp.SURFACE_VIEW);
    }
    this.iMapFragmentDelegate.setContext(getActivity());
    return this.iMapFragmentDelegate;
  }

    /**
     * 返回一个SupportMapFragment 所使用LMap 的对象。
     * @return Map 对象，如果SupportMapFragment 初始化失败，则返回null。
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
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
    if (localaa == null) {
      return null;
    }
    if (this.map == null) {
      this.map = new MapController(localaa);
    }
    return this.map;
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
  }

  public void onInflate(Activity paramActivity, AttributeSet paramAttributeSet, Bundle paramBundle)
  {
    super.onInflate(paramActivity, paramAttributeSet, paramBundle);
    try
    {
      getMapFragmentDelegate().onInflate(paramActivity, new MapOptions(), paramBundle);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    try
    {
      if (paramBundle == null) {
        paramBundle = getArguments();
      }
      return getMapFragmentDelegate().onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
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
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
  }

  public void onPause()
  {
    super.onPause();
    try
    {
      getMapFragmentDelegate().onPause();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
  }

  public void onDestroyView()
  {
    try
    {
      getMapFragmentDelegate().onDestroyView();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
    super.onDestroyView();
  }

  public void onDestroy()
  {
    try
    {
      getMapFragmentDelegate().onDestroy();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
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
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
  }
  /**
   * 用户重载这个方法时必须调用父类的这个方法，保存地图状态。
   * 覆盖:
   * onSaveInstanceState 在类中 android.app.Fragment
   */
  @Override
  public void onSaveInstanceState(Bundle paramBundle)
  {
    try
    {
      getMapFragmentDelegate().onSaveInstanceState(paramBundle);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
    super.onSaveInstanceState(paramBundle);
  }

  public void setArguments(Bundle paramBundle)
  {
    super.setArguments(paramBundle);
  }

  /**
   * @deprecated
   * 设置是否显示，在fragment切换的时候可以使用，或者想隐藏MapFragment的时候可以使用
   * @param paramBoolean true：显示； false：不显示
     */
  public void setUserVisibleHint(boolean paramBoolean)
  {
    super.setUserVisibleHint(paramBoolean);
    if (paramBoolean) {
      getMapFragmentDelegate().setVisible(View.VISIBLE);
    } else {
      getMapFragmentDelegate().setVisible(View.GONE);
    }
  }
}
