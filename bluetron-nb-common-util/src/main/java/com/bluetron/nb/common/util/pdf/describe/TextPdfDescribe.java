package com.bluetron.nb.common.util.pdf.describe;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * 文字内容
 * @author genx
 * @date 2021/4/13 12:09
 */
public class TextPdfDescribe extends AbsPdfDescribe {

    /**
     * 文字
     */
    private String text;

    /**
     * 字体大小
     */
    private float fontSize = 16;

    /**
     * 行高
     * 为空时 默认 fontSize * 1.5
     */
    private Float lineHeight;

    @Override
    public void render(PDDocument document, PDPage page, PDPageContentStream contentStream, PDFont font) throws Exception {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);

        List<StringBuilder> list = new ArrayList();
        StringBuilder sb = new StringBuilder();

        char[] data = text.toCharArray();

        for (char datum : data) {
            if (datum == '\n') {
                list.add(sb);
                sb = new StringBuilder();
            } else {
                sb.append(datum);
                try {
                    if (font.getStringWidth(sb.toString()) * fontSize / 1000f > getWidth()) {
                        sb.deleteCharAt(sb.length() - 1);
                        list.add(sb);
                        sb = new StringBuilder();
                        sb.append(datum);
                    }
                }catch (Exception e){
                    // 字体不认识的字符
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
        }

        if (sb.length() > 0) {
            list.add(sb);
        }

        float lineHeight = this.lineHeight != null ? this.lineHeight : this.fontSize * 1.5f;
        float textHeight = lineHeight * list.size();

        //TODO 位置还是不准
        float y = getY(page)  + (getHeight() - textHeight) / 2 + (lineHeight - fontSize) / 2 + 2;
        for (int i = list.size() - 1; i >= 0; i--) {
            contentStream.setTextMatrix(Matrix.getTranslateInstance(getX(), y));
            contentStream.showText(list.get(i).toString());
            y += lineHeight;
        }

        contentStream.endText();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Float getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(Float lineHeight) {
        this.lineHeight = lineHeight;
    }
}
