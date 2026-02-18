package com.gpsolutions.taskhotels.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ArrivalTimeDto (
    @JsonFormat(pattern = "HH:mm")
    LocalTime checkIn,
    @JsonFormat(pattern = "HH:mm")
    LocalTime checkOut
) {}
