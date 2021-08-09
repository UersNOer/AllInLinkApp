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
 * TextureSupportMapFragment 是一个地图的容器。这个碎片（fragment）是在APP 中显示地图最简单的方法。TextureSupportMapFragment 类已经自动处理了一个View 运行的生命同期。用户可以在activity 的layout 文件里加入一段简单的XML 代码来实现TextureSupportMapFragment。示例如下： 使用这个类之前必须在Android项目里引入Android Support Library。
 */
public class TextureSupportMapFragment
  extends Fragment
{
  private MapController lmap;
  private IMapFragmentDelegate iMapFragmentDelegate;

    /**
     * 使用默认的选项创建TextureSupportMapFragment。
     * @return 新创建的TextureSupportMapFragment对象。
     */
  public static TextureSupportMapFragment newInstance()
  {
    return newInstance(new MapOptions());
  }

    /**
     * 根据用户传入的MapOptions 创建SupportMapFragment。
     * @param options - 地图初始化的options对象。
     * @return 一个TextureSupportMapFragment对象。
     */
  public static TextureSupportMapFragment newInstance(MapOptions options)
  {
    TextureSupportMapFragment localTextureSupportMapFragment = new TextureSupportMapFragment();
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
    localTextureSupportMapFragment.setArguments(localBundle);
    return localTextureSupportMapFragment;
  }
  
  protected IMapFragmentDelegate getMapFragmentDelegate()
  {
    if (this.iMapFragmentDelegate == null) {
      this.iMapFragmentDelegate = new MapFragmentDelegateImp(MapFragmentDelegateImp.TEXTURE_VIEW);
    }
    this.iMapFragmentDelegate.setContext(getActivity());
    return this.iMapFragmentDelegate;
  }

    /**
     * 返回一个TextureSupportMapFragment 所使用MapController 的对象。
     * @return 返回MapController 对象，如果TextureSupportMapFragment 初始化失败，则返回null。
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
    if (this.lmap == null) {
      this.lmap = new MapController(localaa);
    }
    return this.lmap;
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

  public void setUserVisibleHint(boolean paramBoolean)
  {
    super.setUserVisibleHint(paramBoolean);
    if (paramBoolean) {
      getMapFragmentDelegate().setVisible(0);
    } else {
      getMapFragmentDelegate().setVisible(8);
    }
  }
}
