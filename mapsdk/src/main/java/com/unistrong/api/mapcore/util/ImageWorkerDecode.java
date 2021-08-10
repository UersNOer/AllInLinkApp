package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.unistrong.api.mapcore.TileOverlayDelegateImp;
import java.lang.ref.WeakReference;

public abstract class ImageWorkerDecode {
	private ImageCacheDecode mImageCache; // a
	private ImageCacheDecode.ImageCacheParams mImageCacheParams; // b
	private boolean mExitTasksEarly = false; // e
	protected boolean mPauseWork = false; // c
	private final Object mPauseWorkLock = new Object(); // f
	protected Resources mResources; // d

	private static final int MESSAGE_CLEAR = 0;
	private static final int MESSAGE_INIT_DISK_CACHE = 1;
	private static final int MESSAGE_FLUSH = 2;
	private static final int MESSAGE_CLOSE = 3;

	protected ImageWorkerDecode(Context paramContext) {
		this.mResources = paramContext.getResources();
	}

	public void a(boolean paramBoolean,
			TileOverlayDelegateImp.TileCoordinate tile) // a->loadImage
	{
		if (tile == null) {
			return;
		}
		Bitmap localBitmap = null;
		if (this.mImageCache != null) {
			StringBuilder localObject = new StringBuilder();
			localObject.append(tile.X);
			localObject.append("-");
			localObject.append(tile.Y);
			localObject.append("-");
			localObject.append(tile.Zoom);
			localBitmap = this.mImageCache.getBitmapFromMemCache(localObject
					.toString());
		}
		if (localBitmap != null) {
			tile.getBitmapFromMemCache(localBitmap);
		} else {
			final BitmapWorkerTask task = new BitmapWorkerTask(tile);

			tile.task = task;

			task.executeOnExecutor(AsyncTaskDecode.DUAL_THREAD_EXECUTOR,
					new Boolean[] { Boolean.valueOf(paramBoolean) });
		}
	}

	public void a(ImageCacheDecode.ImageCacheParams parama) // a ->addImageCache
	{
		this.mImageCacheParams = parama;
		this.mImageCache = ImageCacheDecode.getInstance(this.mImageCacheParams);
		new CacheAsyncTask().execute(new Object[] { Integer.valueOf(1) });
	}

	public void setExitTasksEarly(boolean exitTasksEarly) // a
	{
		this.mExitTasksEarly = exitTasksEarly;
		setPauseWork(false);
	}

	protected abstract Bitmap processBitmap(Object paramObject); // a

	protected ImageCacheDecode getImageCache() // a
	{
		return this.mImageCache;
	}

	public static void cancelWork(TileOverlayDelegateImp.TileCoordinate tile) // a
	{
		BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(tile);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancel(true);
			LogManager.writeLog("ImageWorker", "cancelWork - cancelled work for " + tile,
					111);
		}
	}

	private static BitmapWorkerTask getBitmapWorkerTask(
			TileOverlayDelegateImp.TileCoordinate tile) // a
	{
		if (tile != null) {
			return tile.task;
		}
		return null;
	}

	public class BitmapWorkerTask // a
			extends AsyncTaskDecode<Boolean, Void, Bitmap> {
		private final WeakReference<TileOverlayDelegateImp.TileCoordinate> tileReference; // e

		public BitmapWorkerTask(
				TileOverlayDelegateImp.TileCoordinate parama) // a
		{
			this.tileReference = new WeakReference<TileOverlayDelegateImp.TileCoordinate>(parama);
		}

		protected Bitmap doInBackground(Boolean... paramVarArgs) {
			LogManager.writeLog("ImageWorker", "doInBackground - starting work", 111);

			boolean needDownload = paramVarArgs[0].booleanValue();

			TileOverlayDelegateImp.TileCoordinate tile = (TileOverlayDelegateImp.TileCoordinate) this.tileReference
					.get();
			if (tile == null) {
				return null;
			}
			StringBuilder localStringBuilder = new StringBuilder();
			localStringBuilder.append(tile.X);
			localStringBuilder.append("-");
			localStringBuilder.append(tile.Y);
			localStringBuilder.append("-");
			localStringBuilder.append(tile.Zoom);
			String str = localStringBuilder.toString();
			Bitmap tileBitmap = null;
			synchronized (mPauseWorkLock) {
				while (mPauseWork && (!isCancelled())) {
					try {
						mPauseWorkLock.wait();
					} catch (InterruptedException localInterruptedException) {
					}
				}
			}
			if ((mImageCache != null)
					&& (!isCancelled())
					&& (getAttachedTileCoordinate() != null)
					&& (! mExitTasksEarly)) {
				tileBitmap = mImageCache.getBitmapFromDiskCache(str);
			}
			if ((needDownload)
					&& (tileBitmap == null)
					&& (!isCancelled())
					&& (getAttachedTileCoordinate() != null)
					&& (! mExitTasksEarly )) {
				tileBitmap = processBitmap(tile);
			}
			if (tileBitmap != null) {
				if ( mImageCache != null) {
					 mImageCache.a(str,tileBitmap);
				}
			}
			LogManager.writeLog("ImageWorker", "doInBackground - finished work", 111);

			return tileBitmap;
		}

		protected void onPostExecute(Bitmap value) // a
		{
			if ((isCancelled()) || mExitTasksEarly ){ // (ImageWorkerDecode.setCompassEnabled(ImageWorkerDecode.this))) {
				value = null;
			}
			TileOverlayDelegateImp.TileCoordinate tile = getAttachedTileCoordinate();
			if ((value != null) && (!value.isRecycled())
					&& (tile != null)) {
				LogManager.writeLog("ImageWorker",
						"onPostExecute - setting bitmap: " + tile.toString(),
						111);

				tile.getBitmapFromMemCache(value);
			}
		}

		protected void onCancelled(Bitmap value) // b
		{
			super.onCancelled(value);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		private TileOverlayDelegateImp.TileCoordinate getAttachedTileCoordinate() // e
		{
			TileOverlayDelegateImp.TileCoordinate tile = tileReference.get();
			BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(tile); //ImageWorkerDecode.setZoomControlsEnabled(tile);
			if (this == bitmapWorkerTask) {
				return tile;
			}
			return null;
		}
	}

	public void setPauseWork(boolean paramBoolean) // b
	{
		synchronized (this.mPauseWorkLock) {
			this.mPauseWork = paramBoolean;
			if (!this.mPauseWork) {
				this.mPauseWorkLock.notifyAll();
			}
		}
	}

	protected class CacheAsyncTask // b
			extends AsyncTaskDecode<Object, Void, Void> {
		protected CacheAsyncTask() {
		} // b

		@Override
		protected Void doInBackground(Object... paramVarArgs) // d
		{
			switch (((Integer) paramVarArgs[0]).intValue()) {
			case MESSAGE_CLEAR:
				ImageWorkerDecode.this.clearCacheInternal();
				break;
			case MESSAGE_INIT_DISK_CACHE:
				ImageWorkerDecode.this.initDiskCacheInternal();
				break;
			case MESSAGE_FLUSH:
				ImageWorkerDecode.this.flushCacheInternal();
				break;
			case MESSAGE_CLOSE:
				ImageWorkerDecode.this.closeCacheInternal();
				break;
			}
			return null;
		}
	}

	protected void initDiskCacheInternal() // b
	{
		if (this.mImageCache != null) {
			this.mImageCache.initDiskCache();
		}
	}

	protected void clearCacheInternal() // c
	{
		if (this.mImageCache != null) {
			this.mImageCache.clearCache();
		}
	}

	protected void flushCacheInternal() // d
	{
		if (this.mImageCache != null) {
			this.mImageCache.flush();
		}
	}

	protected void closeCacheInternal() // e
	{
		if (this.mImageCache != null) {
			this.mImageCache.close();
			this.mImageCache = null;
		}
	}

	public void clearCache() // f
	{
		new CacheAsyncTask().execute(MESSAGE_CLEAR);
	}

	public void flushCache() // g
	{
		new CacheAsyncTask().execute(MESSAGE_FLUSH);
	}

	public void closeCache() // h
	{
		new CacheAsyncTask().execute(MESSAGE_CLOSE);
	}
}
