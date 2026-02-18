package com.gpsolutions.taskhotels.exception;

public class HotelNotFound extends RuntimeException {
  public HotelNotFound(Long id) {
    super("Hotel id:" + id + " not found");
  }

}
