package com.bluetron.nb.common.util.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * excel数据读取访问者
 * @author genx
 * @date 2021/4/16 13:31
 */
public class ExcelDataVisitor<T> {
    private List<String> headerList = new ArrayList(32);
    private List<T> dataList = new ArrayList(1024);

    public List<String> getHeaderList() {
        return headerList;
    }

    public void addHeader(String headerName) {
        this.headerList.add(headerName);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void addData(T data) {
        this.dataList.add(data);
    }
}