package com.example.android_supervisor.entities;

import java.util.List;

public class WorkGridSysBean {


    /**
     * _id : 5d777e07d9e5a445188bd30e
     * geometry : {"coordinates":[[[[113.94631184400009,22.773907316000077],[113.94655789600006,22.771219254000073],[113.94657494900002,22.769967441000063],[113.94660201500004,22.767980008000052],[113.94660228700002,22.767965549000053],[113.94663390300002,22.766262469000026],[113.94662844400011,22.76620067700003],[113.9465332320001,22.76519403900005],[113.9461604820001,22.761251220000077],[113.9459598740001,22.761444625000024],[113.94402970600004,22.763305224000078],[113.94398607900007,22.763234065000063],[113.94373140500011,22.763417596000068],[113.94327924200002,22.76369290300005],[113.94288985100002,22.76387499300006],[113.9423680650001,22.76407381300004],[113.94198861000008,22.76426548200004],[113.94157847500003,22.76446610800008],[113.94111565900005,22.764769493000074],[113.94072271500012,22.765139945000044],[113.94060076900008,22.765332960000023],[113.94077654600005,22.76532769100004],[113.94107809700006,22.765495635000036],[113.94137686000012,22.765815726000028],[113.94166432200005,22.76611390100004],[113.94188416300005,22.76626965500003],[113.94216229200003,22.766437215000053],[113.94259162100003,22.766672455000048],[113.94299744700004,22.766907313000047],[113.94331120300001,22.767053718000057],[113.9434755210001,22.767172197000036],[113.9430819060001,22.767165829000078],[113.94266359800008,22.76726267400005],[113.94241592400009,22.767465897000022],[113.94189864200008,22.76796624900004],[113.94177466500001,22.76807727700003],[113.94147820400008,22.768176091000043],[113.94143528300003,22.768297850000067],[113.941312014,22.768371245000026],[113.94119637900008,22.768576603000042],[113.94088980100001,22.768675254000073],[113.94068770400008,22.76861546400005],[113.9404971150001,22.76848992400005],[113.94044825600008,22.76838551800006],[113.9402741780001,22.768458055000053],[113.93978628200011,22.768670150000048],[113.93982139900004,22.768736586000045],[113.9401390920001,22.769296144000066],[113.94047980300002,22.76987783900006],[113.94067315300003,22.770196224000074],[113.94064371000003,22.770365863000052],[113.94276179900011,22.772136657000033],[113.94539456000007,22.773229635000064],[113.94627612400008,22.773880916000053],[113.94631184400009,22.773907316000077]]]],"type":"MultiPolygon"}
     * id : 440309T_MAP_CUSTOM_WORKGRID.45
     * type : Feature
     * workGridProperties : {"note":"","shapeArea":"660476","shapeLeng":"3906.62335436","sqCode":"440309001007","sqName":"翠湖","type":"T_MAP_CUSTOM_WORKGRID","x":"","y":"","zrCode":"440309001007A0","zrName":"A0"}
     */

    private String _id;
    private GeometryBean geometry;
    private String id;
    private String type;
    private WorkGridPropertiesBean workGridProperties;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WorkGridPropertiesBean getWorkGridProperties() {
        return workGridProperties;
    }

    public void setWorkGridProperties(WorkGridPropertiesBean workGridProperties) {
        this.workGridProperties = workGridProperties;
    }

    public static class GeometryBean {
        /**
         * coordinates : [[[[113.94631184400009,22.773907316000077],[113.94655789600006,22.771219254000073],[113.94657494900002,22.769967441000063],[113.94660201500004,22.767980008000052],[113.94660228700002,22.767965549000053],[113.94663390300002,22.766262469000026],[113.94662844400011,22.76620067700003],[113.9465332320001,22.76519403900005],[113.9461604820001,22.761251220000077],[113.9459598740001,22.761444625000024],[113.94402970600004,22.763305224000078],[113.94398607900007,22.763234065000063],[113.94373140500011,22.763417596000068],[113.94327924200002,22.76369290300005],[113.94288985100002,22.76387499300006],[113.9423680650001,22.76407381300004],[113.94198861000008,22.76426548200004],[113.94157847500003,22.76446610800008],[113.94111565900005,22.764769493000074],[113.94072271500012,22.765139945000044],[113.94060076900008,22.765332960000023],[113.94077654600005,22.76532769100004],[113.94107809700006,22.765495635000036],[113.94137686000012,22.765815726000028],[113.94166432200005,22.76611390100004],[113.94188416300005,22.76626965500003],[113.94216229200003,22.766437215000053],[113.94259162100003,22.766672455000048],[113.94299744700004,22.766907313000047],[113.94331120300001,22.767053718000057],[113.9434755210001,22.767172197000036],[113.9430819060001,22.767165829000078],[113.94266359800008,22.76726267400005],[113.94241592400009,22.767465897000022],[113.94189864200008,22.76796624900004],[113.94177466500001,22.76807727700003],[113.94147820400008,22.768176091000043],[113.94143528300003,22.768297850000067],[113.941312014,22.768371245000026],[113.94119637900008,22.768576603000042],[113.94088980100001,22.768675254000073],[113.94068770400008,22.76861546400005],[113.9404971150001,22.76848992400005],[113.94044825600008,22.76838551800006],[113.9402741780001,22.768458055000053],[113.93978628200011,22.768670150000048],[113.93982139900004,22.768736586000045],[113.9401390920001,22.769296144000066],[113.94047980300002,22.76987783900006],[113.94067315300003,22.770196224000074],[113.94064371000003,22.770365863000052],[113.94276179900011,22.772136657000033],[113.94539456000007,22.773229635000064],[113.94627612400008,22.773880916000053],[113.94631184400009,22.773907316000077]]]]
         * type : MultiPolygon
         */

        private String type;
        private List<List<List<List<Double>>>> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<List<List<List<Double>>>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<List<List<List<Double>>>> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class WorkGridPropertiesBean {
        /**
         * note :
         * shapeArea : 660476
         * shapeLeng : 3906.62335436
         * sqCode : 440309001007
         * sqName : 翠湖
         * type : T_MAP_CUSTOM_WORKGRID
         * x :
         * y :
         * zrCode : 440309001007A0
         * zrName : A0
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
