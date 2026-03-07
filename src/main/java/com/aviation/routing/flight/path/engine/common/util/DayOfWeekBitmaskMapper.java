package com.aviation.routing.flight.path.engine.common.util;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

@UtilityClass
public final class DayOfWeekBitmaskMapper {

    public static short toBitmask(Set<DayOfWeek> days) {
        if (CollectionUtils.isEmpty(days)) {
            return 0;
        }
        short mask = 0;
        for (DayOfWeek day : days) {
            mask |= (short) (1 << (day.getValue() - 1));
        }
        return mask;
    }

    public static Set<DayOfWeek> toDays(short mask) {
        Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            if ((mask & (1 << (day.getValue() - 1))) != 0) {
                days.add(day);
            }
        }
        return days;
    }
}