package com.clouway.subnets.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by ivan.genchev1989@gmail.com.
 */
public class NewSubnet {

  static class IP {

    public static Long getNetworkIP(String host) {
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

    private static String decimalToBinary(int decimal) {
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
    private static String appendZeroToLength(String binaryResult, int length) {
      while (binaryResult.length() != length) {
        binaryResult = "0" + binaryResult;
      }

      return binaryResult;
    }
  }

  public final String nodeId;
  public final String subnetIP;
  public final int slash;
  public final String description;

  public NewSubnet(String nodeId, String subnetIP, int slash, String description) {
    this.nodeId = nodeId;
    this.subnetIP = subnetIP;
    this.slash = slash;
    this.description = description;
  }


  public Long getMinIP() {
    return IP.getNetworkIP(subnetIP) & getSubnetMask();
  }

  public Long getMaxIP() {
    return getMinIP() + (1 << 32 - slash);
  }

  public String getHost(Long nativeValue) {
    return IP.getHost(nativeValue);
  }

  public Long getSubnetMask() {
    long mask = 0xFFFFFFFFL << (32 - slash) & 0xFFFFFFFFL;
    return mask;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewSubnet newSubnet = (NewSubnet) o;

    if (slash != newSubnet.slash) return false;
    if (description != null ? !description.equals(newSubnet.description) : newSubnet.description != null) return false;
    if (nodeId != null ? !nodeId.equals(newSubnet.nodeId) : newSubnet.nodeId != null) return false;
    if (subnetIP != null ? !subnetIP.equals(newSubnet.subnetIP) : newSubnet.subnetIP != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = nodeId != null ? nodeId.hashCode() : 0;
    result = 31 * result + (subnetIP != null ? subnetIP.hashCode() : 0);
    result = 31 * result + slash;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
