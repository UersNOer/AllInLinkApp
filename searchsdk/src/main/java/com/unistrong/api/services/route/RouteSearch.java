package com.unistrong.api.services.route;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类定义了一条路径。一条路径，可以是公交路径、步行路径或者自驾路径。根据构造时的参数不同，可以得到不同的路径。
 */
public class RouteSearch {
	public static final String WALK_MODE = "walking";
	public static final String DRIVE_MODE = "driving";
	public static final String BUS_MODE = "busing";

    /**
     * 最少时间。
     */
    public static final int BusDefault = 11;
    /**
     * 最省钱。
     */
    public static final int BusSaveMoney = 1;
    /**
     * 最少换乘。
     */
    public static final int BusLeaseChange = 13;
    /**
     * 最少步行。
     */
    public static final int BusLeaseWalk = 14;
    /**
     * 最舒适模式，乘坐有空调的车线；
     */
    public static final int BusComfortable = 4;
    /**
     * 不乘地铁。
     */
    public static final int BusNoSubway = 5;


    /**
     * 速度优先
     */
    public static final int DrivingDefault = 11;
    /**
     * 费用优先
     */
    public static final int DrivingSaveMoney = 0;
    /**
     * 距离优先
     */
    public static final int DrivingShortDistance = 12;
    /**
     * 不走高速
     */
    public static final int DrivingNoHighWay = 5;

    /**
     * 同时使用速度优先、费用优先、距离优先三个策略计算路径。
     */

    public static final int  DrivingMultiStrategy = 6;
    /**
     * 不走快速路
     */
    public static final int DrivingNoExpressways = 10;
    /**
     * 国道优先
     */
    public static final int DrivingOnNationalWay= 2;
    /**
     * 省道优先
     */
    public static final int DrivingOnProvincialWay  = 4;

    /**
     *最少时间
     */
    public static final int WalkDefault = 11;
    /**
     *最短距离
     */
    public static final int WalkShortDistance = 12;

	private Context context;
	private OnRouteSearchListener routeSearchListener;
	private Handler handler = null;

	public RouteSearch(android.content.Context context) {
		this.context = context.getApplicationContext();
		handler = MessageManager.getInstance();
	}

	/**
	 * 设置异步计算结果监听。
	 * 
	 * @param listener
	 *            异步计算结果监听。
	 */
	public void setRouteSearchListener(
			RouteSearch.OnRouteSearchListener listener) {
		this.routeSearchListener = listener;
	}

	/**
	 * 步行同步计算路径。
	 * 
	 * @param walkQuery
	 *            步行搜索参数。
	 * @return 返回步行路径计算结果。
	 * @throws UnistrongException
	 */
	public WalkRouteResult calculateWalkRoute(
			RouteSearch.WalkRouteQuery walkQuery) throws UnistrongException {
		WalkSearchServerHandler handle = new WalkSearchServerHandler(
				this.context, walkQuery, CoreUtil.getProxy(this.context), null);
		return handle.GetData();

	}

	/**
	 * 驾车同步计算路径。
	 * 
	 * @param driveQuery
	 *            驾车搜索参数。
	 * @return 返回驾车路径计算结果。
	 * @throws UnistrongException
	 */
	public DriveRouteResult calculateDriveRoute(
			RouteSearch.DriveRouteQuery driveQuery) throws UnistrongException {
		DriveSearchServerHandler handle = new DriveSearchServerHandler(
				this.context, driveQuery, CoreUtil.getProxy(this.context), null);
		return handle.GetData();
	}

	/**
	 * 公交同步计算路径。
	 * 
	 * @param busQuery
	 *            驾车搜索参数。
	 * @return 返回驾车路径计算结果。
	 * @throws UnistrongException
	 */
	public BusRouteResult calculateBusRoute(RouteSearch.BusRouteQuery busQuery)
			throws UnistrongException {
		BusSearchServerHandler handle = new BusSearchServerHandler(
				this.context, busQuery, CoreUtil.getProxy(this.context), null);
		return handle.GetData();
	}

	/**
	 * 步行异步计算路径。
	 * 
	 * @param walkQuery
	 *            步行搜索参数。
	 */
	public void calculateWalkRouteAsyn(
			final RouteSearch.WalkRouteQuery walkQuery) {
		new Thread(new Runnable() {
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();
				try {
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_WALKROUTE;
					localMessage.arg2 = 0;
					MessageManager.RouteWrapper locale = new MessageManager.RouteWrapper();
					localMessage.obj = locale;
					locale.listener = RouteSearch.this.routeSearchListener;
					locale.walkResult = RouteSearch.this
							.calculateWalkRoute(walkQuery);
				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
				} finally {
					if (null != RouteSearch.this.handler)
						RouteSearch.this.handler.sendMessage(localMessage);
				}
			}
		}).start();
	}

	/**
	 * 驾车异步计算路径。
	 * 
	 * @param driveQuery
	 *            驾车搜索参数。
	 */
	public void calculateDriveRouteAsyn(
			final RouteSearch.DriveRouteQuery driveQuery) {
		new Thread(new Runnable() {
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();
				try {
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_DRIVEROUTE;
					localMessage.arg2 = 0;
					MessageManager.RouteWrapper locale = new MessageManager.RouteWrapper();
					localMessage.obj = locale;
					locale.listener = RouteSearch.this.routeSearchListener;
					locale.driveResult = RouteSearch.this
							.calculateDriveRoute(driveQuery);
				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
				} finally {
					if (null != RouteSearch.this.handler)
						RouteSearch.this.handler.sendMessage(localMessage);
				}
			}
		}).start();
	}

	/**
	 * 公交异步计算路径。
	 * 
	 * @param busQuery
	 *            公交搜索参数。
	 */
	public void calculateBusRouteAsyn(final RouteSearch.BusRouteQuery busQuery) {
		new Thread(new Runnable() {
			public void run() {
				Message localMessage = MessageManager.getInstance()
						.obtainMessage();
				try {
					localMessage.arg1 = MessageManager.MESSAGE_TYPE_BUSROUTE;
					localMessage.arg2 = 0;
					MessageManager.RouteWrapper locale = new MessageManager.RouteWrapper();

                    localMessage.obj = locale;

					locale.listener = RouteSearch.this.routeSearchListener;
					locale.busResult = RouteSearch.this.calculateBusRoute(busQuery);

				} catch (UnistrongException unistrongException) {
					localMessage.arg2 = unistrongException.getErrorCode();
				} finally {
					if (null != RouteSearch.this.handler)
						RouteSearch.this.handler.sendMessage(localMessage);
				}
			}
		}).start();

	}

	/**
	 * 此类定义了公交路径规划查询路径的起终点。
	 */
	public static class BusRouteQuery implements Parcelable, Cloneable {
		private String mode = BUS_MODE;
		private FromAndTo fromAndTo;

		private String coord_Type;
		/**
		 * 导航策略。
		 */
		private int tactics = 11;

		public static final Creator<BusRouteQuery> CREATOR = new BusRouteQueryCreator<BusRouteQuery>();

		/**
		 * BusRouteQuery 构造函数。
		 */
		public BusRouteQuery() {

		}

		/**
		 * 序列化实现。
		 */
		public BusRouteQuery(Parcel paramParcel) {
			this.fromAndTo = ((RouteSearch.FromAndTo) paramParcel
					.readParcelable(RouteSearch.FromAndTo.class
							.getClassLoader()));
			this.tactics = paramParcel.readInt();
			this.coord_Type = paramParcel.readString();
			this.mode = paramParcel.readString();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeParcelable(this.fromAndTo, flags);
			dest.writeInt(this.tactics);
			dest.writeString(this.coord_Type);
			dest.writeString(this.mode);
		}
		/**
		 * BusRouteQuery 构造函数。
		 *
		 * @param fromAndTo
		 *            起终点坐标。
		 */
		public BusRouteQuery(FromAndTo fromAndTo) {
			super();
			this.fromAndTo = fromAndTo;
			if (CoreUtil.isNull(fromAndTo))
				try {
					throw new UnistrongException(
							UnistrongException.ERROR_NULL_PARAMETER);
				} catch (UnistrongException e) {
					throw new IllegalArgumentException("Empty Parameter ");
				}
		}


		/**
		 * BusRouteQuery 构造函数。
		 * 
		 * @param fromAndTo
		 *            起终点坐标。
		 * @param tactics
		 *            策略。
		 */
		public BusRouteQuery(FromAndTo fromAndTo, int tactics) {
			super();
			this.fromAndTo = fromAndTo;
			this.tactics = tactics;
		}


		/**
		 * 返回查询路径的起终点。
		 * 
		 * @return 查询路径的起终点。
		 */
		public FromAndTo getFromAndTo() {
			return this.fromAndTo;
		}

		/**
		 * 设置查询路径的起终点。
		 * 
		 * @param mFromAndTo
		 *            查询路径的起终点。
		 */
		public void setFromAndTo(FromAndTo mFromAndTo) {
			this.fromAndTo = mFromAndTo;
		}

		/**
		 * 返回坐标类型。
		 * 
		 * @return 坐标类型：
		 * @brief 参数取值 ：gcj02. 为国测局加密坐标 ;wgs84.为gps设备获取坐标， 默认为：gcj02。
		 */
		public String getCoordType() {
			return this.coord_Type;
		}

		/**
		 * 设置坐标类型
		 * 
		 * @param coordType
		 *            ：
		 * @brief 参数取值 ：gcj02. 为国测局加密坐标 ;wgs84.为gps设备获取坐标， 默认为：gcj02。
		 */
		public void setCoordType(String coordType) {
			this.coord_Type = coordType;
		}

		/**
		 * 设置导航策略
		 * 
		 * @return 导航策略值
		 */
		public int getTactics() {
			return this.tactics;
		}

		/**
		 * 设置导航策略。
		 * 
		 * @param tactics
		 *            导航策略。
		 * @brief 参数取值
		 *        ：11：表示最快捷模式，尽可能乘坐地铁和快速公交线路；1：最经济模式，尽可能省钱；13：表示最少换乘模式，尽可能减少换乘次数
		 *        ； 14：表示最少步行模式，尽可能减少步行距离；4：表示最舒适模式，乘坐有空调的车线；5：表示不乘地铁模式，不乘坐地铁线路。
		 *        默认值为11。
		 */
		public void setTactics(int tactics) {
			this.tactics = tactics;
		}



		public int hashCode() {
			int i = 31;
			int j = 1;
			j = i * j
					+ (this.fromAndTo == null ? 0 : this.fromAndTo.hashCode());
			j = i * j + this.tactics;
			j = i* j+ (this.coord_Type == null ? 0 : this.coord_Type.hashCode());
			j = i * j + (this.mode == null ? 0 : this.mode.hashCode());
			return j;
		}

		/**
		 * 比较两个查询条件是否相同。
		 * 
		 * @param obj
		 *            - 查询条件。
		 * @return 查询条件是否相同。
		 */
		public boolean equals(java.lang.Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BusRouteQuery localBusRouteQuery = (BusRouteQuery) obj;
			if (this.fromAndTo == null) {
				if (localBusRouteQuery.fromAndTo != null)
					return false;
			} else if (!this.fromAndTo.equals(localBusRouteQuery.fromAndTo))
				return false;
			if (this.coord_Type == null) {
				if (localBusRouteQuery.coord_Type != null)
					return false;
			} else if (!this.coord_Type.equals(localBusRouteQuery.coord_Type))
				return false;
			if (this.tactics != localBusRouteQuery.tactics)
				return false;
			if (this.mode == null) {
				if (localBusRouteQuery.mode != null)
					return false;
			} else if (!this.mode.equals(localBusRouteQuery.mode))
				return false;
			return true;

		}

		public BusRouteQuery clone() {
			BusRouteQuery query = new BusRouteQuery(this.fromAndTo.clone());
			query.setCoordType(this.getCoordType());
			query.setTactics(this.getTactics());
			return query;
		}

		@Override
		public int describeContents() {
			return 0;
		}



	}

	/**
	 * 此类定义了步行路径的起终点和计算路径的模式。
	 */
	public static class WalkRouteQuery implements Parcelable, Cloneable {
		private FromAndTo fromAndTo;
		private String coord_Type;
		private int tactics;
		public static final Creator<WalkRouteQuery> CREATOR = new WalkRouteQueryCreator<WalkRouteQuery>();

		/**
		 * WalkRouteQuery构造函数。
		 */
		public WalkRouteQuery() {
		}

		/**
		 * 序列化实现。
		 */
		public WalkRouteQuery(Parcel paramParcel) {
			this.fromAndTo = ((RouteSearch.FromAndTo) paramParcel
					.readParcelable(RouteSearch.FromAndTo.class
							.getClassLoader()));
			this.coord_Type = paramParcel.readString();
			this.tactics = paramParcel.readInt();
		}

		/**
		 * WalkRouteQuery构造函数。
		 * 
		 * @param fromAndTo
		 *            起终点坐标。
		 */
		public WalkRouteQuery(RouteSearch.FromAndTo fromAndTo) {
			this.fromAndTo = fromAndTo;
			if (CoreUtil.isNull(fromAndTo))
				try {
					throw new UnistrongException(
							UnistrongException.ERROR_NULL_PARAMETER);
				} catch (UnistrongException e) {
					throw new IllegalArgumentException("Empty Parameter ");
				}
		}

        /**
         * WalkRouteQuery构造函数。
         * @param fromAndTo   起终点坐标。
         * @param tactics 导航策略。
         */

        public WalkRouteQuery(RouteSearch.FromAndTo fromAndTo,int tactics) {
            this.fromAndTo = fromAndTo;
            this.tactics = tactics;
            if (CoreUtil.isNull(fromAndTo))
                try {
                    throw new UnistrongException(
							UnistrongException.ERROR_NULL_PARAMETER);
                } catch (UnistrongException e) {
                    throw new IllegalArgumentException("Empty Parameter ");
                }
        }

		/**
		 * 返回查询路径的起终点。
		 * 
		 * @return 查询路径的起终点。
		 */
		public FromAndTo getFromAndTo() {
			return fromAndTo;
		}

		/**
		 * 设置查询路径的起终点。
		 *
		 */
		public void setFromAndTo(FromAndTo fromAndTo) {
			this.fromAndTo = fromAndTo;
		}
		/**
		 * 返回坐标类型。
		 * 
		 * @return 坐标类型 。
		 */
		public String getCoordType() {
			return coord_Type;
		}



		/**
		 * 设置坐标类型。
		 * 
		 * @param coordType
		 * @brief 参数取值 ：gcj02. 为国测局加密坐标 ;wgs84.为gps设备获取坐标， 默认为：gcj02。
		 */
		public void setCoordType(String coordType) {
			this.coord_Type = coordType;
		}

		/**
		 * 返回导航策略。
		 * 
		 * @return 导航策略。
		 */
		public int getTactics() {
			return tactics;
		}

		/**
		 * 设置导航策略。
		 * 
		 * @param tactics
		 *            导航策略。
		 * @brief 参数取值 ：11.为最少时间，12.为最短路径， 默认为:11。
		 */
		public void setTactics(int tactics) {
			this.tactics = tactics;
		}



		public int hashCode() {
			int i = 31;
			int j = 1;
			j = i * j
					+ (this.fromAndTo == null ? 0 : this.fromAndTo.hashCode());
			j = i * j + this.tactics;
			j = i
					* j
					+ (this.coord_Type == null ? 0 : this.coord_Type.hashCode());
			return j;

		}

		/**
		 * 比较两个查询条件是否相同。
		 * 
		 * @param obj
		 *            - 查询条件。
		 * @return 查询条件是否相同。
		 */
		public boolean equals(java.lang.Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WalkRouteQuery localDriveRouteQuery = (WalkRouteQuery) obj;
			if (this.fromAndTo == null) {
				if (localDriveRouteQuery.fromAndTo != null)
					return false;
			} else if (!this.fromAndTo.equals(localDriveRouteQuery.fromAndTo))
				return false;
			if (this.coord_Type == null) {
				if (localDriveRouteQuery.coord_Type != null)
					return false;
			} else if (!this.coord_Type.equals(localDriveRouteQuery.coord_Type))
				return false;
			if (this.tactics != localDriveRouteQuery.tactics)
				return false;
			return true;

		}

		public void writeToParcel(android.os.Parcel dest, int flags) {
			dest.writeParcelable(this.fromAndTo, flags);
			dest.writeString(coord_Type);
			dest.writeInt(this.tactics);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public WalkRouteQuery clone() {
			try {
				super.clone();
				WalkRouteQuery query = new WalkRouteQuery(this.fromAndTo.clone());
				query.setTactics(this.getTactics());
				query.setCoordType(this.getCoordType());
			} catch (CloneNotSupportedException localCloneNotSupportedException) {
			}
			return new WalkRouteQuery(this.fromAndTo);
		}
	}

	/**
	 * 此类定义了驾车路径查询规划。
	 */

	public static class DriveRouteQuery implements Parcelable, Cloneable {
		private FromAndTo fromAndTo;
		private String coord_Type;// 坐标类型
		private int tactics = 11;// 导航策略
		private List<LatLonPoint> passedByPoints;// 途径点坐标
		private List<List<LatLonPoint>> avoidpolygons;// 避让区域坐标
		// 避让道路名称，支持输入一条道路名称。
		private String avoidroad;
		public static final Creator<DriveRouteQuery> CREATOR = new DriveRouteQueryCreator<DriveRouteQuery>();

		/**
		 * DriveRouteQuery构造函数。
		 */
		public DriveRouteQuery() {

		}

		/**
		 * DriveRouteQuery构造函数。
		 * 
		 * @param fromAndTo
		 *            起终点坐标。
		 */
		public DriveRouteQuery(RouteSearch.FromAndTo fromAndTo) {
			this.fromAndTo = fromAndTo;
			if (CoreUtil.isNull(fromAndTo))
				try {
					throw new UnistrongException(
							UnistrongException.ERROR_NULL_PARAMETER);
				} catch (UnistrongException e) {
					throw new IllegalArgumentException("Empty Parameter ");
				}
		}

		/**
		 * DriveRouteQuery构造函数。
		 * 
		 * @param fromAndTo
		 *            起终点坐标。
		 * @param tactics
		 *            策略。
		 */
		public DriveRouteQuery(RouteSearch.FromAndTo fromAndTo, int tactics) {
			this.fromAndTo = fromAndTo;
            this.tactics = tactics;

		}

		/**
		 * 返回坐标类型
		 * 
		 * @return 坐标类型
		 * @brief 参数取值 ：gcj02. 为国测局加密坐标 wgs84.为gps设备获取坐标， 默认为：gcj02.
		 */
		public String getCoordType() {
			return coord_Type;
		}

		/**
		 * 设置坐标类型.
		 * 
		 * @param coordType
		 * @brief 参数取值 ：gcj02. 为国测局加密坐标 wgs84.为gps设备获取坐标， 默认为：gcj02.
		 */
		public void setCoordType(String coordType) {
			this.coord_Type = coordType;
		}

		/**
		 * 设置导航策略。
		 * 
		 * @param tactics
		 * @brief 参数取值
		 *        ：10。不走快速路（不包含高速路）11.速度优先（快速路优先），12.距离优先（路径最短），0，费用优先（不走收费路段的最快道路
		 *        ），1.基于TMC速度最快（若起、终点不在同一城市内，则按照速度优先计算），2：国道优先，4：省道优先，5：不走高速；6：
		 *        多策略1（同时返回速度优先、费用优先、距离优先的路径各一条），7：多策略2（同时返回基于TMC速度最快、基于TMC耗油最少、
		 *        速度优先、费用优先的路径各一条）；8：多策略3（同时返回速度优先、基于TMC速度最快的路径各一条）；9：
		 *        基于TMC速度最快且不走收费路段（在基于TMC速度最快的策略下避开收费路段，若路径规划失败，则按照基于TMC速度最快计算）;
		 *        默认为:11。
		 */
		public void setTactics(int tactics) {
			this.tactics = tactics;
		}

		/**
		 * 返回导航策略。
		 * 
		 * @return 导航策略。
		 */
		public int getTactics() {
			return tactics;
		}

		/**
		 * 将途径点位置坐标转换为字符串输出。
		 * 
		 * @return 途径点坐标的字符串。
		 */
		public String getPassedPointStr() {
			String str = "";
			if (passedByPoints != null && passedByPoints.size() > 0) {
				for (LatLonPoint point : passedByPoints) {
					str = str + ";" + point.toString();
				}
				return str;
			}
			return str;
		}

		public boolean hasPassPoint() {
			if (passedByPoints != null && passedByPoints.size() > 0) {
				return true;
			}
			return false;
		}

		/**
		 * 判断是否有避让区域。
		 * 
		 * @return 是否有避让区域。
		 */
		public boolean hasAvoidpolygons() {
			if (avoidpolygons != null && avoidpolygons.size() > 0) {
				for (List<LatLonPoint> list : avoidpolygons) {
					if (list.size() > 0)
						return true;
				}
			}
			return false;
		}

		/**
		 * 返回设定查询的途经点。
		 * 
		 * @return 设定查询的途经点。
		 */
		public List<LatLonPoint> getPassedByPoints() {
			return passedByPoints;
		}

		/**
		 * 途径点坐标，最多3个坐标。
		 * 
		 * @param passedByPoints
		 */
		public void setPassedByPoints(List<LatLonPoint> passedByPoints) {
			this.passedByPoints = passedByPoints;
		}

		/**
		 * 返回设定查询的避让区域。
		 * 
		 * @return 设定查询的避让区域。
		 */
		public List<List<LatLonPoint>> getAvoidpolygons() {
			return avoidpolygons;
		}

		/**
		 * 设置避让区域。
		 * 
		 * @param avoidpolygons
		 *            避让点坐标。
		 */
		public void setAvoidpolygons(List<List<LatLonPoint>> avoidpolygons) {
			this.avoidpolygons = avoidpolygons;
		}

		/**
		 * 返回查询路径的起终点。
		 * 
		 * @return 查询路径的起终点。
		 */
		public FromAndTo getFromAndTo() {
			return fromAndTo;
		}
		/**
		 * 设置查询路径的起终点。
		 *
		 */
		public void setFromAndTo(FromAndTo fromAndTo) {
			this.fromAndTo = fromAndTo;
		}

		/**
		 * <p><em>从V3.6.14增加此接口。</em></p>
		 * 返回避让道路名称，支持输入一条道路名称。
		 * @return 避让道路名称，支持输入一条道路名称。
		 * @since 1.10.0
		 */
		public String getAvoidroad() {
			return avoidroad;
		}

		/**
		 * <p><em>从V3.6.14增加此接口。</em></p>
		 * 设置避让道路名称，支持输入一条道路名称。
		 * @param avoidroad 避让道路名称，支持输入一条道路名称。
		 * @since 1.10.0
		 */
		public void setAvoidroad(String avoidroad) {
			this.avoidroad = avoidroad;
		}

		/**
		 * 序列化实现。
		 */
		public DriveRouteQuery(Parcel paramParcel) {
			this.fromAndTo = ((RouteSearch.FromAndTo) paramParcel
					.readParcelable(RouteSearch.FromAndTo.class
							.getClassLoader()));
			this.tactics = paramParcel.readInt();
			this.passedByPoints = paramParcel
					.createTypedArrayList(LatLonPoint.CREATOR);
			int i = paramParcel.readInt();
			if (i == 0)
				this.avoidpolygons = null;
			else {
				this.avoidpolygons = new ArrayList<List<LatLonPoint>>();
			}
			for (int j = 0; j < i; j++) {
				this.avoidpolygons.add(paramParcel
						.createTypedArrayList(LatLonPoint.CREATOR));
			}
			this.coord_Type = paramParcel.readString();
			this.avoidroad = paramParcel.readString();

		}



		public int hashCode() {
			int i = 31;
			int j = 1;
			j = i * j
					+ (this.fromAndTo == null ? 0 : this.fromAndTo.hashCode());
			j = i * j + this.tactics;
			j = i
					* j
					+ (this.coord_Type == null ? 0 : this.coord_Type.hashCode());

			j = i
					* j
					+ (this.passedByPoints == null ? 0 : this.passedByPoints
							.hashCode());
			j = i
					* j
					+ (this.avoidpolygons == null ? 0 : this.avoidpolygons
							.hashCode());
			j = i * j
					+ (this.avoidroad == null ? 0 : this.avoidroad.hashCode());
			return j;

		}

		/**
		 * 比较两个查询条件是否相同。
		 * 
		 * @param obj
		 *            - 查询条件。
		 * @return 查询条件是否相同。
		 */
		public boolean equals(java.lang.Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DriveRouteQuery localDriveRouteQuery = (DriveRouteQuery) obj;
			if (this.fromAndTo == null) {
				if (localDriveRouteQuery.fromAndTo != null)
					return false;
			} else if (!this.fromAndTo.equals(localDriveRouteQuery.fromAndTo))
				return false;
			if (this.coord_Type == null) {
				if (localDriveRouteQuery.coord_Type != null)
					return false;
			} else if (!this.coord_Type.equals(localDriveRouteQuery.coord_Type))
				return false;
			if (this.tactics != localDriveRouteQuery.tactics)
				return false;
			if (this.passedByPoints == null) {
				if (localDriveRouteQuery.passedByPoints != null)
					return false;
			} else if (!this.passedByPoints
					.equals(localDriveRouteQuery.passedByPoints))
				return false;
			if (this.avoidpolygons == null) {
				if (localDriveRouteQuery.avoidpolygons != null)
					return false;
			} else if (!this.avoidpolygons
					.equals(localDriveRouteQuery.avoidpolygons))
				return false;
			if (this.avoidroad == null) {
				if (localDriveRouteQuery.avoidroad != null)
					return false;
			} else if (!this.avoidroad
					.equals(localDriveRouteQuery.avoidroad))
				return false;
			return true;

		}

		public void writeToParcel(android.os.Parcel dest, int flags) {
			dest.writeParcelable(this.fromAndTo, flags);
			dest.writeInt(this.tactics);
			dest.writeTypedList(passedByPoints);
			if (this.avoidpolygons == null) {
				dest.writeInt(0);
			} else {
				dest.writeInt(this.avoidpolygons.size());
				for (List<LatLonPoint> localList : this.avoidpolygons) {
					dest.writeTypedList(localList);
				}
			}
			dest.writeString(this.coord_Type);
			dest.writeString(this.avoidroad);

		}

		public DriveRouteQuery clone() {
			try {
				super.clone();
				DriveRouteQuery query = new DriveRouteQuery(this.fromAndTo.clone());
				if(this.getAvoidpolygons()!=null&&this.getAvoidpolygons().size()>0){
					List<List<LatLonPoint>> avoidList = new ArrayList<List<LatLonPoint>>();
					for(List<LatLonPoint> avoidpolygon: this.getAvoidpolygons()){
						List<LatLonPoint> avoidpolygonList = new ArrayList<LatLonPoint>();
						for(LatLonPoint point:avoidpolygon){
							avoidpolygonList.add(point.copy());
						}
						avoidList.add(avoidpolygonList);
					}
				}
				query.setAvoidpolygons(this.getAvoidpolygons());
				query.setCoordType(this.getCoordType());
				if(this.getPassedByPoints()!=null&&this.getPassedByPoints().size()>0){
					List<LatLonPoint> list = new ArrayList<LatLonPoint>();
					for(LatLonPoint latlon:this.getPassedByPoints()){
						list.add(latlon.copy());
						query.setPassedByPoints(list);
					}
				}
				query.setTactics(this.getTactics());
				query.setAvoidroad(this.getAvoidroad());
				return query;
			} catch (CloneNotSupportedException localCloneNotSupportedException) {
				return null;
			}
		}

		@Override
		public int describeContents() {
			return 0;
		}

	}

	/**
	 * 构造路径规划的起点和终点坐标
	 */
	public static class FromAndTo implements Parcelable, Cloneable {
        private LatLonPoint from;
        private LatLonPoint to;
		private String fromPoiID;
		private String toPoiID;
		public static final Creator<FromAndTo> CREATOR = new FromAndToCreator<FromAndTo>();

		/**
		 * FromAndTo 构造函数
		 */
		public FromAndTo() {
			super();
		}

		/**
		 * FromAndTo的构造函数
		 */
		public FromAndTo(LatLonPoint from, LatLonPoint to) {

			this.from = from;
			this.to = to;
			if (isNull()) {
				try {
					throw new UnistrongException(
							UnistrongException.ERROR_NULL_PARAMETER);
				} catch (UnistrongException e) {
					throw new IllegalArgumentException("Empty Location");
				}
			}
		}

		private boolean isNull() {
			if (from == null || to == null || from.getLatitude() == 0
					|| from.getLongitude() == 0 || to.getLatitude() == 0
					|| to.getLongitude() == 0)
				return true;
			else
				return false;
		}

		/**
		 * 序列化实现。
		 */
		public FromAndTo(Parcel paramParcel) {
			this.from = ((LatLonPoint) paramParcel
					.readParcelable(LatLonPoint.class.getClassLoader()));
			this.to = ((LatLonPoint) paramParcel
					.readParcelable(LatLonPoint.class.getClassLoader()));
			this.fromPoiID = paramParcel.readString();
			this.toPoiID = paramParcel.readString();
		}

		/**
		 * 返回路径规划目的地POI的ID。
		 * 
		 * @return 路径规划的目的地坐标。
		 */
		private String getDestinationPoiID() {
			return toPoiID;
		}

		/**
		 * 返回路径规划起点POI的ID。
		 * 
		 * @return 路径规划的起点坐标。
		 */
        private String getStartPoiID() {
			return fromPoiID;
		}

        /**
         * 设置路径规划的起点坐标。
         * @param from 路径规划的起点坐标。
         */
        public void setFrom(LatLonPoint from) {
            this.from = from;
        }

        /**
         * 设置路径规划的终点坐标。
         * @param to 路径规划的终点坐标。
         */
        public void setTo(LatLonPoint to) {
            this.to = to;
        }

		/**
		 * 返回路径规划的起点坐标。
		 * 
		 * @return 路径规划的起点坐标。
		 */
		public LatLonPoint getFrom() {
			return from;
		}

		/**
		 * 返回路径规划的终点坐标。
		 * 
		 * @return 路径规划的终点坐标。
		 */
		public LatLonPoint getTo() {
			return to;
		}



		public int hashCode() {
			int i = 31;
			int j = 1;
			j = i * j + (this.from == null ? 0 : this.from.hashCode());
			j = i * j + (this.to == null ? 0 : this.to.hashCode());
			j = i * j
					+ (this.fromPoiID == null ? 0 : this.fromPoiID.hashCode());
			j = i * j + (this.toPoiID == null ? 0 : this.toPoiID.hashCode());
			return j;
		}

		/**
		 * 比较路径规划的起终点是否相同。
		 * 
		 * @param paramObject
		 *            - 待比较对象。
		 * @return 路径规划起终点是否相同。
		 */
		public boolean equals(Object paramObject) {
			if (this == paramObject)
				return true;
			if (paramObject == null)
				return false;
			if (getClass() != paramObject.getClass())
				return false;
			FromAndTo localFromAndTo = (FromAndTo) paramObject;
			if (this.toPoiID == null) {
				if (localFromAndTo.toPoiID != null)
					return false;
			} else if (!this.toPoiID.equals(localFromAndTo.toPoiID))
				return false;
			if (this.from == null) {
				if (localFromAndTo.from != null)
					return false;
			} else if (!this.from.equals(localFromAndTo.from))
				return false;
			if (this.fromPoiID == null) {
				if (localFromAndTo.fromPoiID != null)
					return false;
			} else if (!this.fromPoiID.equals(localFromAndTo.fromPoiID))
				return false;
			if (this.to == null) {
				if (localFromAndTo.to != null)
					return false;
			} else if (!this.to.equals(localFromAndTo.to))
				return false;
			return true;
		}

		/**
		 * 设置路径规划目的地POI的ID。
		 * 
		 * @param mDestinationPoiID
		 *            路径规划的目的地坐标。
		 */
		private void setDestinationPoiID(String mDestinationPoiID) {
			this.toPoiID = mDestinationPoiID;
		}

		/**
		 * 设置路径规划起点POI的ID。
		 * 
		 * @param mStartPoiID
		 *            路径规划的起点坐标
		 */
        private void setStartPoiID(String mStartPoiID) {
			this.fromPoiID = mStartPoiID;
		}

		public FromAndTo clone() {
			try {
				super.clone();
			} catch (CloneNotSupportedException localCloneNotSupportedException) {
			}
			FromAndTo localFromAndTo = new FromAndTo(this.from, this.to);
			localFromAndTo.setStartPoiID(this.fromPoiID);
			localFromAndTo.setDestinationPoiID(this.toPoiID);
			return localFromAndTo;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel paramParcel, int paramInt) {
			paramParcel.writeParcelable(this.from, paramInt);
			paramParcel.writeParcelable(this.to, paramInt);
			paramParcel.writeString(this.fromPoiID);
			paramParcel.writeString(this.toPoiID);
		}
	}

	/**
	 * 本类为路径搜索结果的异步处理回调接口。
	 */
	public static abstract interface OnRouteSearchListener {
		/**
		 * 驾车路径规划结果的回调方法。
		 * 
		 * @param driveRouteResult
		 *            驾车路径规划的结果集。
		 * @param errorCode
		 *            返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）。
		 */
		public abstract void onDriveRouteSearched(
				DriveRouteResult driveRouteResult, int errorCode);

		/**
		 * 步行路径规划结果的回调方法。
		 * 
		 * @param walkRouteResult
		 *            步行路径规划的结果集。
		 * @param errorCode
		 *            返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）。
		 */
		public abstract void onWalkRouteSearched(
				WalkRouteResult walkRouteResult, int errorCode);

		/**
		 * 公交换乘路径规划结果的回调方法。
		 * 
		 * @param busRouteResult
		 *            公交路径规划的结果集。
		 * @param errorCode
		 *            返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）。
		 */
		public abstract void onBusRouteSearched(BusRouteResult busRouteResult,
				int errorCode);
	}

}
