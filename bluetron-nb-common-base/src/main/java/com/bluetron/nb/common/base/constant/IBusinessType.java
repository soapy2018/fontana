package com.bluetron.nb.common.base.constant;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * @author genx
 * @date 2021/8/11 13:13
 */
public interface IBusinessType {

    String name();

    default String getCode(){
        return name();
    }

    String getLabel();

    String getSequenceCode();

}
