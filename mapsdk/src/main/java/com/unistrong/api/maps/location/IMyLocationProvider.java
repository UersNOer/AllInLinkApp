package com.unistrong.api.maps.location;

import android.location.Location;

public interface IMyLocationProvider {
	boolean startLocationProvider(IMyLocationConsumer myLocationConsumer);

	void stopLocationProvider();

	Location getLastKnownLocation();

	void destroy();
}
