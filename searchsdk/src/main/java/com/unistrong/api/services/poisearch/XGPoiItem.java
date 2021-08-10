package com.unistrong.api.services.poisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个POI（Point Of Interest，兴趣点）。一个POI 由如下成员组成: POI 的唯一标识，即id。这个标识在不同的数据版本中延续。
 * POI 的名称。 POI 的经纬度。 POI 的地址。 POI 的类型代码。 POI 的类型描述。 POI 的电话号码。 POI距离周边搜索中心点的距离。
 */
public class XGPoiItem implements Serializable {



    /**
     * type : Feature
     * id : poi.3989
     * geometry : {"type":"Point","coordinates":[113.83117571,34.4837414]}
     * geometry_name : the_geom
     * properties : {"objectid":5943,"kind":"企业","poiname":"河南机场集团有限公司","road":"迎宾大道","address":"迎宾大道河南机场集团有限公司","note_":null,"picture":"HKG_F_0307_0409","x":"484813.556","y":"3822682.841","zplj":"/照片/2020年完成/完成后/1027号图完成版/1027修改照片/HKG__0307_0409.jp","kindno":"270000"}
     */

    private String type;
    private String id;
    private GeometryBean geometry;
    private String geometry_name;
    private PropertiesBean properties;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public String getGeometry_name() {
        return geometry_name;
    }

    public void setGeometry_name(String geometry_name) {
        this.geometry_name = geometry_name;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }


    public static class GeometryBean implements Serializable {
        @Override
        public String toString() {
            return "GeometryBean{" +
                    "type='" + type + '\'' +
                    ", coordinates=" + coordinates +
                    '}';
        }

        /**
         * type : Point
         * coordinates : [113.83117571,34.4837414]
         */


        private String type;
        private List<Double> coordinates;

        public GeometryBean(ArrayList<Double> laction) {
            this.coordinates = laction;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }


        public double getLongitude() {
            return coordinates.get(0);
        }

        public double getLatitude() {
            return coordinates.get(1);

        }
    }

    public static class PropertiesBean implements Serializable {
        /**
         * objectid : 5943
         * kind : 企业
         * poiname : 河南机场集团有限公司
         * road : 迎宾大道
         * address : 迎宾大道河南机场集团有限公司
         * note_ : null
         * picture : HKG_F_0307_0409
         * x : 484813.556
         * y : 3822682.841
         * zplj : /照片/2020年完成/完成后/1027号图完成版/1027修改照片/HKG__0307_0409.jp
         * kindno : 270000
         */

        private int objectid;
        private String kind;
        private String poiname;
        private String road;
        private String address;
        private Object note_;
        private String picture;
        private String x;
        private String y;
        private String zplj;
        private String kindno;

        public int getObjectid() {
            return objectid;
        }

        public void setObjectid(int objectid) {
            this.objectid = objectid;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getPoiname() {
            return poiname;
        }

        public void setPoiname(String poiname) {
            this.poiname = poiname;
        }

        public String getRoad() {
            return road;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getNote_() {
            return note_;
        }

        public void setNote_(Object note_) {
            this.note_ = note_;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
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

        public String getZplj() {
            return zplj;
        }

        public void setZplj(String zplj) {
            this.zplj = zplj;
        }

        public String getKindno() {
            return kindno;
        }

        public void setKindno(String kindno) {
            this.kindno = kindno;
        }
    }

    @Override
    public String toString() {
        return "XGPoiItem{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", geometry=" + geometry +
                ", geometry_name='" + geometry_name + '\'' +
                ", properties=" + properties +
                '}';
    }
}
