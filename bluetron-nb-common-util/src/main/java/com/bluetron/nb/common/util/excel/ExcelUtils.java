package com.bluetron.nb.common.util.excel;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author genx
 * @date 2021/6/16 12:53
 */
public class ExcelUtils {

    public static HorizontalCellStyleStrategy horizontalCellStyleStrategy() {
        WriteCellStyle headerStyle = new WriteCellStyle();
        headerStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("TEXT"));
        return new HorizontalCellStyleStrategy(headerStyle, contentStyle);
    }

    public static final List<String> splitStringList(String str) {
        List<String> result = new ArrayList<>();
        String[] strs = StringUtils.split(str, ",|，|\\\n");
        for (String s : strs) {
            if (!s.equals("") && !s.replaceAll(" ", "").equals("")) {
                result.add(s.trim());
            }
        }
        return result;
    }

}
