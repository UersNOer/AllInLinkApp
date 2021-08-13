package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_map_grid")
public class MapGrid {
    @DatabaseField
    private String type;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Geometry geometry;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    @SerializedName("workGridProperties")
    private HashMap<String, String> properties;

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

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
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
}
