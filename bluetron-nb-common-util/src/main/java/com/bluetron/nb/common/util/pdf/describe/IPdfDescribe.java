package com.bluetron.nb.common.util.pdf.describe;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * PDF内容描述
 * @author genx
 * @date 2021/4/12 20:09
 */
public interface IPdfDescribe {

    /**
     * 渲染
     *
     * @param document
     * @param page
     * @param contentStream
     * @param font
     */
    void render(PDDocument document, PDPage page, PDPageContentStream contentStream, PDFont font) throws Exception;

}
