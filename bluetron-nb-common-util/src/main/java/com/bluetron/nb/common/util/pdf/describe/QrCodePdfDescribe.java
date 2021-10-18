package com.bluetron.nb.common.util.pdf.describe;

import com.bluetron.nb.common.util.QrCodeUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/4/12 20:10
 */
public class QrCodePdfDescribe extends AbsPdfDescribe {

    /**
     * 二维码内容
     */
    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(PDDocument document, PDPage page, PDPageContentStream contentStream, PDFont font) throws Exception {
        //通过测试发现 二维码尺寸放大四倍  在长文本时 最终大小会比较吻合
        BufferedImage qrImage = QrCodeUtil.createQRCode(getText(), getWidth() * 4, getHeight() * 4, 0);
        PDImageXObject image = JPEGFactory.createFromImage(document, qrImage, 1);

        float y = getY(page);

        contentStream.drawImage(image, getX(), y, getWidth(), getHeight());

    }

}
