package com.unistrong.api.mapcore.tdservices;

import android.os.AsyncTask;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PeopleUtils {
    public static final int PEOPLE_SIZE = 3001;

    /**
     * 学校医院分布Task
     */
    public static class Population extends AsyncTask {
        String keyWord;
        android.os.Handler handler;
        private int type;
        private String population;

        public Population(int type, android.os.Handler c) {
            this.handler = c;
            this.type = type;
            switch (type) {
                case 1:
                    population = "population";
                    keyWord = "population";
                    break;
                case 2:
                    population = "population_density";
                    keyWord = "density";
                    break;
                case 3:
                    population = "shengnei_shixiaqu";
                    keyWord = "oueside";
                    break;
                case 4:
                    population = "waisheng";
                    keyWord = "immigration";
                    break;
                case 5:
                    population = "house_average";
                    keyWord = "family_scale";
                    break;
                case 6:
                    population = "live_area";
                    keyWord = "living_space";
                    break;
                case 7:
                    population = "illiteracy";
                    keyWord = "illiteracy";
                    break;
                case 8:
                    population = "live_children";
                    keyWord = "children";
                    break;
                case 9:
                    population = "off_work";
                    keyWord = "unemployment";
                    break;
                case 10:
                    population = "old_people";
                    keyWord = "old_people";
                    break;
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            InputStream is = null;
            try {
                ArrayList<CityPeople> list = new ArrayList<>();

                is = CoverageUtils.class.getResourceAsStream("/assets/json/people/" + keyWord);
                int byteLength;
                byte[] buffer = new byte[1024];
                StringBuilder builder = new StringBuilder();
                while ((byteLength = is.read(buffer)) != -1)
                    builder.append(new String(buffer, 0, byteLength, "UTF-8"));
                is.close();
                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray datas = jsonObject1.getJSONArray("datas");
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject cityObject = datas.getJSONObject(i);
                    CityPeople cityPeople = new CityPeople();
                    cityPeople.setCityname(cityObject.getString("cityname"));
                    String forder = cityObject.getString("forder");
                    if (forder.length() > 2) {
                        forder = forder.substring(0, forder.length() - 2);
                    }
                    cityPeople.setForder(forder);
                    cityPeople.setGbcode(cityObject.getString("gbcode"));
                    cityPeople.setPopulation(cityObject.getString(population));
                    list.add(cityPeople);
                }
                Message message = Message.obtain();
                message.obj = list;
                message.arg1 = type;
                message.what = PEOPLE_SIZE;
                handler.sendMessage(message);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class CityPeople {
        private String forder;
        private String gbcode;
        private String cityname;
        private String population;

        public String getPopulation() {
            return population;
        }

        public void setPopulation(String population) {
            this.population = population;
        }

        public String getForder() {
            return forder;
        }

        public void setForder(String forder) {
            this.forder = forder;
        }

        public String getGbcode() {
            return gbcode;
        }

        public void setGbcode(String gbcode) {
            this.gbcode = gbcode;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        @Override
        public String toString() {
            return "CityPeople{" +
                    "forder='" + forder + '\'' +
                    ", gbcode='" + gbcode + '\'' +
                    ", cityname='" + cityname + '\'' +
                    '}';
        }
    }
}
