package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Getter
@AllArgsConstructor
public enum DashboardType {

  HOURLY(8, 3600, ChronoUnit.HOURS),
  DAILY(24, 3 * 3600, ChronoUnit.HOURS),
  WEEKLY(7, 24 * 3600, ChronoUnit.DAYS);

  private int totalHours;

  private int secondsInterval;

  private TemporalUnit temporalUnit;

}
