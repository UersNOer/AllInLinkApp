package com.example.android_supervisor.http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujie
 */
public final class QueryBody extends JsonBody {

    private QueryBody(Object object) {
        super(object);
    }

    public static class Builder {
        int pageIndex;
        int pageSize;
        boolean orCond;
        String taskStatus;
        List<Map<String, Object>> groupParams;

        String supervisionStatus;

        String position;
        String questionDesc;
        String caseTitle;

        String startReportTime,endReportTime;

        long receiverId;

        String type;
        List<Double> coordinates;

        String  actInstIdFrom,actInstIdTo,procDefKey,userId;

        final List<String> ascs = new ArrayList<>();
        final List<String> descs = new ArrayList<>();
        final List<Map<String, Object>> params = new ArrayList<>();
        final Map<String, Object> extras = new HashMap<>();



        public Builder actInstIdFrom(String actInstIdFrom) {
            this.actInstIdFrom = actInstIdFrom;
            return this;
        }

        public Builder actInstIdTo(String actInstIdTo) {
            this.actInstIdTo = actInstIdTo;
            return this;
        }


        public Builder procDefKey(String procDefKey) {
            this.procDefKey = procDefKey;
            return this;
        }


        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }




        public Builder pageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
            return this;
        }

        public Builder receiverId(Long receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public Builder taskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder startReportTime(String startReportTime ) {
            this.startReportTime = startReportTime;
            return this;
        }

        public Builder endReportTime(String endReportTime ) {
            this.endReportTime = endReportTime;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }
        public Builder coordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
            return this;
        }


        public Builder supervisionStatus(String supervisionStatus) {
            this.supervisionStatus = supervisionStatus;
            return this;
        }

        public Builder position(String position) {
            this.position = position;
            return this;
        }


        public Builder questionDesc(String questionDesc) {
            this.questionDesc = questionDesc;
            return this;
        }


        public Builder caseTitle(String caseTitle) {
            this.caseTitle = caseTitle;
            return this;
        }

        public Builder eq(String key, String value) {
            return addParam(key, value, "eq");
        }

        public Builder ne(String key, String value) {
            return addParam(key, value, "ne");
        }

        public Builder ge(String key, String value) {
            return addParam(key, value, "ge");
        }

        public Builder le(String key, String value) {
            return addParam(key, value, "le");
        }

        public Builder in(String key, String value) {
            return addParam(key, value, "in");
        }

        public Builder notIn(String key, String value) {
            return addParam(key, value, "not_in");
        }

        public Builder like(String key, String value) {
            return addParam(key, value, "like");
        }

        public Builder and() {
            orCond = false;
            return this;
        }

        public Builder or() {
            orCond = true;
            return this;
        }

        public Builder put(String key, Object value) {
            extras.put(key, value);
            return this;
        }

        public Builder beginGroup() {
            groupParams = new ArrayList<>();
            return this;
        }

        public Builder endGroup() {
            if (groupParams != null) {
                Map<String, Object> param = new HashMap<>();
                param.put("group", groupParams);
                param.put("cond", "and");
                params.add(param);
                groupParams = null;
            }
            return this;
        }

        Builder addParam(String key, String value, String cond) {
            Map<String, Object> param = new HashMap<>();
            param.put("key", key);
            param.put("value", value);
            param.put("cond", orCond ? "or_" + cond : cond);
            if (groupParams != null) {
                groupParams.add(param);
            } else {
                params.add(param);
            }
            return this;
        }

        public Builder asc(String value) {
            ascs.add(value);
            return this;
        }

        public Builder desc(String value) {
            descs.add(value);
            return this;
        }

        public QueryBody create() {
            boolean isPage = pageSize > 0;
            Map<String, Object> map = new HashMap<>();
            map.put("page", isPage);
            map.put("current", pageIndex);
            map.put("limit", pageSize);
            map.put("ascs", ascs);
            map.put("descs", descs);
            map.put("params", params);
            map.put("taskStatus", taskStatus);
            map.put("receiverId", receiverId);
            map.put("supervisionStatus", supervisionStatus);

            map.put("caseTitle", caseTitle);
            map.put("questionDesc", questionDesc);
            map.put("position", position);

            map.put("type", type);
            map.put("coordinates", coordinates);

            map.put("actInstIdFrom", actInstIdFrom);
            map.put("actInstIdTo", actInstIdTo);


            map.put("startReportTime", startReportTime);
            map.put("endReportTime", endReportTime);


            map.putAll(extras);
            return new QueryBody(map);
        }
    }
}
