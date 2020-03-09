package pl.zmudzin.library.domain.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * @author Piotr Żmudzin
 */
public class DateTimeUtil {

    public static LocalDateTime skipWeekends(LocalDateTime dateTime) {
        if (dateTime.getDayOfWeek() == DayOfWeek.SATURDAY || dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dateTime = dateTime.toLocalDate().atTime(0, 0);
        }
        if (dateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
            dateTime = dateTime.plusDays(2);
        } else if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dateTime = dateTime.plusDays(1);
        }
        return dateTime;
    }
}
