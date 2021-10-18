package com.bluetron.nb.common.util.pdf;

import com.bluetron.nb.common.util.pdf.describe.IPdfDescribe;
import com.bluetron.nb.common.util.pdf.describe.QrCodePdfDescribe;
import com.bluetron.nb.common.util.pdf.describe.RectanglePdfDescribe;
import com.bluetron.nb.common.util.pdf.describe.TextPdfDescribe;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PDF模板解析
 * @author genx
 * @date 2021/4/16 9:17
 */
public class TemplateResolverUtil {

    private static Configuration DEFAULT_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_0);

    static {
        //数值不加逗号
        DEFAULT_CONFIGURATION.setNumberFormat("#");
    }

    public static List<List<IPdfDescribe>> parseByResource(String location, List dataList, int pageSize) throws IOException, TemplateException {
        Resource resource = new DefaultResourceLoader().getResource(location);
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        //数值不加逗号
        configuration.setNumberFormat("#");
        Template template = new Template("", new InputStreamReader(resource.getInputStream()), configuration, "UTF-8");
        return parse(template, dataList, pageSize);
    }

    public static List<List<IPdfDescribe>> parseByText(String templateText, List dataList, int pageSize) throws IOException, TemplateException {
        Template template = new Template("", templateText, DEFAULT_CONFIGURATION);
        return parse(template, dataList, pageSize);
    }

    /**
     * 使用模板将数据 转换成 PDF描述
     * @param template 模板
     * @param dataList 具体的数据
     * @param pageSize 每页条数
     * @return
     */
    public static List<List<IPdfDescribe>> parse(Template template, List dataList, int pageSize) throws IOException, TemplateException {
        if (dataList == null) {
            throw new IllegalArgumentException("数据不能为空");
        } else if (dataList.size() == 0) {
            return new ArrayList(0);
        }


        List<List<IPdfDescribe>> result = new ArrayList(dataList.size() / pageSize + 1);
        List<List<Object>> list = splitList(dataList, pageSize);

        for (List<Object> objects : list) {
            StringWriter writer = new StringWriter();
            Map<String, Object> map = new HashMap(2);
            map.put("dataList", objects);
            template.process(map, writer);
            result.add(parseToPdfDescribeList(writer.toString()));
        }
        return result;
    }

    private static List<IPdfDescribe> parseToPdfDescribeList(String xml) {
        List<IPdfDescribe> list = new ArrayList(100);

        Document d = Jsoup.parse(xml);
        Element pageEl = d.getElementsByTag("page").first();

        for (Element element : pageEl.children()) {
            if ("rectangle".equals(element.tagName())) {
                RectanglePdfDescribe rectangle = new RectanglePdfDescribe();
                rectangle.setX(NumberUtils.toFloat(element.attr("x")));
                rectangle.setY(NumberUtils.toFloat(element.attr("y")));
                rectangle.setWidth(NumberUtils.toFloat(element.attr("width")));
                rectangle.setHeight(NumberUtils.toFloat(element.attr("height")));
                rectangle.setDashStyle(new int[]{8, 4, 4, 4});
                rectangle.setColor(element.attr("color"));
                list.add(rectangle);
            } else if ("text".equals(element.tagName())) {
                TextPdfDescribe text = new TextPdfDescribe();
                text.setX(NumberUtils.toFloat(element.attr("x")));
                text.setY(NumberUtils.toFloat(element.attr("y")));
                text.setWidth(NumberUtils.toFloat(element.attr("width")));
                text.setHeight(NumberUtils.toFloat(element.attr("height")));
                text.setFontSize(NumberUtils.toFloat(element.attr("font-size")));
                text.setText(element.text().replace("\\n", "\n"));
                list.add(text);
            } else if ("qrcode".equals(element.tagName())) {
                QrCodePdfDescribe qrcode = new QrCodePdfDescribe();
                qrcode.setX(NumberUtils.toFloat(element.attr("x")));
                qrcode.setY(NumberUtils.toFloat(element.attr("y")));
                qrcode.setWidth(NumberUtils.toFloat(element.attr("width")));
                qrcode.setHeight(NumberUtils.toFloat(element.attr("height")));
                qrcode.setText(element.attr("text"));
                list.add(qrcode);
            }
        }
        return list;
    }


    public static <T> List<List<T>> splitList(List<T> dataList, int pageSize) {
        List<List<T>> result = new ArrayList(dataList.size() / pageSize);
        List<T> temp = new ArrayList(pageSize);
        for (T item : dataList) {
            temp.add(item);
            if (temp.size() >= pageSize) {
                result.add(temp);
                temp = new ArrayList(pageSize);
            }
        }
        if (temp.size() > 0) {
            result.add(temp);
        }
        return result;
    }

}
