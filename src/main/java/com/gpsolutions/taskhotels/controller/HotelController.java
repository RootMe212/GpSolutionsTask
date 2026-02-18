package com.gpsolutions.taskhotels.controller;

import com.gpsolutions.taskhotels.dto.CreateHotelRequest;
import com.gpsolutions.taskhotels.dto.HotelDetailsResponse;
import com.gpsolutions.taskhotels.dto.HotelResponse;
import com.gpsolutions.taskhotels.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property-view")
@Tag(name = "Hotels", description = "Hotels API")
public class HotelController {

  private final HotelService hotelService;

  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @Operation(summary = "Get all hotels", description = "Returns a list of all hotels with brief info")
  @ApiResponse(responseCode = "200", description = "Hotels returned successfully")
  @GetMapping("/hotels")
  public ResponseEntity<List<HotelResponse>> getAllHotels() {
    List<HotelResponse> hotels = hotelService.getAllHotels();
    return ResponseEntity.ok(hotels);
  }

  @Operation(summary = "Get hotel by ID", description = "Returns full details of a hotel")
  @ApiResponse(responseCode = "200", description = "Hotel found")
  @ApiResponse(responseCode = "404", description = "Hotel not found")
  @GetMapping("/hotels/{id}")
  public ResponseEntity<HotelDetailsResponse> getHotelById(@PathVariable Long id) {
    HotelDetailsResponse hotel = hotelService.getHotelDetailsById(id);
    return ResponseEntity.ok(hotel);
  }

  @Operation(summary = "Search hotels", description = "Search hotels by name, brand, city, country or amenity")
  @ApiResponse(responseCode = "200", description = "Search results returned")
  @GetMapping("/search")
  public ResponseEntity<List<HotelResponse>> searchHotel(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String country,
      @RequestParam(required = false) String amenities
  ) {
    List<HotelResponse> hotels = hotelService.searchHotels(name, brand, city, country, amenities);
    return ResponseEntity.ok(hotels);
  }

  @Operation(summary = "Create hotel", description = "Creates a new hotel")
  @ApiResponse(responseCode = "201", description = "Hotel created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid request data")
  @PostMapping("/hotels")
  public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody CreateHotelRequest request) {
    HotelResponse hotel = hotelService.createHotel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
  }

  @Operation(summary = "Add amenities to hotel", description = "Adds a list of amenities to an existing hotel")
  @ApiResponse(responseCode = "200", description = "Amenities added successfully")
  @ApiResponse(responseCode = "404", description = "Hotel not found")
  @PostMapping("/hotels/{id}/amenities")
  public ResponseEntity<Void> addAmenities(
      @PathVariable Long id,
      @RequestBody List<String> amenities
  ){
    hotelService.addAmenitiesToHotel(id, amenities);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Get histogram", description = "Returns count of hotels grouped by parameter (brand, city, country, amenities).")
  @ApiResponse(responseCode = "200", description = "Histogram returned successfully")
  @ApiResponse(responseCode = "400", description = "Invalid parameter")
  @GetMapping("/histogram/{param}")
  public ResponseEntity<Map<String,Long>> getHistogram(@PathVariable String param) {
    Map<String,Long> histogram = hotelService.getHistogram(param);
    return ResponseEntity.ok(histogram);
  }
}
