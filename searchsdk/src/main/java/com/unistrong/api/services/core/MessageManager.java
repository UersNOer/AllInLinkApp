package com.unistrong.api.services.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.unistrong.api.services.busline.BusLineResult;
import com.unistrong.api.services.busline.BusLineSearch;
import com.unistrong.api.services.busline.BusStationResult;
import com.unistrong.api.services.busline.BusStationSearch;
import com.unistrong.api.services.cloud.CloudDatasetSearch;
import com.unistrong.api.services.cloud.CloudDatasetSearchResult;
import com.unistrong.api.services.cloud.CloudSearchResult;
import com.unistrong.api.services.cloud.CloudSearch;
import com.unistrong.api.services.cloud.CloudStorage;
import com.unistrong.api.services.cloud.CloudStorageResult;
import com.unistrong.api.services.district.DistrictResult;
import com.unistrong.api.services.district.DistrictSearch;
import com.unistrong.api.services.geocoder.GeocodeResult;
import com.unistrong.api.services.geocoder.GeocodeSearch;
import com.unistrong.api.services.geocoder.RegeocodeResult;
import com.unistrong.api.services.help.Inputtips;
import com.unistrong.api.services.help.Tip;
import com.unistrong.api.services.poisearch.ComplexSearch;
import com.unistrong.api.services.poisearch.ComplexSearchResult;
import com.unistrong.api.services.poisearch.PoiItem;
import com.unistrong.api.services.poisearch.PoiSearch;
import com.unistrong.api.services.poisearch.XGPoiResult;
import com.unistrong.api.services.route.BusRouteResult;
import com.unistrong.api.services.route.DriveRouteResult;
import com.unistrong.api.services.route.RouteSearch;
import com.unistrong.api.services.route.WalkRouteResult;

import java.util.List;

public class MessageManager extends Handler {
	private static MessageManager messageManager = null;

	public static final int MESSAGE_TYPE_GEOCODE = 2;
	public static final int MESSAGE_TYPE_POISEARCH = 3;
	public static final int MESSAGE_TYPE_DISTRICT = 4;
	public static final int MESSAGE_TYPE_DRIVEROUTE = 5;
	public static final int MESSAGE_TYPE_WALKROUTE = 6;
	public static final int MESSAGE_TYPE_INPUTTIPS = 7;
	public static final int MESSAGE_TYPE_BUSROUTE = 8;
	public static final int MESSAGE_TYPE_BUSLINE = 9;
	public static final int MESSAGE_TYPE_BUSSTATIONE = 10;

    public static final int MESSAGE_TYPE_POIIDSEARCH = 11;
	public static final int MESSAGE_TYPE_CLOUDBBOXSEARCH = 12;
	public static final int MESSAGE_TYPE_CLOUDCONDITIONSEARCH = 13;
	public static final int MESSAGE_TYPE_CLOUDIDSEARCH = 14;
	public static final int MESSAGE_TYPE_CLOUDSTORAGE_ADD= 15;
	public static final int MESSAGE_TYPE_CLOUDSTORAGE_ADDSET = 16;
	public static final int MESSAGE_TYPE_CLOUDSTORAGE_UPDATA = 17;
	public static final int MESSAGE_TYPE_CLOUDSTORAGE_DEL= 18;
	public static final int MESSAGE_TYPE_CLOUDSTORAGE_DELSET = 19;
	public static final int MESSAGE_TYPE_CLOUDDATASET_SEARCHID = 20;
	public static final int MESSAGE_TYPE_CLOUDDATASET_SEARCHPAGE = 21;
	public static final int MESSAGE_TYPE_CLOUDDATASET_SEARCHALL = 22;
	public static final int MESSAGE_TYPE_COMPLEXSEARCH = 23;

	public static final int MESSAGE_GEOCODE_GEOCODE = 200;
	public static final int MESSAGE_GEOCODE_REGEOCODE = 201;
	public static final int MESSAGE_DISTRICT = 400;

	public static synchronized MessageManager getInstance() {
		if (messageManager == null) {
			if ((Looper.myLooper() == null)
					|| (Looper.myLooper() != Looper.getMainLooper())) {
				messageManager = new MessageManager(Looper.getMainLooper());
			} else
				messageManager = new MessageManager();
		}

		return messageManager;
	}

	MessageManager() {

	}

	MessageManager(Looper paramLooper) {
		super(paramLooper);
	}

	public void handleMessage(Message paramMessage) {
			switch (paramMessage.arg1) {
			case MESSAGE_TYPE_GEOCODE:
				HandleGeocodeMessage(paramMessage);
				break;
			case MESSAGE_TYPE_POISEARCH:
				HandlePOISearchMessage(paramMessage);
				break;
			case MESSAGE_TYPE_DISTRICT:
				HandleDistrictMessage(paramMessage);
				break;
			case MESSAGE_TYPE_WALKROUTE:
				HandleWalkRouteSearchMessage(paramMessage);
				break;

			case MESSAGE_TYPE_DRIVEROUTE:
				HandleDriveRouteMessage(paramMessage);
				break;
			case MESSAGE_TYPE_INPUTTIPS:
				HandleInputtipsMessage(paramMessage);
				break;

			case MESSAGE_TYPE_BUSROUTE:
				HandleBusRouteMessage(paramMessage);
				break;

			case MESSAGE_TYPE_BUSLINE:
				HandleBusLineMessage(paramMessage);
				break;
			case MESSAGE_TYPE_BUSSTATIONE:
				HandleBusStationMessage(paramMessage);
				break;
            case MESSAGE_TYPE_POIIDSEARCH:
                HandlePOIIDSearchMessage(paramMessage);
                break;
			case MESSAGE_TYPE_CLOUDBBOXSEARCH:
				handleCloudBBoxSearchMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDCONDITIONSEARCH:
				handleCloudConditionSearchMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDIDSEARCH:
				handleCloudCIdSearchMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDSTORAGE_ADD:
				handleCloudStorageAddMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDSTORAGE_ADDSET:
				handleCloudStorageAddSetMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDSTORAGE_UPDATA:
				handleCloudStorageUpdataMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDSTORAGE_DEL:
				handleCloudStorageDelMessage(paramMessage);
				break;
			case MESSAGE_TYPE_CLOUDSTORAGE_DELSET:
				handleCloudStorageDelSetMessage(paramMessage);
				break;
				case MESSAGE_TYPE_CLOUDDATASET_SEARCHID:
					handleCloudDatassetSearchIDMessage(paramMessage);
					break;
				case MESSAGE_TYPE_CLOUDDATASET_SEARCHPAGE:
					handleCloudDatassetSearchPageMessage(paramMessage);
					break;
				case MESSAGE_TYPE_CLOUDDATASET_SEARCHALL:
					handleCloudDatassetSearchAllMessage(paramMessage);
					break;
				case MESSAGE_TYPE_COMPLEXSEARCH:
					handleComplexSearchIDMessage(paramMessage);
					break;
			}
	}

	private void handleComplexSearchIDMessage(Message paramMessage){
		Object localObj1 = null;
		ComplexSearch.OnComplexSearchListener complexSearchListener;
		Object localObj2;

		localObj1 = (ComplexSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		complexSearchListener = ((ComplexSearchWrapper)localObj1).listener;
		if (complexSearchListener == null){
			return;
		}
		localObj2 = ((ComplexSearchWrapper)localObj1).result;
		complexSearchListener.onComplexSearched((ComplexSearchResult) localObj2, paramMessage.arg2);
	}

	private void handleCloudDatassetSearchIDMessage(Message paramMessage){
		Object localObj1 = null;
		CloudDatasetSearch.OnCloudDatasetSearchListener cloudSearchListener;
		Object localObj2;

		localObj1 = (CloudDatasetSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		cloudSearchListener = ((CloudDatasetSearchWrapper)localObj1).listener;
		if (cloudSearchListener == null){
			return;
		}
		localObj2 = ((CloudDatasetSearchWrapper)localObj1).result;
		cloudSearchListener.onIDSearched((CloudDatasetSearchResult) localObj2, paramMessage.arg2);
	}

	private void handleCloudDatassetSearchPageMessage(Message paramMessage){
		Object localObj1 = null;
		CloudDatasetSearch.OnCloudDatasetSearchListener cloudSearchListener;
		Object localObj2;

		localObj1 = (CloudDatasetSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		cloudSearchListener = ((CloudDatasetSearchWrapper)localObj1).listener;
		if (cloudSearchListener == null){
			return;
		}
		localObj2 = ((CloudDatasetSearchWrapper)localObj1).result;
		cloudSearchListener.onPageSearched((CloudDatasetSearchResult) localObj2, paramMessage.arg2);
	}

	private void handleCloudDatassetSearchAllMessage(Message paramMessage){
		Object localObj1 = null;
		CloudDatasetSearch.OnCloudDatasetSearchListener cloudSearchListener;
		Object localObj2;

		localObj1 = (CloudDatasetSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		cloudSearchListener = ((CloudDatasetSearchWrapper)localObj1).listener;
		if (cloudSearchListener == null){
			return;
		}
		localObj2 = ((CloudDatasetSearchWrapper)localObj1).result;
		cloudSearchListener.onAllSearched((CloudDatasetSearchResult) localObj2, paramMessage.arg2);
	}

	private void handleCloudStorageDelSetMessage(Message paramMessage) {
		Object localObject;
		CloudStorage.OnCloudStorageListener listener;
		CloudStorageResult result;
		localObject = (CloudStorageWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		listener = ((CloudStorageWrapper) localObject).listener;
		if (listener == null) {
			return;
		}
		result = null;
		result = ((CloudStorageWrapper) localObject).result;
		if(result==null){
			listener.onDelSet(paramMessage.arg2);
			return;
		}
		listener.onDelSet(Integer.valueOf(result.getStatus()));
	}

	private void handleCloudStorageDelMessage(Message paramMessage) {
		Object localObject;
		CloudStorage.OnCloudStorageListener listener;
		CloudStorageResult result;
		localObject = (CloudStorageWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		listener = ((CloudStorageWrapper) localObject).listener;
		if (listener == null) {
			return;
		}
		result = null;
		result = ((CloudStorageWrapper) localObject).result;
		if(result==null){
			listener.onDel(new long[]{},paramMessage.arg2);
			return;
		}
		listener.onDel(result.getId(),Integer.valueOf(result.getStatus()));
	}

	private void handleCloudStorageUpdataMessage(Message paramMessage) {
		Object localObject;
		CloudStorage.OnCloudStorageListener listener;
		CloudStorageResult result;
		localObject = (CloudStorageWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		listener = ((CloudStorageWrapper) localObject).listener;
		if (listener == null) {
			return;
		}
		result = null;
		result = ((CloudStorageWrapper) localObject).result;
		if(result==null){
			listener.onUpdata(new long[]{},paramMessage.arg2);
			return;
		}
		listener.onUpdata(result.getId(),Integer.valueOf(result.getStatus()));
	}

	private void handleCloudStorageAddSetMessage(Message paramMessage) {
		Object localObject;
		CloudStorage.OnCloudStorageListener listener;
		CloudStorageResult result;
		localObject = (CloudStorageWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		listener = ((CloudStorageWrapper) localObject).listener;
		if (listener == null) {
			return;
		}
		result = null;
		result = ((CloudStorageWrapper) localObject).result;
		if(result==null){
			listener.onAddSet(0,paramMessage.arg2);
			return;
		}
		listener.onAddSet(result.getDatasetId(),Integer.valueOf(result.getStatus()));
	}

	private void handleCloudStorageAddMessage(Message paramMessage) {
		Object localObject;
		CloudStorage.OnCloudStorageListener listener;
		CloudStorageResult result;
		localObject = (CloudStorageWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		listener = ((CloudStorageWrapper) localObject).listener;
		if (listener == null) {
			return;
		}
		result = null;
		result = ((CloudStorageWrapper) localObject).result;
		if(result==null){
			listener.onAdd(new long[]{},paramMessage.arg2);
			return;
		}
		listener.onAdd(result.getId(),Integer.valueOf(result.getStatus()));
	}


	private void HandleBusStationMessage(Message paramMessage) {

		Object localObject;
		BusStationSearch.OnBusStationSearchListener busStationListener;
		Object localObject2;
		localObject = (BusStationWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		busStationListener = ((BusStationWrapper) localObject).listener;
		if (busStationListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((BusStationWrapper) localObject).result;
		busStationListener.onBusStationSearched(
				(BusStationResult) localObject2, paramMessage.arg2);

	}

	private void HandleBusLineMessage(Message paramMessage) {

		Object localObject;
		BusLineSearch.OnBusLineSearchListener busLineListener;
		Object localObject2;
		localObject = (BusLineWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		busLineListener = ((BusLineWrapper) localObject).listener;
		if (busLineListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((BusLineWrapper) localObject).result;
		busLineListener.onBusLineSearched((BusLineResult) localObject2,
				paramMessage.arg2);

	}

	@SuppressWarnings("unchecked")
	private void HandleInputtipsMessage(Message paramMessage) {

		Object localObject;
		Inputtips.InputtipsListener inputListener;
		Object localObject2;
		localObject = (InputtipsWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		inputListener = ((InputtipsWrapper) localObject).listener;
		if (inputListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((InputtipsWrapper) localObject).result;
		inputListener.onGetInputtips((List<Tip>) localObject2,
				paramMessage.arg2);

	}

	private void HandleDistrictMessage(Message paramMessage) {
		Object localObject;
		DistrictSearch.OnDistrictSearchListener disListener;
		Object localObject2;

		localObject = (DistrictWrapper) paramMessage.obj;
		if (localObject == null) {
			return;
		}
		disListener = ((DistrictWrapper) localObject).listener;
		if (disListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((DistrictWrapper) localObject).result;
		disListener.onDistrictSearched((DistrictResult) localObject2,paramMessage.arg2);

	}

	private void handleCloudBBoxSearchMessage(Message paramMessage){
		Object localObj1 = null;
		CloudSearch.OnCloudSearchListener cloudSearchListener;
		Object localObj2;

		localObj1 = (CloudSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		cloudSearchListener = ((CloudSearchWrapper)localObj1).listener;
		if (cloudSearchListener == null){
			return;
		}
		localObj2 = ((CloudSearchWrapper)localObj1).result;
		cloudSearchListener.onBBoxSearched((CloudSearchResult) localObj2, paramMessage.arg2);
	}

	private void handleCloudConditionSearchMessage(Message paramMessage){
		Object localObj1 = null;
		CloudSearch.OnCloudSearchListener cloudSearchListener;
		Object localObj2;

		localObj1 = (CloudSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		cloudSearchListener = ((CloudSearchWrapper)localObj1).listener;
		if (cloudSearchListener == null){
			return;
		}
		localObj2 = ((CloudSearchWrapper)localObj1).result;
		cloudSearchListener.onConditionSearched((CloudSearchResult) localObj2, paramMessage.arg2);
	}

	private void handleCloudCIdSearchMessage(Message paramMessage){
		Object localObj1 = null;
		CloudSearch.OnCloudSearchListener cloudSearchListener;
		Object localObj2;

		localObj1 = (CloudSearchWrapper)paramMessage.obj;
		if (localObj1 == null){
			return;
		}
		cloudSearchListener = ((CloudSearchWrapper)localObj1).listener;
		if (cloudSearchListener == null){
			return;
		}
		localObj2 = ((CloudSearchWrapper)localObj1).result;
		cloudSearchListener.onIDSearched((CloudSearchResult) localObj2, paramMessage.arg2);
	}

	private void HandlePOIIDSearchMessage(Message paramMessage) {

		Object localObject1;
		PoiSearch.OnPoiSearchListener localOnPoiSearchListener;
		Object localObject2;
		localObject1 = (POIIDWrapper) paramMessage.obj;
		if (localObject1 == null) {
			return;
		}
		localOnPoiSearchListener = ((POIIDWrapper) localObject1).listener;
		if (localOnPoiSearchListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((POIIDWrapper) localObject1).result;
        localOnPoiSearchListener.onPoiItemSearched((List<XGPoiResult>) localObject2,  paramMessage.arg2);
	}


    private void HandlePOISearchMessage(Message paramMessage) {

        Object localObject1;
        PoiSearch.OnPoiSearchListener localOnPoiSearchListener;
        Object localObject2;
        localObject1 = (POIWrapper) paramMessage.obj;
        if (localObject1 == null) {
            return;
        }
        localOnPoiSearchListener = ((POIWrapper) localObject1).listener;
        if (localOnPoiSearchListener == null) {
            return;
        }
        localObject2 = null;
        localObject2 = ((POIWrapper) localObject1).result;
        localOnPoiSearchListener.onPoiSearched((XGPoiResult) localObject2,
                paramMessage.arg2);
    }

	private void HandleWalkRouteSearchMessage(Message paramMessage) {

		Object localObject1;
		RouteSearch.OnRouteSearchListener localOnPoiSearchListener;
		Object localObject2;
		localObject1 = (RouteWrapper) paramMessage.obj;
		if (localObject1 == null) {
			return;
		}
		localOnPoiSearchListener = ((RouteWrapper) localObject1).listener;
		if (localOnPoiSearchListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((RouteWrapper) localObject1).walkResult;
		localOnPoiSearchListener.onWalkRouteSearched(
				(WalkRouteResult) localObject2, paramMessage.arg2);
	}

	private void HandleDriveRouteMessage(Message paramMessage) {

		Object localObject1;
		RouteSearch.OnRouteSearchListener localOnPoiSearchListener;
		Object localObject2;
		localObject1 = (RouteWrapper) paramMessage.obj;
		if (localObject1 == null) {
			return;
		}
		localOnPoiSearchListener = ((RouteWrapper) localObject1).listener;
		if (localOnPoiSearchListener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((RouteWrapper) localObject1).driveResult;
		localOnPoiSearchListener.onDriveRouteSearched(
				(DriveRouteResult) localObject2, paramMessage.arg2);
	}

	private void HandleBusRouteMessage(Message paramMessage) {

		Object localObject1;
		RouteSearch.OnRouteSearchListener listener;
		Object localObject2;
		localObject1 = (RouteWrapper) paramMessage.obj;
		if (localObject1 == null) {
			return;
		}
		listener = ((RouteWrapper) localObject1).listener;
		if (listener == null) {
			return;
		}
		localObject2 = null;
		localObject2 = ((RouteWrapper) localObject1).busResult;
		listener.onBusRouteSearched((BusRouteResult) localObject2,
				paramMessage.arg2);
	}

	private void HandleGeocodeMessage(Message paramMessage) {
		Object localObject1;
		GeocodeSearch.OnGeocodeSearchListener localOnGeocodeSearchListener;
		Object localObject2;
		if (paramMessage.what == MESSAGE_GEOCODE_REGEOCODE) {
			localObject1 = (RegeocodeWrapper) paramMessage.obj;
			if (localObject1 == null) {
				return;
			}
			localOnGeocodeSearchListener = ((RegeocodeWrapper) localObject1).listener;
			if (localOnGeocodeSearchListener == null) {
				return;
			}
			localObject2 = null;

			localObject2 = ((RegeocodeWrapper) localObject1).result;
			localOnGeocodeSearchListener.onRegeocodeSearched(
					(RegeocodeResult) localObject2, paramMessage.arg2);
		} else if (paramMessage.what == MESSAGE_GEOCODE_GEOCODE) {
			localObject1 = (GeocodeWrapper) paramMessage.obj;
			if (localObject1 == null) {
				return;
			}
			localOnGeocodeSearchListener = ((GeocodeWrapper) localObject1).listener;
			if (localOnGeocodeSearchListener == null) {
				return;
			}
			localObject2 = null;

			localObject2 = ((GeocodeWrapper) localObject1).result;
			localOnGeocodeSearchListener.onGeocodeSearched(
					(GeocodeResult) localObject2, paramMessage.arg2);
		}
	}

	public static class RegeocodeWrapper {
		public RegeocodeResult result;
		public GeocodeSearch.OnGeocodeSearchListener listener;
	}

	public static class GeocodeWrapper {
		public GeocodeResult result;
		public GeocodeSearch.OnGeocodeSearchListener listener;
	}

	public static class POIWrapper {
		public XGPoiResult result;
		public PoiSearch.OnPoiSearchListener listener;
	}

    public static class POIIDWrapper {
        public List<PoiItem> result;
        public PoiSearch.OnPoiSearchListener listener;
    }

	public static class DistrictWrapper {
		public DistrictResult result;
		public DistrictSearch.OnDistrictSearchListener listener;
	}

	public static class RouteWrapper {
		public WalkRouteResult walkResult;
		public DriveRouteResult driveResult;
		public BusRouteResult busResult;
		public RouteSearch.OnRouteSearchListener listener;
	}

	public static class InputtipsWrapper {
		public List<Tip> result;
		public Inputtips.InputtipsListener listener;
	}

	public static class BusLineWrapper {
		public BusLineResult result;
		public BusLineSearch.OnBusLineSearchListener listener;
	}

	public static class BusStationWrapper {
		public BusStationResult result;
		public BusStationSearch.OnBusStationSearchListener listener;
	}

	public static class CloudSearchWrapper {
		public CloudSearchResult result;
		public CloudSearch.OnCloudSearchListener listener;
	}
	public static class CloudStorageWrapper{
		public CloudStorageResult result;
		public CloudStorage.OnCloudStorageListener listener;
	}
	public static class CloudDatasetSearchWrapper {
		public CloudDatasetSearchResult result;
		public CloudDatasetSearch.OnCloudDatasetSearchListener listener;
	}
	public static class ComplexSearchWrapper {
		public ComplexSearchResult result;
		public ComplexSearch.OnComplexSearchListener listener;
	}
}
