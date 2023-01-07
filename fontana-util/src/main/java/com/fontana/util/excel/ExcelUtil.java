package com.fontana.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.fontana.util.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
 */
public class ExcelUtil {

    private ExcelUtil() {
        throw new IllegalStateException("Utility class");
    }

    /******************************利用hutool工具封装的excel操作，偏向于代码方式***********************************************************/

    /**
     * 读取输入流的第一个表格
     * @param bookStream 输入流
     * @return 对象list
     */
    public static List<List<Object>> readExcel(InputStream bookStream) {
        return readExcel(bookStream, 0);
    }

    /**
     * 读取指定excel文件流的sheet页到集合；对于合并单元格，都会读到List中
     * @param bookStream 输入流
     * @param sheetName 表格名
     * @return
     */
    public static List<List<Object>> readExcel(InputStream bookStream, String sheetName) {
        return cn.hutool.poi.excel.ExcelUtil.getReader(bookStream, sheetName).read();
    }

    /**
     *
     * @param bookStream 输入流
     * @param sheetIndex 表格序号
     * @return
     */

    public static List<List<Object>> readExcel(InputStream bookStream, int sheetIndex) {
        return cn.hutool.poi.excel.ExcelUtil.getReader(bookStream, sheetIndex).read();
    }

    /**
     * 读取文件到对象list
     * @param bookFile 文件
     * @return 对象list
     */
    public static List<List<Object>> readExcel(File bookFile) {
        return cn.hutool.poi.excel.ExcelUtil.getReader(bookFile).read();
    }


    /**
     * 将List<List<String>>写入到excel，同时可以设置表头，sheet名
     *
     * @param dataList 数据对象list
     * @param bookFile 输出文件
     * @param sheetName 表格名
     * @param title 大标题
     */
    public static void writeListToExcel(List<List<Object>> dataList, File bookFile, String sheetName, String title) {

        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(bookFile, sheetName);
        //ExcelWriter writer = new ExcelWriter(bookFile, sheetName);

        //跳过当前行，既第一行，非必须，在此演示用
        //writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        if (!StringUtil.isEmpty(title)) {
            writer.merge(dataList.get(0).size() - 1, title);
        }
        //一次性写出内容，强制输出标题
        writer.write(dataList, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 将List<List<String>>写入到excel，同时可以设置表头，sheet名
     *
     * @param dataList 数据对象list
     * @param bookFile 输出文件
     */
    public static void writeListToExcel(List<List<Object>> dataList, File bookFile) {
        writeListToExcel(dataList, bookFile, null, null);
    }

    /**
     * 将List<List<String>>写入到excel，同时可以设置表头，sheet名
     *
     * @param dataList 数据对象list
     * @param bookFile 输出文件
     * @param title 大标题
     */
    public static void writeListToExcel(List<List<Object>> dataList, File bookFile, String title) {
        writeListToExcel(dataList, bookFile, null, title);
    }

    /**
     * 将List<Map<Object, Object>写到excel，注意这里不同行对应map的key必须相同，也就是列标题要相同
     *
     * @param dataList 数据对象list
     * @param bookFile 输出文件
     * @param sheetName 表格名
     * @param title 大标题
     */
    public static void writeMapToExcel(List<Map<Object, Object>> dataList, File bookFile, String sheetName, String title) {

        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(bookFile, sheetName);
        //ExcelWriter writer = new ExcelWriter(bookFile, sheetName);

        //跳过当前行，既第一行，非必须，在此演示用
        //writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        if (!StringUtil.isEmpty(title)) {
            writer.merge(dataList.get(0).size() - 1, title);
        }
        //一次性写出内容，强制输出标题
        writer.write(dataList, true);
        //关闭writer，释放内存
        writer.close();
    }

    /**
     * 将List<Map<Object, Object>写到excel，注意这里不同行对应map的key必须相同，也就是列标题要相同
     * @param dataList 数据对象list
     * @param bookFile 输出文件
     */
    public static void writeMapToExcel(List<Map<Object, Object>> dataList, File bookFile) {
        writeMapToExcel(dataList, bookFile, null, null);
    }

    /**
     * 将List<Map<Object, Object>写到excel，注意这里不同行对应map的key必须相同，也就是列标题要相同
     * @param dataList 数据对象list
     * @param bookFile 输出文件
     * @param title 大标题
     */
    public static void writeMapToExcel(List<Map<Object, Object>> dataList, File bookFile, String title) {
        writeMapToExcel(dataList, bookFile, null, title);
    }

    /**
     * @param dataList  map数据
     * @param fileName  文件名
     * @param response 响应
     * @param sheetName 表格名
     * @param title 大标题
     * @throws IOException
     */
    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response, String sheetName, String title) throws IOException {

        //是否为xlsx
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(true);

        if (!StringUtil.isBlank(sheetName)) {
            writer.setSheet(sheetName);
        }

        //合并单元格后的标题行，使用默认标题样式
        if (!StringUtil.isBlank(title)) {
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

    /******************************利用easyPoi封装的excel操作，偏向于注解方式***********************************************************/

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
            , boolean isCreateHeader, HttpServletResponse response, IExcelDictHandler excelDictHandler) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        // 自定义字典查询规则
        exportParams.setDictHandler(excelDictHandler);
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

    /**
     *
     * @param filePath 导入文件
     * @param titleRows 标题行数，默认为0
     * @param headerRows 表头行数，默认为1
     * @param pojoClass 数据对象class
     * @return 数据对象list
     */

    public static <T> List<T> importExcel(String filePath, Class<T> pojoClass, Integer titleRows, Integer headerRows) {
        if (StringUtils.isBlank(filePath)) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
    }

    /**
     *
     * @param filePath 导入文件
     * @param pojoClass 数据对象class
     * @return 数据对象list
     */

    public static <T> List<T> importExcel(String filePath, Class<T> pojoClass) {
        if (StringUtils.isBlank(filePath)) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
    }

    /**
     *
     * @param file MultipartFile对象，spring框架下的上传文件对象
     * @param titleRows 标题行数，默认为0
     * @param headerRows 表头行数，默认为1
     * @param pojoClass 数据对象class
     * @return 数据对象list
     */

    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass, Integer titleRows, Integer headerRows) throws Exception {
        if (file == null) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
    }

    /**
     *
     * @param file MultipartFile对象，spring框架下的上传文件对象
     * @param pojoClass 数据对象class
     * @return 数据对象list
     */

    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws Exception {
        if (file == null) {
            return Collections.emptyList();
        }
        ImportParams params = new ImportParams();
        return ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
    }


    /******************************easyexcel***********************************************************/





    /**
     *@Param: data 需要导出的数据 ,fileName 文件名称 ,response
     *@return:
     *@Author: xxxx
     *@date: 2023/1/7
     */
    public static void ExportDataToExcel (List<?> data, String fileName, HttpServletResponse response) {
        //设置标题样式
        WriteCellStyle headStyle = new WriteCellStyle();
        //设置字体居中显示
        headStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //构造字体对象
        WriteFont writeFont = new WriteFont();
        writeFont.setFontHeightInPoints((short)15);
        // headStyle.setWriteFont(writeFont);
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex());
        //设置content样式
        WriteCellStyle contentStyle = new WriteCellStyle();
        //设置字体垂直居中和水平居中
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        writeFont.setFontHeightInPoints((short)12);  //设置字体大小
        contentStyle.setWriteFont(writeFont);
        contentStyle.setWrapped(true);  //自动换行

        //设置边框样式
        contentStyle.setBorderLeft(BorderStyle.DOTTED);
        contentStyle.setBorderTop(BorderStyle.DOTTED);
        contentStyle.setBorderRight(BorderStyle.DOTTED);
        contentStyle.setBorderBottom(BorderStyle.DOTTED);

        //水平单元格样式策略, 头是头的样式 内容是内容的样式
        HorizontalCellStyleStrategy cellStyleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);

        com.alibaba.excel.ExcelWriter excelWriter = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName + "-" + System.currentTimeMillis() , "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(response.getOutputStream(), data.get(0).getClass()).build();
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").registerWriteHandler(cellStyleStrategy).build();
            excelWriter.write(data, writeSheet);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }







}
