package com.gpsolutions.taskhotels.exception;

public class InvalidHistogramParam extends RuntimeException {
  public InvalidHistogramParam(String param) {
    super("Invalid histogram param: " + param + ". Valid values are: brand, city, country, amenities");
  }
}
