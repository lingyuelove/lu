package com.luxuryadmin.common.aop.check;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author monkey king
 * @date 2020-05-28 20:34:47
 */
public class DateTimeValidator implements ConstraintValidator<DateTime, String> {
   private DateTime dateTime;

   @Override
   public void initialize(DateTime dateTime) {
      this.dateTime = dateTime;
   }

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      // 如果 value 为空则不进行格式验证，为空验证可以使用 @NotBlank @NotNull @NotEmpty 等注解来进行控制，职责分离
      if (value == null) {
         return true;
      }
      String format = dateTime.format();

      if (value.length() != format.length()) {
         return false;
      }

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

      try {
         Date parseDate = simpleDateFormat.parse(value);

         String formatDate = simpleDateFormat.format(parseDate);
         //严格按照格式来进行校验
         if (value.equals(formatDate)) {
            return true;
         }
      } catch (Exception ignored){

      }
      return false;
   }
}
