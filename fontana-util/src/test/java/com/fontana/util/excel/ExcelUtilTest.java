package com.fontana.util.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.fontana.util.bean.BeanUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: ExcelUtilTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/10/20 15:22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication()
@Slf4j
public class ExcelUtilTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Before
    public void before() {

    }

    @Test
    public void testReadXlsx() {

        List<List<Object>> dataList = ExcelUtil.readExcel(ResourceUtil.getStream("aaa.xlsx"));

        dataList.forEach(t -> log.info(t.toString()));

        // 标题
        Assert.assertEquals("姓名", dataList.get(0).get(0));
        Assert.assertEquals("性别", dataList.get(0).get(1));
        Assert.assertEquals("年龄", dataList.get(0).get(2));
        Assert.assertEquals("鞋码", dataList.get(0).get(3));

        // 第一行
        Assert.assertEquals("张三", dataList.get(1).get(0));
        Assert.assertEquals("男", dataList.get(1).get(1));
        Assert.assertEquals(11L, dataList.get(1).get(2));
        Assert.assertEquals(41.5D, dataList.get(1).get(3));


        dataList = ExcelUtil.readExcel(ResourceUtil.getStream("aaa.xlsx"), "12");

        dataList.forEach(t -> log.info(t.toString()));


    }

    @Test
    public void testReadXls() {
        List<List<Object>> dataList = ExcelUtil.readExcel(ResourceUtil.getStream("aaa.xls"));

        dataList.forEach(t -> log.info(t.toString()));

        log.info("-------------华丽的分隔符------------");
        dataList = ExcelUtil.readExcel(ResourceUtil.getStream("aaa.xls"), "校园入学");

        dataList.forEach(t -> log.info(t.toString()));


    }

    /**
     * 测试简单的list输出到表格
     */

    @Test
    public void testWriteList() {

        List<Object> s1 = Lists.newArrayList("aa", "bb", "cc", "dd");
        List<Object> s2 = Lists.newArrayList("aa1", "bb1", "cc1", "dd1");
        List<Object> s3 = Lists.newArrayList("aa2", "bb2", "cc2", "dd2");
        List<Object> s4 = Lists.newArrayList("aa3", "bb3", "cc3", "dd3");
        List<Object> s5 = Lists.newArrayList("aa4", "bb4", "cc4", "dd4");

        List<List<Object>> ss = Lists.newArrayList(s1, s2, s3, s4, s5);

        //自定义表格名、大标题
        FileUtil.del("d:/testData/excel/writeTest1.xlsx");
        FileUtil.del("d:/testData/excel/writeTest1.xls");
        File file1 = FileUtil.newFile("d:/testData/excel/writeTest1.xlsx");
        File file2 = FileUtil.newFile("d:/testData/excel/writeTest1.xls");
        ExcelUtil.writeListToExcel(ss, file1, "test", "测试标题");
        ExcelUtil.writeListToExcel(ss, file2, "test", "测试标题");

        //只有数据，没有大标题，也不自定义表格名
        FileUtil.del("d:/testData/excel/writeTest2.xlsx");
        FileUtil.del("d:/testData/excel/writeTest2.xls");
        file1 = FileUtil.newFile("d:/testData/excel/writeTest2.xlsx");
        file2 = FileUtil.newFile("d:/testData/excel/writeTest2.xls");
        ExcelUtil.writeListToExcel(ss, file1);
        ExcelUtil.writeListToExcel(ss, file2);

        //自定义大标题
        FileUtil.del("d:/testData/excel/writeTest3.xlsx");
        FileUtil.del("d:/testData/excel/writeTest3.xls");
        file1 = FileUtil.newFile("d:/testData/excel/writeTest3.xlsx");
        file2 = FileUtil.newFile("d:/testData/excel/writeTest3.xls");
        ExcelUtil.writeListToExcel(ss, file1, "测试标题");
        ExcelUtil.writeListToExcel(ss, file2, "测试标题");

    }

    /**
     * 测试简单的map输出到Excel
     */

    @Test
    public void testWriteMap() {

        Map<Object, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());

        Map<Object, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<Object, Object>> ss = CollUtil.newArrayList(row1, row2);

        FileUtil.del("d:/testData/excel/writeTest4.xls");
        File file = FileUtil.newFile("d:/testData/excel/writeTest4.xls");
        ExcelUtil.writeMapToExcel(ss, file, "test", "测试标题");

        FileUtil.del("d:/testData/excel/writeTest5.xls");
        file = FileUtil.newFile("d:/testData/excel/writeTest5.xls");
        ExcelUtil.writeMapToExcel(ss, file);

        FileUtil.del("d:/testData/excel/writeTest6.xls");
        file = FileUtil.newFile("d:/testData/excel/writeTest6.xls");
        ExcelUtil.writeMapToExcel(ss, file, "测试标题");

    }

    /**
     * EasyPoi导出excel方法
     * @throws IOException
     */
    @Test
    public void testExportExcel1() throws IOException {

        SysUserExcel user1 = new SysUserExcel();
        user1.setUsername("user1");
        user1.setNickname("user1");
        user1.setSex(0);
        user1.setMobile("12313213213213");
        user1.setCreateTime(DateUtil.date());
        user1.setUpdateTime(DateUtil.date());
        List<SysUserExcel> userExcelList = new ArrayList<>();


        SysUserExcel user2 = new SysUserExcel();
        BeanUtil.copyProperties(user1, user2);
        user2.setUsername("user2");

        SysUserExcel user3 = new SysUserExcel();
        BeanUtil.copyProperties(user1, user3);
        user3.setUsername("user3");

        userExcelList.add(user1);
        userExcelList.add(user2);
        userExcelList.add(user3);


        ExcelUtil.exportExcel(userExcelList, null, "用户", SysUserExcel.class, "user", response);

        log.info(response.getContentAsString());
        //Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(null, "用户", ExcelType.XSSF), SysUserExcel.class, userExcelList);
        // Workbook workbook = ExcelExportUtil.exportExcel(userExcelList, ExcelType.XSSF);
        // workbook.close();
    }

    /**
     * hutool导出excel方法
     * @throws IOException
     */
    @Test
    public void testExportExcel2() throws IOException {

        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<String, Object>> ss = CollUtil.newArrayList(row1, row2);

        ExcelUtil.exportExcel(ss, "测试成绩", response, "测试sheet", "测试标题");
        log.info(response.getContentAsString());

    }

    /**
     * easyPoi导入
     * @throws Exception
     */
    @Test
    public void testImport1() throws Exception {
        File file = new File("src/test/resources/SysUserExcel.xls");
        MultipartFile mulFile = new MockMultipartFile(
                "SysUserExcel.xls", //文件名
                "SysUserExcel.xls", //originalName 相当于上传文件在客户机上的文件名
                ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                new FileInputStream(file) //文件流
        );
        List<SysUserExcel> list = ExcelUtil.importExcel(mulFile, SysUserExcel.class);
        list.forEach(System.out::println);

    }

    /**
     * easyPoi导入
     * @throws Exception
     */
    @Test
    public void testImport2() throws Exception {
        File file = new File("src/test/resources/SysUserExcel.xls");
        MultipartFile mulFile = new MockMultipartFile(
                "SysUserExcel.xls", //文件名
                "SysUserExcel.xls", //originalName 相当于上传文件在客户机上的文件名
                ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                new FileInputStream(file) //文件流
        );
        List<List<Object>> list = ExcelUtil.readExcel(mulFile.getInputStream());
        list.forEach(System.out::println);

    }
}


