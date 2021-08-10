package com.unistrong.api.mapcore.tdservices;

import java.util.List;

public class School {


    @Override
    public String toString() {
        return "School{" +
                "count='" + count + '\'' +
                ", resultType=" + resultType +
                ", statistics=" + statistics +
                ", keyWord='" + keyWord + '\'' +
                ", prompt=" + prompt +
                '}';
    }


    private String count;
    private int resultType;
    public StatisticsBean statistics;
    private String keyWord;
    private List<PromptBean> prompt;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public StatisticsBean getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsBean statistics) {
        this.statistics = statistics;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<PromptBean> getPrompt() {
        return prompt;
    }

    public void setPrompt(List<PromptBean> prompt) {
        this.prompt = prompt;
    }

    public static class StatisticsBean {
        @Override
        public String toString() {
            return "StatisticsBean{" +
                    "citysCount=" + citysCount +
                    ", provinceCount=" + provinceCount +
                    ", countryCount=" + countryCount +
                    ", keyword='" + keyword + '\'' +
                    ", priorityCitys=" + priorityCitys +
                    ", allAdmins=" + allAdmins +
                    '}';
        }



        private int citysCount;
        private int provinceCount;
        private int countryCount;
        private String keyword;
        private List<PriorityCitysBean> priorityCitys;
        private List<AllAdminsBean> allAdmins;

        public int getCitysCount() {
            return citysCount;
        }

        public void setCitysCount(int citysCount) {
            this.citysCount = citysCount;
        }

        public int getProvinceCount() {
            return provinceCount;
        }

        public void setProvinceCount(int provinceCount) {
            this.provinceCount = provinceCount;
        }

        public int getCountryCount() {
            return countryCount;
        }

        public void setCountryCount(int countryCount) {
            this.countryCount = countryCount;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public List<PriorityCitysBean> getPriorityCitys() {
            return priorityCitys;
        }

        public void setPriorityCitys(List<PriorityCitysBean> priorityCitys) {
            this.priorityCitys = priorityCitys;
        }

        public List<AllAdminsBean> getAllAdmins() {
            return allAdmins;
        }

        public void setAllAdmins(List<AllAdminsBean> allAdmins) {
            this.allAdmins = allAdmins;
        }

        public static class PriorityCitysBean {



            private String name;
            private int count;
            private int adminCode;
            private double lon;
            private double lat;
            private List<AreaStatisticsBean> areaStatistics;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getAdminCode() {
                return adminCode;
            }

            public void setAdminCode(int adminCode) {
                this.adminCode = adminCode;
            }

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public List<AreaStatisticsBean> getAreaStatistics() {
                return areaStatistics;
            }

            public void setAreaStatistics(List<AreaStatisticsBean> areaStatistics) {
                this.areaStatistics = areaStatistics;
            }

            public static class AreaStatisticsBean {
                /**
                 * name : 渝北区
                 * count : 67
                 * adminCode : 156500112
                 * lon : 29.721052
                 * lat : 106.627315
                 */

                private String name;
                private int count;
                private int adminCode;
                private double lon;
                private double lat;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getAdminCode() {
                    return adminCode;
                }

                public void setAdminCode(int adminCode) {
                    this.adminCode = adminCode;
                }

                public double getLon() {
                    return lon;
                }

                public void setLon(double lon) {
                    this.lon = lon;
                }

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }
            }
        }

        public static class AllAdminsBean {
            @Override
            public String toString() {
                return "AllAdminsBean{" +
                        "name='" + name + '\'' +
                        ", count=" + count +
                        ", adminCode=" + adminCode +
                        ", lon=" + lon +
                        ", lat=" + lat +
                        ", childAdmins=" + childAdmins +
                        '}';
            }



            private String name;
            private int count;
            private int adminCode;
            private double lon;
            private double lat;
            private List<ChildAdminsBean> childAdmins;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getAdminCode() {
                return adminCode;
            }

            public void setAdminCode(int adminCode) {
                this.adminCode = adminCode;
            }

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public List<ChildAdminsBean> getChildAdmins() {
                return childAdmins;
            }

            public void setChildAdmins(List<ChildAdminsBean> childAdmins) {
                this.childAdmins = childAdmins;
            }

            public static class ChildAdminsBean {
                @Override
                public String toString() {
                    return "ChildAdminsBean{" +
                            "name='" + name + '\'' +
                            ", count=" + count +
                            ", adminCode=" + adminCode +
                            ", lon=" + lon +
                            ", lat=" + lat +
                            ", areaStatistics=" + areaStatistics +
                            '}';
                }


                private String name;
                private int count;
                private int adminCode;
                private double lon;
                private double lat;
                private List<AreaStatisticsBeanX> areaStatistics;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getAdminCode() {
                    return adminCode;
                }

                public void setAdminCode(int adminCode) {
                    this.adminCode = adminCode;
                }

                public double getLon() {
                    return lon;
                }

                public void setLon(double lon) {
                    this.lon = lon;
                }

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public List<AreaStatisticsBeanX> getAreaStatistics() {
                    return areaStatistics;
                }

                public void setAreaStatistics(List<AreaStatisticsBeanX> areaStatistics) {
                    this.areaStatistics = areaStatistics;
                }

                public static class AreaStatisticsBeanX {
                    @Override
                    public String toString() {
                        return "AreaStatisticsBeanX{" +
                                "name='" + name + '\'' +
                                ", count=" + count +
                                ", adminCode=" + adminCode +
                                ", lon=" + lon +
                                ", lat=" + lat +
                                '}';
                    }

                    /**
                     * name : 瑞金市
                     * count : 81
                     * adminCode : 156360781
                     * lon : 25.895957873
                     * lat : 116.011787174
                     */


                    private String name;
                    private int count;
                    private int adminCode;
                    private double lon;
                    private double lat;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public int getAdminCode() {
                        return adminCode;
                    }

                    public void setAdminCode(int adminCode) {
                        this.adminCode = adminCode;
                    }

                    public double getLon() {
                        return lon;
                    }

                    public void setLon(double lon) {
                        this.lon = lon;
                    }

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }
                }
            }
        }
    }

    public static class PromptBean {
        /**
         * type : 4
         * admins : [{"name":"中国","adminCode":156000000}]
         */

        private int type;
        private List<AdminsBean> admins;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<AdminsBean> getAdmins() {
            return admins;
        }

        public void setAdmins(List<AdminsBean> admins) {
            this.admins = admins;
        }

        public static class AdminsBean {
            /**
             * name : 中国
             * adminCode : 156000000
             */

            private String name;
            private int adminCode;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAdminCode() {
                return adminCode;
            }

            public void setAdminCode(int adminCode) {
                this.adminCode = adminCode;
            }
        }
    }
}
