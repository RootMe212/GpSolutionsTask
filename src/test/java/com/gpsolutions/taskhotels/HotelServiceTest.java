package com.gpsolutions.taskhotels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.gpsolutions.taskhotels.exception.HotelNotFound;
import com.gpsolutions.taskhotels.exception.InvalidHistogramParam;
import com.gpsolutions.taskhotels.mapper.HotelMapper;
import com.gpsolutions.taskhotels.repository.AmenityRepository;
import com.gpsolutions.taskhotels.repository.HotelRepository;
import com.gpsolutions.taskhotels.service.HotelServiceImpl;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

  @Mock
  private HotelRepository hotelRepository;

  @Mock
  private AmenityRepository amenityRepository;

  @Mock
  private HotelMapper hotelMapper;

  @InjectMocks
  private HotelServiceImpl hotelService;

  private Hotel hotel;
  private HotelResponse hotelResponse;
  private HotelDetailsResponse hotelDetailsResponse;

  @BeforeEach
  void setUp() {
    hotel = new Hotel();
    hotel.setId(1L);
    hotel.setName("DoubleTree by Hilton Minsk");
    hotel.setBrand("Hilton");
    hotel.setAddress(new Address(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"));
    hotel.setContacts(new Contacts("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"));
    hotel.setArrivalTime(new ArrivalTime(LocalTime.of(14, 0), LocalTime.of(12, 0)));
    hotel.setAmenities(new ArrayList<>());

    hotelResponse = new HotelResponse(
        1L,
        "DoubleTree by Hilton Minsk",
        "Description",
        "9 Pobediteley Avenue, Minsk, 220004, Belarus",
        "+375 17 309-80-00"
    );

    hotelDetailsResponse = new HotelDetailsResponse(
        1L,
        "DoubleTree by Hilton Minsk",
        "Description",
        "Hilton",
        new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
        new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
        new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0)),
        List.of()
    );
  }

  @Test
  void testCreateHotel() {
    CreateHotelRequest request = new CreateHotelRequest(
        "DoubleTree by Hilton Minsk",
        "Description",
        "Hilton",
        new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
        new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
        new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
    );

    when(hotelMapper.toEntity(request)).thenReturn(hotel);
    when(hotelRepository.save(any())).thenReturn(hotel);
    when(hotelMapper.toHotelResponse(hotel)).thenReturn(hotelResponse);

    HotelResponse result = hotelService.createHotel(request);

    assertNotNull(result);
    assertEquals("DoubleTree by Hilton Minsk", result.name());
    verify(hotelMapper).toEntity(request);
    verify(hotelRepository).save(hotel);
    verify(hotelMapper).toHotelResponse(hotel);
  }

  @Test
  void testGetAllHotels() {
    List<Hotel> hotels = List.of(hotel);
    when(hotelRepository.findAll()).thenReturn(hotels);
    when(hotelMapper.toHotelResponse(hotel)).thenReturn(hotelResponse);

    List<HotelResponse> hotelResponses = hotelService.getAllHotels();

    assertNotNull(hotelResponses);
    assertEquals(1, hotelResponses.size());
    assertEquals("DoubleTree by Hilton Minsk",hotelResponses.getFirst().name());
    verify(hotelRepository).findAll();
    verify(hotelMapper).toHotelResponse(hotel);
  }

  @Test
  void testGetHotelDetailsById() {
    when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
    when(hotelMapper.toHotelDetailsResponse(hotel)).thenReturn(hotelDetailsResponse);

    HotelDetailsResponse result = hotelService.getHotelDetailsById(1L);

    assertNotNull(result);
    assertEquals("DoubleTree by Hilton Minsk",result.name());
    assertEquals("Hilton", result.brand());
    verify(hotelRepository).findById(1L);
    verify(hotelMapper).toHotelDetailsResponse(hotel);
  }

  @Test
  void testGetHotelByIdNotFound() {
    when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

    HotelNotFound exception = assertThrows(HotelNotFound.class,
        () -> hotelService.getHotelDetailsById(999L));

    assertTrue(exception.getMessage().contains("999"));
    verify(hotelRepository).findById(999L);
    verify(hotelMapper, never()).toHotelDetailsResponse(any());
  }

  @Test
  void testAddExistingAmenity() {
    Amenity existingAmenity = new Amenity();
    existingAmenity.setId(1L);
    existingAmenity.setName("Free WiFi");

    when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
    when(amenityRepository.findByNameIgnoreCase("Free WiFi")).thenReturn(Optional.of(existingAmenity));

    hotelService.addAmenitiesToHotel(1L, List.of("Free WiFi"));

    assertTrue(hotel.getAmenities().contains(existingAmenity));
    verify(hotelRepository).findById(1L);
    verify(amenityRepository).findByNameIgnoreCase("Free WiFi");
    verify(amenityRepository, never()).save(any());
    verify(hotelRepository).save(hotel);
  }

  @Test
  void testAddNewAmenity() {
    when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
    when(amenityRepository.findByNameIgnoreCase("Mini bar")).thenReturn(Optional.empty());
    when(amenityRepository.save(any(Amenity.class))).thenAnswer(invocation -> {
      Amenity amenity = invocation.getArgument(0);
      amenity.setId(2L);
      return amenity;
    });

    hotelService.addAmenitiesToHotel(1L, List.of("Mini bar"));

    assertEquals(1, hotel.getAmenities().size());
    verify(amenityRepository).findByNameIgnoreCase("Mini bar");
    verify(amenityRepository).save(any(Amenity.class));
    verify(hotelRepository).save(hotel);
  }

  @Test
  void testSearchHotels() {
    List<Hotel> hotels = List.of(hotel);
    when(hotelRepository.searchHotels("minsk", null, "minsk", null, null)).thenReturn(hotels);
    when(hotelMapper.toHotelResponse(hotel)).thenReturn(hotelResponse);

    List<HotelResponse> result = hotelService.searchHotels("minsk", null, "minsk", null, null);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(hotelRepository).searchHotels("minsk", null, "minsk", null, null);
  }

  @Test
  void testGetHistogram() {
    List<Map<String, Object>> mockData = List.of(
        Map.of("key" , "Hilton", "value", 5L),
        Map.of("key", "Marriott", "value", 3L)
    );
    when(hotelRepository.countByBrand()).thenReturn(mockData);

    Map<String, Long> result = hotelService.getHistogram("brand");

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(5L, result.get("Hilton"));
    assertEquals(3L, result.get("Marriott"));
    verify(hotelRepository).countByBrand();
  }

  @Test
  void testGetHistogramInvalidParam() {
    InvalidHistogramParam exception = assertThrows(InvalidHistogramParam.class,
        () -> hotelService.getHistogram("invalid"));

    assertTrue(exception.getMessage().contains("invalid"));
    verify(hotelRepository, never()).countByBrand();
  }



}
