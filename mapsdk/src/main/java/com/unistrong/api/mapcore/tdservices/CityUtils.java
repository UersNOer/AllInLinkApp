package com.unistrong.api.mapcore.tdservices;

public class CityUtils {

    public static int getCityCode(String cityName) {
        int cityCode = 0;
        switch (cityName) {

            case "beijing":
                cityCode = 1;
                break;
            case "tianjin":
                cityCode = 2;

                break;
            case "hebei_1":
                cityCode = 3;

                break;
            case "shanxi":
                cityCode = 4;

                break;
            case "neimenggu":
                cityCode = 5;

                break;
            case "liaoning":
                cityCode = 6;

                break;
            case "jilin":
                cityCode = 7;

                break;
            case "heilongjiang":
                cityCode = 8;

                break;
            case "shanghai":
                cityCode = 9;

                break;
            case "jiangsu":
                cityCode = 10;

                break;
            case "zhejiang":
                cityCode = 11;

                break;
            case "anhui":
                cityCode = 12;
                break;
            case "fujian":
                cityCode = 13;
                break;
            case "jiangxi":
                cityCode = 14;

                break;
            case "shandong":
                cityCode = 15;

                break;
            case "henan":
                cityCode = 16;
                break;
            case "hubei":
                cityCode = 17;

                break;
            case "guangdong":
                cityCode = 19;
                break;
            case "hunan":
                cityCode = 18;

                break;
            case "guangxi":
                cityCode = 20;
                break;

            case "hainan":
                cityCode = 21;

                break;
            case "chongqing":
                cityCode = 22;
                break;
            case "sichuan":
                cityCode = 23;

                break;
            case "guizhou":
                cityCode = 24;
                break;
            case "yunan":
                cityCode = 25;

                break;
            case "xizang":
                cityCode = 26;

                break;
            case "shanxi_1":
                cityCode = 27;

                break;
            case "gansu":
                cityCode = 28;
                break;
            case "qinghai":
                cityCode = 29;

                break;

            case "ningxia":
                cityCode = 30;

                break;
            case "xinjiang":
                cityCode = 31;

                break;





            case "taiwan":
                cityCode = 32;
                break;






        }


        return cityCode;
    }
}
