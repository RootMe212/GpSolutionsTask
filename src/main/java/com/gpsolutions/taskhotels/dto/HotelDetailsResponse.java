package com.gpsolutions.taskhotels.dto;

import java.util.List;

public record HotelDetailsResponse(
  Long id,
  String name,
  String description,
  String brand,
  AddressDto address,
  ContactsDto contacts,
  ArrivalTimeDto arrivalTime,
  List<String> amenities
) {}