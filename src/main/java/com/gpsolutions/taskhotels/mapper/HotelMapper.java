package com.gpsolutions.taskhotels.mapper;

import com.gpsolutions.taskhotels.dto.AddressDto;
import com.gpsolutions.taskhotels.dto.ArrivalTimeDto;
import com.gpsolutions.taskhotels.dto.ContactsDto;
import com.gpsolutions.taskhotels.dto.CreateHotelRequest;
import com.gpsolutions.taskhotels.dto.HotelDetailsResponse;
import com.gpsolutions.taskhotels.dto.HotelResponse;
import com.gpsolutions.taskhotels.entity.Address;
import com.gpsolutions.taskhotels.entity.Amenity;
import com.gpsolutions.taskhotels.entity.ArrivalTime;
import com.gpsolutions.taskhotels.entity.Contacts;
import com.gpsolutions.taskhotels.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

  public HotelResponse toHotelResponse(Hotel hotel) {
    Address address = hotel.getAddress();
    String formattedAddress = String.format("%d %s, %s, %s, %s",
        address.getHouseNumber(),
        address.getStreet(),
        address.getCity(),
        address.getPostCode(),
        address.getCountry());

    return new HotelResponse(
        hotel.getId(),
        hotel.getName(),
        hotel.getDescription(),
        formattedAddress,
        hotel.getContacts().getPhone()
    );
  }

  public HotelDetailsResponse toHotelDetailsResponse(Hotel hotel) {
    return new HotelDetailsResponse(
        hotel.getId(),
        hotel.getName(),
        hotel.getDescription(),
        hotel.getBrand(),
        new AddressDto(
            hotel.getAddress().getHouseNumber(),
            hotel.getAddress().getStreet(),
            hotel.getAddress().getCity(),
            hotel.getAddress().getCountry(),
            hotel.getAddress().getPostCode()
        ),
        new ContactsDto(
            hotel.getContacts().getPhone(),
            hotel.getContacts().getEmail()
        ),
        new ArrivalTimeDto(
            hotel.getArrivalTime().getCheckIn(),
            hotel.getArrivalTime().getCheckOut()
        ),
        hotel.getAmenities().stream()
            .map(Amenity::getName)
            .toList()
    );
  }

  public Hotel toEntity(CreateHotelRequest request) {
    Hotel hotel = new Hotel();
    hotel.setName(request.name());
    hotel.setDescription(request.description());
    hotel.setBrand(request.brand());

    hotel.setAddress(new Address(
        request.address().houseNumber(),
        request.address().street(),
        request.address().city(),
        request.address().country(),
        request.address().postCode()
    ));

    hotel.setContacts(new Contacts(
        request.contacts().phone(),
        request.contacts().email()
    ));

    if (request.arrivalTime() != null) {
        hotel.setArrivalTime(new ArrivalTime(
          request.arrivalTime().checkIn(),
          request.arrivalTime().checkOut()
    ));
    }

    return hotel;
  }
}
