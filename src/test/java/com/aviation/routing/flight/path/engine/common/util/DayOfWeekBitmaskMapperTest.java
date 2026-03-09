package com.aviation.routing.flight.path.engine.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class DayOfWeekBitmaskMapperTest {

    @Test
    void toBitmask_shouldReturnCorrectValue() {
        assertEquals((short) 0, DayOfWeekBitmaskMapper.toBitmask(null));
        assertEquals((short) 0, DayOfWeekBitmaskMapper.toBitmask(EnumSet.noneOf(DayOfWeek.class)));
        
        // MONDAY: 1 << 0 = 1
        assertEquals((short) 1, DayOfWeekBitmaskMapper.toBitmask(EnumSet.of(DayOfWeek.MONDAY)));
        
        // TUESDAY: 1 << 1 = 2
        assertEquals((short) 2, DayOfWeekBitmaskMapper.toBitmask(EnumSet.of(DayOfWeek.TUESDAY)));
        
        // MONDAY, WEDNESDAY, FRIDAY: 1+4+16 = 21
        assertEquals((short) 21, DayOfWeekBitmaskMapper.toBitmask(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));
        
        // ALL DAYS: 1+2+4+8+16+32+64 = 127
        assertEquals((short) 127, DayOfWeekBitmaskMapper.toBitmask(EnumSet.allOf(DayOfWeek.class)));
    }

    @Test
    void toDays_shouldReturnCorrectSet() {
        assertTrue(DayOfWeekBitmaskMapper.toDays((short) 0).isEmpty());
        
        Set<DayOfWeek> mondayOnly = DayOfWeekBitmaskMapper.toDays((short) 1);
        assertEquals(1, mondayOnly.size());
        assertTrue(mondayOnly.contains(DayOfWeek.MONDAY));
        
        Set<DayOfWeek> monWedFri = DayOfWeekBitmaskMapper.toDays((short) 21);
        assertEquals(3, monWedFri.size());
        assertTrue(monWedFri.contains(DayOfWeek.MONDAY));
        assertTrue(monWedFri.contains(DayOfWeek.WEDNESDAY));
        assertTrue(monWedFri.contains(DayOfWeek.FRIDAY));
        
        Set<DayOfWeek> allDays = DayOfWeekBitmaskMapper.toDays((short) 127);
        assertEquals(7, allDays.size());
        assertTrue(allDays.containsAll(EnumSet.allOf(DayOfWeek.class)));
    }

    @Test
    void roundTrip_shouldPreserveData() {
        Set<DayOfWeek> original = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        short mask = DayOfWeekBitmaskMapper.toBitmask(original);
        Set<DayOfWeek> result = DayOfWeekBitmaskMapper.toDays(mask);
        
        assertEquals(original, result);
    }
}
