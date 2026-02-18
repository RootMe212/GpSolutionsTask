package com.gpsolutions.taskhotels;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.gpsolutions.taskhotels.dto.AddressDto;
import com.gpsolutions.taskhotels.dto.ArrivalTimeDto;
import com.gpsolutions.taskhotels.dto.ContactsDto;
import com.gpsolutions.taskhotels.dto.CreateHotelRequest;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HotelControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreateHotel() throws Exception {
    CreateHotelRequest request = new CreateHotelRequest(
        "DoubleTree by Hilton Minsk",
        "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor",
        "Hilton",
        new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
        new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
        new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
    );

    mockMvc.perform(post("/property-view/hotels")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
        .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"));
  }

  @Test
  void testGetAllHotels() throws Exception {
    mockMvc.perform(get("/property-view/hotels"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void testNotFoundError() throws Exception {
    mockMvc.perform(get("/property-view/hotels/99"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Not Found"));
  }

  @Test
  void testSearchHotels() throws Exception {
    mockMvc.perform(get("/property-view/search")
        .param("city", "minsk"))
        .andExpect(status().isOk());
  }

  @Test
  void testGetHotelById() throws Exception {
    CreateHotelRequest request = new CreateHotelRequest(
        "DoubleTree by Hilton Minsk",
        "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor",
        "Hilton",
        new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
        new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
        new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
    );

    String response = mockMvc.perform(post("/property-view/hotels")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    long hotelId = objectMapper.readTree(response).get("id").asLong();

    mockMvc.perform(get("/property-view/hotels/" + hotelId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
        .andExpect(jsonPath("$.brand").value("Hilton"))
        .andExpect(jsonPath("$.address.city").value("Minsk"));
  }

  @Test
  void testAddAmenities() throws Exception {
    CreateHotelRequest request = new CreateHotelRequest(
        "DoubleTree by Hilton Minsk",
        "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor",
        "Hilton",
        new AddressDto(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
        new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
        new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
    );

    String response = mockMvc.perform(post("/property-view/hotels")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    long hotelId = objectMapper.readTree(response).get("id").asLong();

    List<String> amenities = List.of(
        "Free parking",
        "Free WiFi",
        "Non-smoking rooms",
        "Concierge",
        "On-site restaurant",
        "Fitness center",
        "Pet-friendly rooms",
        "Room service",
        "Business center",
        "Meeting rooms"
    );

    mockMvc.perform(post("/property-view/hotels/" + hotelId + "/amenities")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(amenities)))
        .andExpect(status().isOk());
  }

  @Test
  void testHistogram() throws Exception {
    mockMvc.perform(get("/property-view/histogram/brand"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/property-view/histogram/city"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/property-view/histogram/country"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/property-view/histogram/amenities"))
        .andExpect(status().isOk());
  }


}
