package com.bluetron.nb.common.util.excel;

import cn.hutool.core.util.ReflectUtil;
import com.bluetron.nb.common.base.constant.ICodeAndNameEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * excel导出
 *
 * @author genx
 * @date 2021/5/8 8:31
 */
public class ExcelExportHelper implements Closeable {


    enum ExcelType {
        xls,
        xlsx
    }

    private final Workbook workbook;

    private int rowIndex;

    private Map<String, CellStyle> cellStyleCache = new HashMap(16);

    private final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public ExcelExportHelper() {
        this(ExcelType.xlsx);
    }

    public ExcelExportHelper(ExcelType excelType) {
        if (excelType != null && excelType == ExcelType.xls) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }

        Font font = workbook.getFontAt((short) 0);
        font.setCharSet(HSSFFont.DEFAULT_CHARSET);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("黑体");
    }

    public ExcelExportHelper createSheet(String sheetName, String[][] headers, String[] columnCodes, List dataList) {
        Sheet sheet = workbook.createSheet(sheetName);
        rowIndex = 0;
        //表头
        writeHeaders(sheet, headers);

        Map<String, Integer> columnMap = buildColumnMap(columnCodes);

        //读数据 写excel
        readAndWrite(dataList, "", sheet, columnMap);

        for (int i = 0; i < columnCodes.length; i++) {
            sheet.autoSizeColumn(i, true);
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i));
        }
        return this;
    }

    public void write(OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
        this.close();
    }

    private void writeHeaders(Sheet sheet, String[][] headers) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        font2.setFontName("黑体");
        font2.setBold(true);
        font2.setFontHeightInPoints((short) 13);
        headerStyle.setFont(font2);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        int max = 0;
        Cell cell;
        for (String[] header : headers) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < header.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerStyle);
            }
            max = Math.max(max, header.length);
        }
        //动态规划 判断哪些可以合并
        Coordinate[][] dp = new Coordinate[headers.length][max];
        for (int i = 0; i < headers.length; i++) {
            for (int j = 0; j < headers[i].length; j++) {
                dp[i][j] = new Coordinate(i, j);
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i > 0 && j > 0 && StringUtils.equals(headers[i][j], headers[i][j - 1]) && StringUtils.equals(headers[i][j], headers[i - 1][j])) {
                    dp[i][j - 1].maximize = false;
                    dp[i - 1][j].maximize = false;
                    dp[i][j].x = dp[i][j - 1].x;
                    dp[i][j].y = dp[i][j - 1].y;
                } else if (j > 0 && StringUtils.equals(headers[i][j], headers[i][j - 1])) {
                    dp[i][j - 1].maximize = false;
                    dp[i][j].x = dp[i][j - 1].x;
                    dp[i][j].y = dp[i][j - 1].y;

                } else if (i > 0 && StringUtils.equals(headers[i][j], headers[i - 1][j])) {
                    dp[i - 1][j].maximize = false;
                    dp[i][j].x = dp[i - 1][j].x;
                    dp[i][j].y = dp[i - 1][j].y;
                }

            }
        }

        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                if (dp[i][j] != null && dp[i][j].maximize) {
                    if (i > dp[i][j].y || j > dp[i][j].x) {
                        sheet.addMergedRegion(new CellRangeAddress(dp[i][j].y, i, dp[i][j].x, j));
                    }
                }
            }
        }
    }

    private Map<String, Integer> buildColumnMap(String[] columnCodes) {
        Map<String, Integer> columnMap = new HashMap(32);
        for (int i = 0; i < columnCodes.length; i++) {
            columnMap.put(columnCodes[i], i);
            if (columnCodes[i].contains(".")) {
                StringBuilder temp = new StringBuilder(16);
                for (char c : columnCodes[i].toCharArray()) {
                    if (c == '.') {
                        columnMap.putIfAbsent(temp.toString(), -1);
                    }
                    temp.append(c);
                }
            }
        }
        return columnMap;
    }

    private void readAndWrite(Collection list, String prefix, Sheet sheet, Map<String, Integer> columnMap) {
        Cell cell;
        for (Object item : list) {
            int start = rowIndex;
            BeanWrapper beanWrapper = new BeanWrapperImpl(item);
            //是否叶子节点
            boolean isLeaf = true;
            //优先遍历列表
            for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
                //这里可以优化一下， 缓存 实体中 返回list的方法
                if (Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && columnMap.containsKey(prefix + propertyDescriptor.getName())) {
                    Collection collection = (Collection) beanWrapper.getPropertyValue(propertyDescriptor.getName());
                    if (collection != null && collection.size() > 0) {
                        readAndWrite(collection, prefix + propertyDescriptor.getName() + ".", sheet, columnMap);
                        isLeaf = false;
                    }
                    //如果对象中包含返回list的方法， 只取第一个
                    break;
                }
            }
            if (isLeaf) {
                sheet.createRow(rowIndex++);
            }

            Integer column;
            for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
                column = columnMap.get(prefix + propertyDescriptor.getName());
                if (column != null && column >= 0) {
                    cell = sheet.getRow(start).createCell(column);
                    setCellValue(cell, beanWrapper, propertyDescriptor);

                    if (rowIndex - 1 > start) {
                        //合并单元格
                        sheet.addMergedRegion(new CellRangeAddress(start, rowIndex - 1, column, column));
                        //重新设置样式 会导致时间样式失效
                        //cell.setCellStyle(getCenterStyle());
                    }
                }
            }
        }
    }

    private void setCellValue(Cell cell, BeanWrapper beanWrapper, PropertyDescriptor propertyDescriptor) {
        Object value = beanWrapper.getPropertyValue(propertyDescriptor.getName());
        if (value == null) {
            return;
        }
        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            //时间格式
            setDateFormatStyle(cell, beanWrapper.getWrappedClass(), propertyDescriptor.getName());
        } else if (value instanceof Number) {
            cell.setCellValue(new BigDecimal(String.valueOf(value)).doubleValue());
        } else if (value instanceof Boolean) {
            try {
                Field field = ReflectUtil.getField(beanWrapper.getWrappedClass(), propertyDescriptor.getName());
                if (field != null) {
                    ExcelBooleanFormat booleanFormat = field.getAnnotation(ExcelBooleanFormat.class);
                    if (booleanFormat != null) {
                        cell.setCellValue((Boolean) value ? booleanFormat.trueLabel() : booleanFormat.falseLabel());
                        return;
                    }
                }
            } catch (Exception e) {

            }
            cell.setCellValue((Boolean) value ? "是" : "否");

        } else if (value instanceof ICodeAndNameEnum) {
            cell.setCellValue(((ICodeAndNameEnum) value).getChineseName());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    private void setDateFormatStyle(Cell cell, Class beanClass, String fieldName) {
        String key = "dateStyle:" + beanClass.getName() + "#" + fieldName;
        CellStyle cellStyle = cellStyleCache.get(key);
        if (cellStyle == null) {
            String pattern = DEFAULT_PATTERN;
            Field field = ReflectUtil.getField(beanClass, fieldName);
            if (field != null) {
                ExcelDateFormat excelDateFormat = field.getAnnotation(ExcelDateFormat.class);
                if (excelDateFormat != null && StringUtils.isNotBlank(excelDateFormat.value())) {
                    pattern = excelDateFormat.value();
                }
            }
            cellStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            cellStyle.setDataFormat(format.getFormat(pattern));
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyleCache.put(key, cellStyle);
        }
        cell.setCellStyle(cellStyle);
    }

    private CellStyle getCenterStyle() {
        CellStyle centerStyle = cellStyleCache.get("centerStyle");
        if (centerStyle == null) {
            centerStyle = workbook.createCellStyle();
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyleCache.put("centerStyle", centerStyle);
        }
        return centerStyle;
    }

    @Override
    public void close() throws IOException {
        this.workbook.close();
    }

    private class Coordinate {
        private int x;
        private int y;
        private boolean maximize = true;

        private Coordinate(int y, int x) {
            this.y = y;
            this.x = x;
        }

    }

}
