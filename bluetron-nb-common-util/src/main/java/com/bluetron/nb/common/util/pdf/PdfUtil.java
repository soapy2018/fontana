package com.bluetron.nb.common.util.pdf;

import com.alibaba.fastjson.JSON;
import com.bluetron.nb.common.util.pdf.describe.IPdfDescribe;
import com.bluetron.nb.common.util.pdf.describe.QrCodePdfDescribe;
import com.bluetron.nb.common.util.pdf.describe.RectanglePdfDescribe;
import com.bluetron.nb.common.util.pdf.describe.TextPdfDescribe;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 
 * @author genx
 * @date 2021/4/15 16:57
 */
public class PdfUtil {

    public static void writePdf(List<QrCode> list, OutputStream outputStream) throws Exception {
        List<List<QrCode>> dataList = TemplateResolverUtil.splitList(list, 10);
        QrCode qrCodeReq;
        List<List<IPdfDescribe>> pdfDescribeListList = new ArrayList(dataList.size());
        for (List<QrCode> qrCodeReqs : dataList) {
            List<IPdfDescribe> pdfDescribeList = new ArrayList(10 * 3);
            for (int i = 0; i < qrCodeReqs.size(); i++) {
                qrCodeReq = qrCodeReqs.get(i);
                //矩形
                RectanglePdfDescribe rectangle = new RectanglePdfDescribe();
                rectangle.setX(i % 2 == 0 ? 22 : 22 + 265 + 20);
                rectangle.setY(160 * (i / 2) + 20);
                rectangle.setWidth(265);
                rectangle.setHeight(140);
                rectangle.setDashStyle(new int[]{8, 4, 4, 4});
                pdfDescribeList.add(rectangle);
                //文本框
                TextPdfDescribe text = new TextPdfDescribe();
                text.setX((i % 2 == 0 ? 22 : 22 + 265 + 20) + 13);
                text.setY(160 * (i / 2) + 20 + 20);
                text.setWidth(130);
                text.setHeight(100);
                text.setFontSize(11);
                text.setText(StringUtils.join(qrCodeReq.getTextLines(), "\n"));
                pdfDescribeList.add(text);
                //二维码
                QrCodePdfDescribe qrCodePdfAnnotation = new QrCodePdfDescribe();
                qrCodePdfAnnotation.setX((i % 2 == 0 ? 22 : 22 + 265 + 20) + 146);
                qrCodePdfAnnotation.setY(160 * (i / 2) + 20 + 20);
                qrCodePdfAnnotation.setWidth(100);
                qrCodePdfAnnotation.setHeight(100);
                qrCodePdfAnnotation.setText(JSON.toJSONString(qrCodeReq.getQrcodeContent()));
                pdfDescribeList.add(qrCodePdfAnnotation);
            }
            pdfDescribeListList.add(pdfDescribeList);
        }
        writePdfData(pdfDescribeListList, outputStream);
    }

    /**
     * 写 PDF 数据到 outputStream
     * @param list
     * @param outputStream
     * @throws Exception
     */
    public static void writePdfData(List<List<IPdfDescribe>> list, OutputStream outputStream) throws Exception {
        PDDocument document = new PDDocument();

        Resource resource = new DefaultResourceLoader().getResource("classpath:fonts/SourceHanSansCN-Bold.ttf");
        PDType0Font font = PDType0Font.load(document, resource.getInputStream());


        for (List<IPdfDescribe> pdfDescribes : list) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document,
                    page, PDPageContentStream.AppendMode.APPEND, false);
            for (IPdfDescribe pdfDescribe : pdfDescribes) {
                pdfDescribe.render(document, page, contentStream, font);
            }
            contentStream.close();
            page.getAnnotations().stream().forEach(item -> item.constructAppearances());
        }
        document.save(outputStream);
        document.close();
    }

    private static List<List<Object>> splitList(List<Object> dataList, int pageSize) {
        List<List<Object>> result = new ArrayList(dataList.size() / pageSize);
        List<Object> temp = new ArrayList(pageSize);
        for (Object item : dataList) {
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
