package com.bluetron.nb.common.util.pdf;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bluetron.nb.common.util.lang.StringUtil;
import com.bluetron.nb.common.util.tools.ReflectUtil;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.*;

/**
 * Description: 
 * @author CQF
 * @date 2021/10/15 16:57
 */
public class PdfUtil {

    private PdfUtil(){
        throw new IllegalStateException("Utility class");
    }
    public static Chapter createChapter(Font font, String chapterTitle, int num){
        //首先创建段落对象，作为章节的标题。FontFactory用于指定段落的字体
//        Font font = FontFactory.getFont(FontFactory.HELVETICA,
//                18,Font.BOLDITALIC,new Color(0,0,255));
//        Paragraph chapterTitle = new Paragraph("Chapter1", font);
        //创建了一个章节对象，标题为“Chapter1”
        Chapter chapter = new Chapter(chapterTitle, num);
        //将编号级别设为0就不会再页面上显示章节编号
        chapter.setNumberDepth(num);
        return chapter;
    }

    public static Section createSection(Chapter chapter, String sectionTitle, Font font){

//        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16,
//                Font.BOLD, new Color(255, 0, 0));
        Paragraph sectionTitlePar = new Paragraph(sectionTitle, font);
        //创建一个小节对象，附上标题，属于chapter
        return chapter.addSection(sectionTitlePar);

    }

    public static Section addTextToSection(Section section, String text, Font font){

//        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16,
//                Font.BOLD, new Color(255, 0, 0));
        //往小节中写文本内容
        Paragraph textPar;
        if(null == font){
             textPar = new Paragraph(text);
        }else {
             textPar = new Paragraph(text, font);
        }
        section.add(textPar);
        return section;
    }

    public static Section addTextToSection(Section section, String text){
        return addTextToSection(section, text, null);
    }

    public static Section addTableToSection(Section section, Table table){
        section.add(table);
        return section;
    }

    public static Section addListToSection(Section section, com.lowagie.text.List list){
        section.add(list);
        return section;
    }

    public static Section addTableToSection(Section section, Collection<?> dataSet, Font headFont, Font dataFont) throws DocumentException, IOException {

        List<List<Map>> datas = new ArrayList<>();
        for(Object o : dataSet){
            Field[] fields = o.getClass().getDeclaredFields();
            List<Map> list = new ArrayList();
            Map infoMap = null;
            for (int i = 0; i < fields.length; i++) {

                Field field = fields[i];
                //过滤掉静态属性
                if(!Modifier.isStatic(field.getModifiers())) {
                    infoMap = new HashMap();
                    if (field.getAnnotation(Excel.class) != null) {
                        Excel excel = (Excel) field.getAnnotation(Excel.class);
                        infoMap.put("name", StringUtil.isBlank(excel.name()) ? field.getName() : excel.name());
                    } else {
                        infoMap.put("name", field.getName());
                    }
                    infoMap.put("value", ReflectUtil.getFieldValueByName(field.getName(), o));
                    list.add(infoMap);
                }
            }
            datas.add(list);
        }

        Table table = new Table(datas.get(0).size(), datas.size());
        //设置表格边框颜色
        table.setBorderColor(new Color(220,255,100));
        //设置单元格的边距间隔等
        table.setPadding(1);
        table.setSpacing(1);
        table.setBorderWidth(1);


        if(datas!=null && datas.size()>0){
            datas.get(0).forEach(head ->  {
                Paragraph text = new Paragraph(head.get("name").toString() , headFont);
                Cell cell = null;
                try {
                    cell = new Cell(text);
                } catch (BadElementException e) {
                    e.printStackTrace();
                }
                //Cell cell = new Cell("LALA")
                cell.setHeader(true);
                table.addCell(cell);
            });
        }

        table.endHeaders();

        datas.forEach(data -> {
            data.forEach(d ->{

                String value = d.get("value")==null ? "": d.get("value").toString();
                Paragraph text = new Paragraph(value , dataFont);
                Cell cell = null;
                try {
                    cell = new Cell(text);
                } catch (BadElementException e) {
                    e.printStackTrace();
                }
                table.addCell(cell);
            });
        });
        section.add(table);
        return section;
    }

//    public static createPdf(){
//        //第一个参数是页面大小，接下来的参数分别是左、右、上和下页边距
//        Document document = new Document(PageSize.A4,50,50,50,50);
//        //(2)创建写入器
//        //第一个参数是对文档对象的引用，第二个参数是输出的文件，将out和document连接起来
//        out = new FileOutputStream(file);
//
//    }

    public static void exportPdf(Document document, HttpServletResponse response, String fileName, Element... elements) throws DocumentException, IOException {

        //FileOutputStream out = new FileOutputStream("d:/pdfTest1.pdf");
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8" + fileName + ".pdf");

        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        //PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        for(Element e : elements){
            document.add(e);
        }
        document.close();
    }





}
