package com.gpsolutions.taskhotels.service;

import com.gpsolutions.taskhotels.dto.CreateHotelRequest;
import com.gpsolutions.taskhotels.dto.HotelDetailsResponse;
import com.gpsolutions.taskhotels.dto.HotelResponse;
import java.util.List;
import java.util.Map;

public interface HotelService {
  List<HotelResponse> getAllHotels();

  HotelDetailsResponse getHotelDetailsById(Long id);

  HotelResponse createHotel(CreateHotelRequest request);

  void addAmenitiesToHotel(Long hotelId, List<String> amenitiesNames);

  List<HotelResponse> searchHotels(String name, String brand, String city,
      String country, String amenity);

  Map<String, Long> getHistogram(String param);

}
