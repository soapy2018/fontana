package com.fontana.util.validate;

import com.fontana.base.exception.GeneralException;
import com.fontana.base.result.ResultCode;
import com.fontana.util.lang.ObjectUtil;
import org.springframework.lang.Nullable;

import java.util.function.Supplier;

/**
 * @author yegenchang
 * @description
 * @date 2022/8/10 20:09
 */
public class LogicAssert {

  /**
   * 断言对象为true
   * @param expression
   * @param message
   */
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new GeneralException(message);
    }
  }

  /**
   * 断言对象为true
   * @param expression
   * @param resultCode 结果
   */
  public static void isTrue(boolean expression, ResultCode resultCode) {
    if (!expression) {
      throw new GeneralException(resultCode);
    }
  }

  /**
   * 断言对象为true
   * @param expression
   * @param resultCode
   * @param message
   */
  public static void isTrue(boolean expression, ResultCode resultCode, String message) {
    if (!expression) {
      throw new GeneralException(resultCode, message);
    }
  }

  /**
   * 断言对象为true
   * @param expression
   * @param messageSupplier
   */
  public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
    if (!expression) {
      throw new GeneralException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * 断言对象为null
   * @param object
   * @param message
   */
  public static void isNull(@Nullable Object object, String message) {
    if (object != null) {
      throw new GeneralException(message);
    }
  }

  /**
   * 断言对象为null
   * @param object
   * @param resultCode
   */
  public static void isNull(@Nullable Object object, ResultCode resultCode){
    if (object != null) {
      throw new GeneralException(resultCode);
    }
  }

  /**
   * 断言对象为null
   * @param object
   * @param resultCode
   * @param message
   */
  public static void isNull(@Nullable Object object, ResultCode resultCode, String message){
    if (object != null) {
      throw new GeneralException(resultCode, message);
    }
  }

  /**
   * 断言对象为null
   * @param object
   * @param messageSupplier
   */
  public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
    if (object != null) {
      throw new GeneralException(nullSafeGet(messageSupplier));
    }
  }

  /**
   * 断言对象不为null
   * @param object
   * @param message
   */
  public static void notNull(@Nullable Object object, String message) {
    if (object == null) {
      throw new GeneralException(message);
    }
  }

  /**
   * 断言对象为null
   * @param object
   * @param resultCode
   */
  public static void notNull(@Nullable Object object,  ResultCode resultCode) {
    if (object == null) {
      throw new GeneralException(resultCode);
    }
  }

  /**
   * 断言对象为null
   * @param object
   * @param resultCode
   * @param message
   */
  public static void notNull(@Nullable Object object,  ResultCode resultCode, String message) {
    if (object == null) {
      throw new GeneralException(resultCode, message);
    }
  }

  /**
   * 断言对象不为null
   * @param object
   * @param messageSupplier
   */
  public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
    if (object == null) {
      throw new GeneralException(nullSafeGet(messageSupplier));
    }
  }

  @Nullable
  private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
    return messageSupplier != null ? (String)messageSupplier.get() : null;
  }

  /**
   * 断言对象不为null或者blank
   * @param object
   * @param message
   */
  public static void isNotBlankOrNull(@Nullable Object object, String message) {
    if (ObjectUtil.isBlankOrNull(object)) {
      throw new GeneralException(message);
    }
  }

  /**
   * 断言对象不为null或者blank
   * @param object
   * @param resultCode
   */
  public static void isNotBlankOrNull(@Nullable Object object,  ResultCode resultCode) {
    if (ObjectUtil.isBlankOrNull(object)) {
      throw new GeneralException(resultCode);
    }
  }

  /**
   * 断言对象不为null或者blank
   * @param object
   * @param resultCode
   * @param message
   */
  public static void isNotBlankOrNull(@Nullable Object object,  ResultCode resultCode, String message) {
    if (ObjectUtil.isBlankOrNull(object)) {
      throw new GeneralException(resultCode, message);
    }
  }


}
