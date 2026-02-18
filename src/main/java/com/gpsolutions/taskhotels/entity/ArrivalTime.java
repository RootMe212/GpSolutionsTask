package com.gpsolutions.taskhotels.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {

  @Column(name = "check_in")
  private LocalTime checkIn;

  @Column(name = "check_out")
  private LocalTime checkOut;

}
