package com.udacity.udacitybaking.util;

public class StringUtil {

  private StringUtil() {
  }

  public static String capitalize(String text) {
    return text.substring(0, 1).toUpperCase().concat(text.substring(1, text.length()));
  }
}
