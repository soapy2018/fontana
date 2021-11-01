package com.bluetron.nb.common.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.bluetron.nb.common.util.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 *
 */
public class ExcelUtil {

    private ExcelUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static List<List<Object>> readExcel(InputStream bookStream){
        return readExcel(bookStream, 0);
    }

    /**
     * description 读取指定excel文件流的sheet页到集合；对于合并单元格，都会读到List中
     * @param bookStream
     * @param sheetName
     * @return
     */
    public static List<List<Object>> readExcel(InputStream bookStream, String sheetName){
        return cn.hutool.poi.excel.ExcelUtil.getReader(bookStream, sheetName).read();
    }

    public static List<List<Object>> readExcel(InputStream bookStream, int sheetIndex){
        return cn.hutool.poi.excel.ExcelUtil.getReader(bookStream, sheetIndex).read();
    }

    public static List<List<Object>> readExcel(File bookFile){
        return cn.hutool.poi.excel.ExcelUtil.getReader(bookFile).read();
    }


    /**
     * description 将List<List<String>>写入到excel，同时可以设置表头，sheet名
     * @param dataList
     * @param bookFile
     * @param sheetName
     * @param title
     */
    public static void writeListToExcel(List<List<String>> dataList, File bookFile, String sheetName, String title){

        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(bookFile, sheetName);
        //ExcelWriter writer = new ExcelWriter(bookFile, sheetName);

        //跳过当前行，既第一行，非必须，在此演示用
        //writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        if(!StringUtil.isEmpty(title)) {
            writer.merge(dataList.get(0).size() - 1, title);
        }
        //一次性写出内容，强制输出标题
        writer.write(dataList, true);
        //关闭writer，释放内存
        writer.close();
    }

    public static void writeListToExcel(List<List<String>> dataList, File bookFile){
        writeListToExcel(dataList, bookFile, null, null);
    }

    public static void writeListToExcel(List<List<String>> dataList, File bookFile, String title){
        writeListToExcel(dataList, bookFile, null, title);
    }

    /**
     * description 将List<Map<String, Object>写到excel，注意这里不同行对应map的key必须相同，也就是列标题要相同
     * @param dataList
     * @param bookFile
     * @param sheetName
     * @param title
     */
    public static void writeMapToExcel(List<Map<String, Object>> dataList, File bookFile, String sheetName, String title){

        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(bookFile, sheetName);
        //ExcelWriter writer = new ExcelWriter(bookFile, sheetName);

        //跳过当前行，既第一行，非必须，在此演示用
        //writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        if(!StringUtil.isEmpty(title)) {
            writer.merge(dataList.get(0).size() - 1, title);
        }
        //一次性写出内容，强制输出标题
        writer.write(dataList, true);
        //关闭writer，释放内存
        writer.close();
    }

    public static void writeMapToExcel(List<Map<String, Object>> dataList, File bookFile){
        writeMapToExcel(dataList, bookFile, null, null);
    }

    public static void writeMapToExcel(List<Map<String, Object>> dataList, File bookFile, String title){
        writeMapToExcel(dataList, bookFile, null, title);
    }

    /**
     * 导出
     *
     * @param list           数据列表，bean数据
     * @param title          标题
     * @param sheetName      sheet名称
     * @param pojoClass      元素类型
     * @param fileName       文件名
     * @param isCreateHeader 是否创建列头
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName
            , boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * 导出
     *
     * @param list      数据列表，bean数据
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass 元素类型
     * @param fileName  文件名
     * @param response
     * @throws IOException
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName
            , HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }


    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName
            , HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (StringUtils.isBlank(filePath)) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception {
        if (file == null) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
    }


    /**
     * @param dataList  map数据
     * @param fileName  文件名
     * @param response
     * @param sheetName
     * @param title
     * @throws IOException
     */
    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response, String sheetName, String title) throws IOException {

        //是否为xlsx
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(true);

        if(!StringUtil.isBlank(sheetName)) {
            writer.setSheet(sheetName);
        }

        //合并单元格后的标题行，使用默认标题样式
        if(!StringUtil.isBlank(title)) {
            writer.merge(dataList.get(0).size() - 1, title);
        }

        writer.write(dataList, true);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        writer.flush(response.getOutputStream(), true);
        writer.close();
        IoUtil.close(response.getOutputStream());
    }

    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response) throws IOException {
        exportExcel(dataList, fileName, response, null, null);
    }

    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response, String title) throws IOException {
        exportExcel(dataList, fileName, response, null, title);
    }

}
