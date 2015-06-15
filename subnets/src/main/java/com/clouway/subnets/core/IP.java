package com.clouway.subnets.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class IP {

  public Long getNetworkIP(String host) {
    String subnetIPtoBinary = "";
    String[] arr = host.split("\\.");

    for (String current : arr) {
      subnetIPtoBinary += decimalToBinary(Integer.parseInt(current));
    }
    return Long.parseLong(subnetIPtoBinary, 2);
  }

  public static String getHost(Long networkIP) {

    Map<Integer, Long> octetsMap = new LinkedHashMap<>();
    octetsMap.put(24, 0xFF000000L);
    octetsMap.put(16, 0x00FF0000L);
    octetsMap.put(8, 0x0000FF00L);
    octetsMap.put(0, 0x000000FFL);
    String host = "";
    for (Entry<Integer, Long> entry : octetsMap.entrySet()) {
      host += binaryToDecimal(Long.toBinaryString((networkIP & entry.getValue()) >> entry.getKey())) + ".";
    }
    int lastDot = host.indexOf(".", host.length() - 1);
    return new StringBuffer(host).deleteCharAt(lastDot).toString();

  }

  private static Integer binaryToDecimal(String binary) {
    return Integer.parseInt(binary, 2);
  }

  /**
   * Returns a string with a decimal number converted from binary
   */

  private String decimalToBinary(int decimal) {
    String binaryResult = Integer.toBinaryString(decimal);
    if (binaryResult.length() != 8) {
      binaryResult = appendZeroToLength(binaryResult, 8);
    }
    return binaryResult;
  }
  /**
   * The method will append 0 to the given string until the length is reached
   * @param binaryResult
   * @param length
   * @return
   */
  private String appendZeroToLength(String binaryResult, int length) {
    while (binaryResult.length() != length) {
      binaryResult = "0" + binaryResult;
    }
    return binaryResult;
  }

}
