package com.gpsolutions.taskhotels.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  private String brand;

  @Embedded
  private Address address;

  @Embedded
  private Contacts contacts;

  @Embedded
  private ArrivalTime arrivalTime;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {
      CascadeType.PERSIST, CascadeType.MERGE
  })
  @JoinTable(
      name = "hotel_amenities",
      joinColumns = @JoinColumn(name = "hotel_id"),
      inverseJoinColumns = @JoinColumn(name = "amenity_id")
  )
  private List<Amenity> amenities = new ArrayList<>();

  public void addAmenity(Amenity amenity) {
    this.amenities.add(amenity);
  }

  public void removeAmenity(Amenity amenity) {
    this.amenities.remove(amenity);
  }
}
