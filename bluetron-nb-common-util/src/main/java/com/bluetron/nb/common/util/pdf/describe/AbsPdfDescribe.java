package com.bluetron.nb.common.util.pdf.describe;

import org.apache.pdfbox.pdmodel.PDPage;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/4/12 20:09
 */
public abstract class AbsPdfDescribe implements IPdfDescribe {

    private float x;
    private float y;
    private float width;
    private float height;


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * 转换 y坐标起始位置
     * @param page
     * @return
     */
    public int getY(PDPage page) {
        return (int) (page.getMediaBox().getHeight() - getY() - getHeight());
    }
}
