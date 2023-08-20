package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supr.orderservice.validators.ValidCardDetails;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@ValidCardDetails
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardData {
  @ToString.Exclude
  private String nameOnCard;

  @ToString.Exclude
  private String numberEncrypted;

  private String tokenIdentifier;

  @ToString.Exclude
  @Length(min = 3, max = 4, message = "Please enter a valid cvv")
  private String cvv;

  @ToString.Exclude
  @Length(min = 2, max = 2, message = "Please enter a valid 2 digit month")
  private String expiryMonth;

  @ToString.Exclude
  @Length(min = 4, max = 4, message = "Please enter a valid 4 digit year")
  private String expiryYear;
}
