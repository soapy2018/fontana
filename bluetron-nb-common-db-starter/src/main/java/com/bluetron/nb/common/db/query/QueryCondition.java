package com.bluetron.nb.common.db.query;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/8/16 19:42
 */
public class QueryCondition<T> implements Map<String, Object>, IQueryCondition<T> {

    private Map<String, Object> condition = new HashMap(32);

    private String orderBy;

    private boolean isAsc;

    private int page = 1;
    private int pageSize = 10;

    @Override
    public int size() {
        return this.condition.size();
    }

    @Override
    public boolean isEmpty() {
        return this.condition.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.condition.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.condition.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return this.condition.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        if (key == null) {
            return null;
        }
        switch (key) {
            case "orderBy":
            case "orderCode":
                this.orderBy = String.valueOf(value);
                break;
            case "isAsc":
                String val = String.valueOf(value);
                this.isAsc = "1".equals(val) || BooleanUtils.toBoolean(val);
                break;
            case "page":
                this.page = NumberUtils.toInt(String.valueOf(value), 1);
                break;
            case "pageSize":
                this.pageSize = NumberUtils.toInt(String.valueOf(value), 10);
                break;
            default:
                return this.condition.put(key, value);
        }
        return null;
    }

    @Override
    public Object remove(Object key) {
        return this.condition.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        this.condition.putAll(m);
    }

    @Override
    public void clear() {
        this.condition.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.condition.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.condition.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.condition.entrySet();
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean asc) {
        isAsc = asc;
    }

    @Override
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


}
