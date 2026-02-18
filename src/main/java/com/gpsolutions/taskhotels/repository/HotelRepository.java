package com.gpsolutions.taskhotels.repository;

import com.gpsolutions.taskhotels.entity.Hotel;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


  List<Hotel> findByNameContainingIgnoreCase(String name);
  List<Hotel> findByBrandIgnoreCase(String brand);
  List<Hotel> findByAddress_CityIgnoreCase(String city);
  List<Hotel> findByAddress_CountryIgnoreCase(String country);

  @Query("Select distinct h from Hotel h join h.amenities a where lower(a.name) = lower(:amenityName)")
  List<Hotel> findByAmenityName(@Param("amenityName") String amenityName);

  /**
   * GET /search
   * @param name partial match, case-insensitive
   * @param brand exact match, case-insensitive
   * @param city exact match, case-insensitive
   * @param country exact match, case-insensitive
   * @param amenity exact match for at least one amenity, case-insensitive
   * @return list of hotels matching all non-null parameter
   */
  @Query("select distinct h from Hotel h left join h.amenities a " +
      "where (:name is null or lower(h.name) like lower(concat('%', :name, '%'))) " +
      "and (:brand is null or lower(h.brand) = lower(:brand)) " +
      "and (:city is null or lower(h.address.city) = lower(:city)) " +
      "and (:country is null or lower(h.address.country) = lower(:country)) " +
      "and (:amenity is null or lower(a.name) = lower(:amenity))")
  List<Hotel> searchHotels (@Param("name") String name,
      @Param("brand") String brand,
      @Param("city") String city,
      @Param("country") String country,
      @Param("amenity") String amenity);


  // GET /histogram/brand
  @Query("select h.brand as key, count(h) as value from Hotel h where h.brand is not null group by h.brand")
  List<Map<String, Object>> countByBrand();

  // GET /histogram/city
  @Query("select h.address.city as key, count(h) as value from Hotel h where h.address.city is not null group by h.address.city")
  List<Map<String, Object>> countByCity();


  //GET /histogram/country
  @Query("select h.address.country as key, count(h) as value from Hotel h where h.address.country is not null group by h.address.country")
  List<Map<String, Object>> countByCountry();


  // GET /histogram/amenities
  @Query("select a.name as key, count(h) as value from Hotel h join h.amenities a group by a.name")
  List<Map<String, Object>> countByAmenities();

}
