package com.bluetron.nb.common.util.pdf;

import cn.hutool.core.date.DateUtil;
import com.bluetron.nb.common.util.excel.SysUserExcel;
import com.bluetron.nb.common.util.tools.BeanUtil;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @className: pdfUtilTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/10/22 14:54
 */
public class PdfUtilTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Before
    public void before() {

    }

    @Test
    public void testPdfWrite() {
        String fileName = "d:/pdfTest.pdf";
        File file = new File(fileName);
        FileOutputStream out = null;
        try {

            // PDDocument document = new PDDocument();
            //(1)实例化文档对象
            //第一个参数是页面大小，接下来的参数分别是左、右、上和下页边距
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            //(2)创建写入器
            //第一个参数是对文档对象的引用，第二个参数是输出的文件，将out和document连接起来
            out = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            //打开文档准备写入内容
            document.open();
            //(3)下面创建章节对象
            //首先创建段落对象，作为章节的标题。FontFactory用于指定段落的字体
            Font font = FontFactory.getFont(FontFactory.HELVETICA,
                    18, Font.BOLDITALIC, new Color(0, 0, 255));
            Paragraph chapter1_title = new Paragraph("Chapter_1", font);
            //创建了一个章节对象，标题为“Chapter_1”
            Chapter chapter_1 = new Chapter(chapter1_title, 1);
            //将编号级别设为0就不会再页面上显示章节编号
            chapter_1.setNumberDepth(0);

            //(4)创建小节对象
            //创建小节对象的标题
            font = FontFactory.getFont(FontFactory.HELVETICA, 16,
                    Font.BOLD, new Color(255, 0, 0));
            Paragraph section1_title = new Paragraph("Section 1 of Chapter 1", font);
            //创建一个小节对象，标题为"This is Section 1 in Chapter 1"，属于chapter1。
            Section section = chapter_1.addSection(section1_title);
            //(5)往小节中写文本内容
            Paragraph text = new Paragraph("This is the first text in section 1 of chapter 1.");
            section.add(text);
            text = new Paragraph("Following is a 5×5 table:");
            section.add(text);
            //(6)往小节中写表格
            //创建表格对象
            Table table = new Table(5, 5);
            //设置表格边框颜色
            table.setBorderColor(new Color(220, 255, 100));
            //设置单元格的边距间隔等
            table.setPadding(1);
            table.setSpacing(1);
            table.setBorderWidth(1);
            //单元格对象
            Cell cell = null;
            //添加表头信息
            for (int colNum = 0; colNum < 5; colNum++) {
                cell = new Cell("header-" + colNum);
                cell.setHeader(true);
                table.addCell(cell);
            }
            table.endHeaders();
            //添加表的内容
            for (int rowNum = 1; rowNum < 5; rowNum++) {
                for (int colNum = 0; colNum < 5; colNum++) {
                    cell = new Cell("value-" + rowNum + "-" + colNum);
                    table.addCell(cell);
                }
            }
            section.add(table);
            //将表格对象添加到小节对象中
            //(7)添加列表
            //列表包含一定数量的ListItem.可以对列表进行编号，也可以不编号
            //将第一个参数设置为true 表明想创建一个进行编号的列表
            //第二个参数设置为true表示列表采用字母进行编码，为false则用数字进行编号
            //第三个参数为列表内容与编号之间的距离
            List list = new List(true, false, 20);
            ListItem item = new ListItem("First item of list;");
            list.add(item);
            item = new ListItem("Second item of list;");
            list.add(item);
            item = new ListItem("Third item of list.");
            list.add(item);
            //将列表对象添加到小节对象中
            section.add(list);
            //(8)添加中文
            //允许在PDF中写入中文，将字体文件放在classPath中。
            //fonts/SourceHanSansCN-Bold.ttf是字体文件
            BaseFont bfChinese = BaseFont.createFont("fonts/SourceHanSansCN-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            //中文大小为20，加粗
            font = new Font(bfChinese, 20, Font.BOLD);
            String context = "这是中文要添加的信息.....";
            text = new Paragraph("PDF中文测试" + context, font);
            section.add(text);
            //(9)将章节对象加入到文档中
            document.add(chapter_1);
            //(10)关闭文档
            document.close();
            System.out.println("PDF文件生成成功，PDF文件名：" + file.getAbsolutePath());
        } catch (DocumentException e) {
            System.out.println("PDF文件" + file.getAbsolutePath() + "生成失败！" + e);
            e.printStackTrace();
        } catch (IOException ee) {
            System.out.println("PDF文件" + file.getAbsolutePath() + "生成失败！" + ee);
            ee.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    //关闭输出文件流
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testPdfWrite1() {
        String fileName = "d:/pdfTest1.pdf";
        File file = new File(fileName);
        FileOutputStream out = null;
        try {

            // PDDocument document = new PDDocument();
            //(1)实例化文档对象
            //第一个参数是页面大小，接下来的参数分别是左、右、上和下页边距
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            //(2)创建写入器
            //第一个参数是对文档对象的引用，第二个参数是输出的文件，将out和document连接起来
            out = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            //打开文档准备写入内容
            document.open();
            //(3)下面创建章节对象
            //首先创建段落对象，作为章节的标题。FontFactory用于指定段落的字体
            Font font = FontFactory.getFont(FontFactory.HELVETICA,
                    18, Font.BOLDITALIC, new Color(0, 0, 255));
            Chapter chapter_1 = PdfUtil.createChapter(font, "Chapter1", 0);
//   Paragraph chapter1_title = new Paragraph("Chapter_1",font);
//   //创建了一个章节对象，标题为“Chapter_1”
//   Chapter chapter_1 = new Chapter(chapter1_title, 1);
//   //将编号级别设为0就不会再页面上显示章节编号
//   chapter_1.setNumberDepth(0);


            //(4)创建小节对象
            //创建小节对象的标题
            font = FontFactory.getFont(FontFactory.HELVETICA, 16,
                    Font.BOLD, new Color(255, 0, 0));

            Section section = PdfUtil.createSection(chapter_1, "Section 1 of Chapter 1", font);
//   Paragraph section1_title = new Paragraph("Section 1 of Chapter 1", font);
//   //创建一个小节对象，标题为"This is Section 1 in Chapter 1"，属于chapter1。
//   Section section = chapter_1.addSection(section1_title);
            //(5)往小节中写文本内容


            PdfUtil.addTextToSection(section, "This is the first text in section 1 of chapter 1.");
            PdfUtil.addTextToSection(section, "This is the first text in section 1 of chapter 1.", font);

//   Paragraph text = new Paragraph("This is the first text in section 1 of chapter 1.");
//   section.add(text);
//   text = new Paragraph("Following is a 5×5 table:");
//   section.add(text);
            //(6)往小节中写表格
            //创建表格对象
//   Table table = new Table(5,5);
//   //设置表格边框颜色
//   table.setBorderColor(new Color(220,255,100));
//   //设置单元格的边距间隔等
//   table.setPadding(1);
//   table.setSpacing(1);
//   table.setBorderWidth(1);
//   //单元格对象
//   Cell cell = null;
//   //添加表头信息
//   for(int colNum=0;colNum<5;colNum++){
//    cell = new Cell("header-"+colNum);
//    cell.setHeader(true);
//    table.addCell(cell);
//   }
//   table.endHeaders();
//   //添加表的内容
//   for(int rowNum=1;rowNum<5;rowNum++){
//    for(int colNum=0;colNum<5;colNum++){
//     cell = new Cell("value-"+rowNum+"-"+colNum);
//     table.addCell(cell);
//    }
//   }
//   section.add(table);

            SysUserExcel user1 = new SysUserExcel();
            user1.setUsername("user1");
            user1.setNickname("小红");
            user1.setSex(0);
            user1.setMobile("12313213213213");
            user1.setCreateTime(DateUtil.date());
            user1.setUpdateTime(DateUtil.date());
            java.util.List<SysUserExcel> userExcelList = new ArrayList<>();


            SysUserExcel user2 = new SysUserExcel();
            BeanUtil.copyProperties(user1, user2);
            user2.setUsername("user2");

            SysUserExcel user3 = new SysUserExcel();
            BeanUtil.copyProperties(user1, user3);
            user3.setUsername("user3");

            userExcelList.add(user1);
            userExcelList.add(user2);
            userExcelList.add(user3);

            //允许在PDF中写入中文，将字体文件放在classPath中。
            //fonts/SourceHanSansCN-Bold.ttf是字体文件
            BaseFont bfChinese = BaseFont.createFont("fonts/SourceHanSansCN-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font dataFont = new Font(bfChinese, 8, Font.NORMAL);
            //中文大小为20，加粗 new Font(bfChinese,20,Font.BOLD);
            Font headFont = new Font(bfChinese, 10, Font.BOLD);
            PdfUtil.addTableToSection(section, userExcelList, headFont, dataFont);
            //将表格对象添加到小节对象中
            //(7)添加列表
            //列表包含一定数量的ListItem.可以对列表进行编号，也可以不编号
            //将第一个参数设置为true 表明想创建一个进行编号的列表
            //第二个参数设置为true表示列表采用字母进行编码，为false则用数字进行编号
            //第三个参数为列表内容与编号之间的距离
            List list = new List(true, false, 20);
            ListItem item = new ListItem("First item of list;");
            list.add(item);
            item = new ListItem("Second item of list;");
            list.add(item);
            item = new ListItem("Third item of list.");
            list.add(item);
            //将列表对象添加到小节对象中
            PdfUtil.addListToSection(section, list);

            //section.add(list);
            //(8)添加中文
            //允许在PDF中写入中文，将字体文件放在classPath中。
            //fonts/SourceHanSansCN-Bold.ttf是字体文件
            //中文大小为20，加粗
            font = new Font(bfChinese, 20, Font.BOLD);
            String context = "这是中文要添加的信息.....";
            Paragraph text = new Paragraph("PDF中文测试" + context, font);
            section.add(text);
            //(9)将章节对象加入到文档中
            //document.add(list);

            //创建表格对象
            Table table = new Table(5, 5);
            //设置表格边框颜色
            table.setBorderColor(new Color(220, 255, 100));
            //设置单元格的边距间隔等
            table.setPadding(1);
            table.setSpacing(1);
            table.setBorderWidth(1);
            //单元格对象
            Cell cell = null;
            //添加表头信息
            for (int colNum = 0; colNum < 5; colNum++) {
                cell = new Cell("header-" + colNum);
                cell.setHeader(true);
                table.addCell(cell);
            }
            table.endHeaders();
            //添加表的内容
            for (int rowNum = 1; rowNum < 5; rowNum++) {
                for (int colNum = 0; colNum < 5; colNum++) {
                    cell = new Cell("value-" + rowNum + "-" + colNum);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.add(chapter_1);

            //(10)关闭文档
            document.close();
            System.out.println("PDF文件生成成功，PDF文件名：" + file.getAbsolutePath());
        } catch (DocumentException e) {
            System.out.println("PDF文件" + file.getAbsolutePath() + "生成失败！" + e);
            e.printStackTrace();
        } catch (IOException ee) {
            System.out.println("PDF文件" + file.getAbsolutePath() + "生成失败！" + ee);
            ee.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    //关闭输出文件流
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testExportPdf() throws DocumentException, IOException {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        //创建表格对象
        Table table = new Table(5, 5);
        //设置表格边框颜色
        table.setBorderColor(new Color(220, 255, 100));
        //设置单元格的边距间隔等
        table.setPadding(1);
        table.setSpacing(1);
        table.setBorderWidth(1);
        //单元格对象
        Cell cell = null;
        //添加表头信息
        for (int colNum = 0; colNum < 5; colNum++) {
            cell = new Cell("header-" + colNum);
            cell.setHeader(true);
            table.addCell(cell);
        }
        table.endHeaders();
        //添加表的内容
        for (int rowNum = 1; rowNum < 5; rowNum++) {
            for (int colNum = 0; colNum < 5; colNum++) {
                cell = new Cell("value-" + rowNum + "-" + colNum);
                table.addCell(cell);
            }
        }

        Paragraph text = new Paragraph("This is the first text in section 1 of chapter 1.");
        //首先创建段落对象，作为章节的标题。FontFactory用于指定段落的字体
        Font font = FontFactory.getFont(FontFactory.HELVETICA,
                18, Font.BOLDITALIC, new Color(0, 0, 255));
        Chapter chapter = PdfUtil.createChapter(font, "Chapter1", 0);

        //创建小节对象的标题
        font = FontFactory.getFont(FontFactory.HELVETICA, 16,
                Font.BOLD, new Color(255, 0, 0));

        Section section = PdfUtil.createSection(chapter, "Section 1 of Chapter 1", font);

        PdfUtil.addTextToSection(section, "This is the first text in section 1 of chapter 1.");
        PdfUtil.addTextToSection(section, "This is the first text in section 1 of chapter 1.", font);


        SysUserExcel user1 = new SysUserExcel();
        user1.setUsername("user1");
        user1.setNickname("小红");
        user1.setSex(0);
        user1.setMobile("12313213213213");
        user1.setCreateTime(DateUtil.date());
        user1.setUpdateTime(DateUtil.date());
        java.util.List<SysUserExcel> userExcelList = new ArrayList<>();


        SysUserExcel user2 = new SysUserExcel();
        BeanUtil.copyProperties(user1, user2);
        user2.setUsername("user2");

        SysUserExcel user3 = new SysUserExcel();
        BeanUtil.copyProperties(user1, user3);
        user3.setUsername("user3");

        userExcelList.add(user1);
        userExcelList.add(user2);
        userExcelList.add(user3);

        //允许在PDF中写入中文，将字体文件放在classPath中。
        //fonts/SourceHanSansCN-Bold.ttf是字体文件
        BaseFont bfChinese = BaseFont.createFont("fonts/SourceHanSansCN-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font dataFont = new Font(bfChinese, 8, Font.NORMAL);
        //中文大小为20，加粗 new Font(bfChinese,20,Font.BOLD);
        Font headFont = new Font(bfChinese, 10, Font.BOLD);
        PdfUtil.addTableToSection(section, userExcelList, headFont, dataFont);

        PdfUtil.exportPdf(document, response, "test", chapter, text, table);

    }
}


