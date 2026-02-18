package com.gpsolutions.taskhotels.dto;

public record HotelResponse(
   Long id,
   String name,
   String description,
   String address,
   String phone
) {}
