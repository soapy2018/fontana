package com.fontana.db.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * json方法基础类
 *
 * @author cqf
 * @since 2022/4/10
 */
public abstract class BaseJsonModel implements Serializable {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public JSONObject toJson() {
        return (JSONObject) JSONObject.toJSON(this);
    }
}
