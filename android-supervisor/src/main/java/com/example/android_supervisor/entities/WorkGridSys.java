package com.example.android_supervisor.entities;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 区域网格实体类
 * Created by yj on 2019/9/3.
 */
@DatabaseTable(tableName = "t_work_grid")
public class WorkGridSys {

    @DatabaseField
    private String type;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Geometry geometry;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private WorkGridPropertiesBean workGridProperties;

    private String _id;

    private String id;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }


    public WorkGridPropertiesBean getWorkGridProperties() {
        return workGridProperties;
    }

    public void setWorkGridProperties(WorkGridPropertiesBean workGridProperties) {
        this.workGridProperties = workGridProperties;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Geometry implements Serializable {

        private ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> coordinates;
        private String type;

        public ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean contains(double x, double y) {
            if (coordinates == null || coordinates.isEmpty())
                return false;

            for (ArrayList<ArrayList<ArrayList<Double>>> polygon : coordinates) {
                if (polygon.isEmpty()) {
                    continue;
                }

                ArrayList<PointD> points = new ArrayList<>();
                for (ArrayList<ArrayList<Double>> polyline : polygon) {
                    if (polyline.isEmpty()) {
                        continue;
                    }
                    for (ArrayList<Double> point : polyline) {
                        if (point.size() == 2) {
                            points.add(new PointD(point.get(0), point.get(1)));
                        }
                    }
                }

                int length = points.size();
                PointD[] pts = points.toArray(new PointD[length]);
                if (points.size() > 3) {
                    if (contains(pts, new PointD(x, y))) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean contains(PointD[] pts, PointD p) {
            int N = pts.length;
            int intersectCount = 0;
            double precision = 2e-10;
            PointD p1, p2;

            p1 = pts[0];
            for (int i = 1; i <= N; ++i) {
                if (p.equals(p1)) {
                    return true;//p is an vertex
                }

                p2 = pts[i % N];//right vertex
                if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {//ray is outside of our interests
                    p1 = p2;
                    continue;//next ray left point
                }

                if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {//ray is crossing over by the algorithm (common part of)
                    if (p.y <= Math.max(p1.y, p2.y)) {//x is before of ray
                        if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {//overlies on a horizontal ray
                            return true;
                        }

                        if (p1.y == p2.y) {//ray is vertical
                            if (p1.y == p.y) {//overlies on a vertical ray
                                return true;
                            } else {//before ray
                                ++intersectCount;
                            }
                        } else {//cross point on the left side
                            double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y
                            if (Math.abs(p.y - xinters) < precision) {//overlies on a ray
                                return true;
                            }

                            if (p.y < xinters) {//before ray
                                ++intersectCount;
                            }
                        }
                    }
                } else {//special case when ray is crossing through the vertex
                    if (p.x == p2.x && p.y <= p2.y) {//p crossing over p2
                        PointD p3 = pts[(i + 1) % N]; //next vertex
                        if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {//p.x lies between p1.x & p3.x
                            ++intersectCount;
                        } else {
                            intersectCount += 2;
                        }
                    }
                }
                p1 = p2;//next ray left point
            }
            if (intersectCount % 2 == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static class PointD {
        private double x;
        private double y;

        public PointD() {
        }

        public PointD(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

    public static class WorkGridPropertiesBean implements Serializable {

        /**
         * grCode : 411728
         * grName : 遂平县
         * x :
         * y :
         * shapeLeng : 19677.1933504
         * shapeArea : 2.44467300863E7
         * type : T_MAP_DISTRICT_GRID
         * note :
         * grUpCode : 411728
         * grUpName : 遂平县
         */


        private String note;
        private String shapeArea;
        private String shapeLeng;
        private String sqCode;
        private String sqName;
        private String type;
        private String x;
        private String y;
        private String zrCode;
        private String zrName;

        public WorkGridPropertiesBean(String note, String shapeArea, String shapeLeng, String sqCode, String sqName, String type, String x, String y, String zrCode, String zrName) {
            this.note = note;
            this.shapeArea = shapeArea;
            this.shapeLeng = shapeLeng;
            this.sqCode = sqCode;
            this.sqName = sqName;
            this.type = type;
            this.x = x;
            this.y = y;
            this.zrCode = zrCode;
            this.zrName = zrName;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getShapeArea() {
            return shapeArea;
        }

        public void setShapeArea(String shapeArea) {
            this.shapeArea = shapeArea;
        }

        public String getShapeLeng() {
            return shapeLeng;
        }

        public void setShapeLeng(String shapeLeng) {
            this.shapeLeng = shapeLeng;
        }

        public String getSqCode() {
            return sqCode;
        }

        public void setSqCode(String sqCode) {
            this.sqCode = sqCode;
        }

        public String getSqName() {
            return sqName;
        }

        public void setSqName(String sqName) {
            this.sqName = sqName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getZrCode() {
            return zrCode;
        }

        public void setZrCode(String zrCode) {
            this.zrCode = zrCode;
        }

        public String getZrName() {
            return zrName;
        }

        public void setZrName(String zrName) {
            this.zrName = zrName;
        }
    }
}
