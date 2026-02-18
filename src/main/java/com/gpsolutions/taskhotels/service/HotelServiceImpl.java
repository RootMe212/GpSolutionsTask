package com.gpsolutions.taskhotels.service;

import com.gpsolutions.taskhotels.dto.CreateHotelRequest;
import com.gpsolutions.taskhotels.dto.HotelDetailsResponse;
import com.gpsolutions.taskhotels.dto.HotelResponse;
import com.gpsolutions.taskhotels.entity.Amenity;
import com.gpsolutions.taskhotels.entity.Hotel;
import com.gpsolutions.taskhotels.exception.HotelNotFound;
import com.gpsolutions.taskhotels.exception.InvalidHistogramParam;
import com.gpsolutions.taskhotels.mapper.HotelMapper;
import com.gpsolutions.taskhotels.repository.AmenityRepository;
import com.gpsolutions.taskhotels.repository.HotelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelServiceImpl implements HotelService {
  private final HotelRepository hotelRepository;
  private final AmenityRepository amenityRepository;
  private final HotelMapper hotelMapper;

  public HotelServiceImpl(HotelRepository hotelRepository, AmenityRepository amenityRepository,
      HotelMapper hotelMapper) {
    this.hotelRepository = hotelRepository;
    this.amenityRepository = amenityRepository;
    this.hotelMapper = hotelMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<HotelResponse> getAllHotels() {
    return hotelRepository.findAll().stream()
        .map(hotelMapper::toHotelResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public HotelDetailsResponse getHotelDetailsById(Long id) {
    Hotel hotel = hotelRepository.findById(id)
        .orElseThrow(() -> new HotelNotFound(id));
    return hotelMapper.toHotelDetailsResponse(hotel);
  }

  @Override
  @Transactional
  public HotelResponse createHotel(CreateHotelRequest request) {
    Hotel hotel = hotelMapper.toEntity(request);
    Hotel savedHotel = hotelRepository.save(hotel);
    return hotelMapper.toHotelResponse(savedHotel);
  }

  @Override
  @Transactional
  public void addAmenitiesToHotel(Long hotelId, List<String> amenitiesNames) {
    Hotel hotel = hotelRepository.findById(hotelId)
        .orElseThrow(() -> new HotelNotFound(hotelId));

    for (String amenitiesName : amenitiesNames) {
      Amenity amenity = amenityRepository.findByNameIgnoreCase(amenitiesName)
          .orElseGet(() ->{
            Amenity newAmenity = new Amenity();
            newAmenity.setName(amenitiesName);
            return amenityRepository.save(newAmenity);
          });
      hotel.addAmenity(amenity);
    }

    hotelRepository.save(hotel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<HotelResponse> searchHotels(String name, String brand, String city, String country,
      String amenity) {
    List<Hotel> hotels = hotelRepository.searchHotels(name, brand, city, country, amenity);
    return hotels.stream()
        .map(hotelMapper::toHotelResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, Long> getHistogram(String param) {
    List<Map<String,Object>> results = switch (param.toLowerCase()) {
      case "brand" -> hotelRepository.countByBrand();
      case "city" -> hotelRepository.countByCity();
      case "country" -> hotelRepository.countByCountry();
      case "amenities" -> hotelRepository.countByAmenities();
      default -> throw new InvalidHistogramParam(param);
    };

    return convertToHistogramMap(results);
  }

  private Map<String, Long> convertToHistogramMap(List<Map<String, Object>> results) {
    Map<String, Long> histogram = new HashMap<>();
    for (Map<String, Object> result : results) {
      String key = (String) result.get("key");
      Long value = ((Number) result.get("value")).longValue();
      histogram.put(key, value);
    }
    return histogram;
  }
}
