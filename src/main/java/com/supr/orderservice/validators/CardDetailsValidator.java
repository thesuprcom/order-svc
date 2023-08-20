package com.supr.orderservice.validators;

import com.google.common.base.Strings;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.CardData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;

@Slf4j
public class CardDetailsValidator implements ConstraintValidator<ValidCardDetails, CardData> {

  private Calendar currentCalendar;


  @Override
  public void initialize(ValidCardDetails constraintAnnotation) {
    currentCalendar = Calendar.getInstance();
    LocalDateTime localDateTime = LocalDateTime.now();
    currentCalendar.set(Calendar.YEAR, localDateTime.getYear());
    currentCalendar.set(Calendar.MONTH, getMonthValue(localDateTime.getMonthValue()));
  }

  @Override
  public boolean isValid(CardData cardData, ConstraintValidatorContext context) {
    if (!(Strings.isNullOrEmpty(cardData.getNumberEncrypted()) &&
        Strings.isNullOrEmpty(cardData.getExpiryMonth()) &&
        Strings.isNullOrEmpty(cardData.getExpiryYear()))) {

      validateCardExpiry(cardData);
      Calendar cardExpiryCalendar = createCardExpireCalendar(cardData);

      if (cardExpiryCalendar.compareTo(currentCalendar) == -1) {
        throw new OrderServiceException(ErrorEnum.INVALID_CARD_EXPIRY, HttpStatus.BAD_REQUEST);
      }
    }

    return true;
  }

  private void validateCardExpiry(CardData cardData) {
    if (Strings.isNullOrEmpty(cardData.getExpiryMonth())) {
      throw new OrderServiceException(ErrorEnum.INVALID_CARD_EXPIRY, HttpStatus.BAD_REQUEST);
    }

    if (Strings.isNullOrEmpty(cardData.getExpiryYear())) {
      throw new OrderServiceException(ErrorEnum.INVALID_CARD_EXPIRY, HttpStatus.BAD_REQUEST);
    }

    if (Strings.isNullOrEmpty(cardData.getNumberEncrypted())) {
      throw new OrderServiceException(ErrorEnum.INVALID_CARD_NUMBER, HttpStatus.BAD_REQUEST);
    }
  }

  private Calendar createCardExpireCalendar(CardData cardData) {
    Calendar cardExpiryCalendar = Calendar.getInstance();
    cardExpiryCalendar.setLenient(false);
    int year = Integer.parseInt(cardData.getExpiryYear());
    int month = Integer.parseInt(cardData.getExpiryMonth());

    cardExpiryCalendar.set(Calendar.YEAR, year);
    cardExpiryCalendar.set(Calendar.MONTH, getMonthValue(month));
    cardExpiryCalendar.set(Calendar.DATE, getLastDateOfTheMonth(year, month));

    try {
      cardExpiryCalendar.getTime(); // This will trigger validation of date on calendar object
    } catch (Exception exception) {
      log.error("Error while parsing data", exception);
      throw new OrderServiceException(ErrorEnum.INVALID_CARD_EXPIRY, HttpStatus.BAD_REQUEST);
    }
    return cardExpiryCalendar;
  }

  private int getLastDateOfTheMonth(final int year, final int month) {
    // Month starts from 1
    return Month.of(month).length(Year.isLeap(year));
  }

  private int getMonthValue(int month) {
    // Month starts from 0
    return month - 1;
  }
}
