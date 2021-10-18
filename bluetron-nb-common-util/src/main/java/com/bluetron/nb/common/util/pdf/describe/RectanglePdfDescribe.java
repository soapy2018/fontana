package com.bluetron.nb.common.util.pdf.describe;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/4/12 20:58
 */
public class RectanglePdfDescribe extends AbsPdfDescribe {

    /**
     * 边框宽度
     */
    private int borderWidth = 1;

    /**
     * 边框样式
     */
    private int[] dashStyle;

    /**
     * 边框颜色
     */
    private String color;


    @Override
    public void render(PDDocument document, PDPage page, PDPageContentStream contentStream, PDFont font) throws Exception {
        contentStream.setLineWidth(getBorderWidth());
        contentStream.setStrokingColor(Color.DARK_GRAY);

        //Drawing a rectangle
        contentStream.addRect(getX(),
                getY(page),
                getWidth(),
                getHeight());

        //Drawing a rectangle
        contentStream.stroke();

    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int[] getDashStyle() {
        return dashStyle;
    }

    public void setDashStyle(int[] dashStyle) {
        this.dashStyle = dashStyle;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
