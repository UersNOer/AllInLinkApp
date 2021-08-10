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
public class MapFragment extends Fragment
{
    /**
     * 地图对象。
     */
    private MapController map;
    private IMapFragmentDelegate fragmentDelegate;

    /**
     * 使用默认的选项创建MapFragment。
     * @return 一个MapFragment对象。
     */
    public static MapFragment newInstance()
    {
        return newInstance(new MapOptions());
    }

    /**
     * 根据用户传入的LMapOptions 创建MapFragment。
     * @param options - 地图初始化的LMapOptions对象。
     * @return 一个MapFragment对象。
     */
    public static MapFragment newInstance(MapOptions options)
    {
        MapFragment fragment = new MapFragment();
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
        fragment.setArguments(bundle);
        return fragment;
    }

    protected IMapFragmentDelegate getMapFragmentDelegate()
    {
        if (this.fragmentDelegate == null) {
            this.fragmentDelegate = new MapFragmentDelegateImp(MapFragmentDelegateImp.SURFACE_VIEW);
        }
        if (getActivity() != null) {
            this.fragmentDelegate.setContext(getActivity());
        }
        return this.fragmentDelegate;
    }

    /**
     * 返回一个MapFragment 所使用LMap 的对象。
     * @return Map 对象，如果MapFragment 初始化失败，则返回null。
     */
    public MapController getMap() throws UnistrongException {
        IMapFragmentDelegate fragmentDelegate = getMapFragmentDelegate();
        if (fragmentDelegate == null) {
            return null;
        }
        IMapDelegate mapDelegate;
        try
        {
            mapDelegate = fragmentDelegate.getMapDelegate();
        }
        catch (RemoteException localRemoteException)
        {
            throw new RuntimeRemoteException(localRemoteException);
        }
        if (mapDelegate == null) {
            return null;
        }
        if (this.map == null) {
            this.map = new MapController(mapDelegate);
        }
        return this.map;
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
    /**
     * 用户重载这个方法时必须调用父类的这个方法，用来用来创建地图。
     * 覆盖:
     * onCreate 在类中 android.app.Fragment
     */
    @Override
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
    /**
     * 用户重载这个方法时必须调用父类的这个方法，用来创建地图view。
     * 覆盖:
     * onCreateView 在类中 android.app.Fragment
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        try
        {
            if (bundle == null) {
                bundle = getArguments();
            }
            return getMapFragmentDelegate().onCreateView(layoutInflater, viewGroup, bundle);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (UnistrongException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法，用来启动地图刷新。
     * 覆盖:
     * onResume 在类中 android.app.Fragment
     */
    @Override
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

    /**
     * 用户重载这个方法时必须调用父类的这个方法，暂停地图刷新。
     * 覆盖:
     * onPause 在类中 android.app.Fragment
     */
    @Override
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

    /**
     * 用户重载这个方法时必须调用父类的这个方法，销毁view。
     * 覆盖:
     * onDestroyView 在类中 android.app.Fragment
     */
    @Override
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

    /**
     * 用户重载这个方法时必须调用父类的这个方法，释放地图资源。
     * 覆盖:
     * onDestroy 在类中 android.app.Fragment
     */
    @Override
    public void onDestroy()
    {
        try
        {
            IMapFragmentDelegate fragmentDelegate = getMapFragmentDelegate();
            fragmentDelegate.onDestroy();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法，低电量模式。
     * 指定者:
     * onLowMemory 在接口中 android.content.ComponentCallbacks
     * 覆盖:
     * onLowMemory 在类中 android.app.Fragment
     */
    @Override
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

    /**
     * 用户重载这个方法时必须调用父类的这个方法，保存地图状态。
     * 覆盖:
     * onSaveInstanceState 在类中 android.app.Fragment
     * @param bundle
     */
    @Override
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

    /**
     * 是在是否显示，在fragment切换的时候可以使用，或者想隐藏MapFragment的时候可以使用
     * 覆盖:
     * setUserVisibleHint 在类中 android.app.Fragment
     * @param isVisibleToUser true：显示； false：不显示。
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        if (isVisibleToUser) {
            getMapFragmentDelegate().setVisible(View.VISIBLE);
        } else {
            getMapFragmentDelegate().setVisible(View.GONE);
        }
    }
}