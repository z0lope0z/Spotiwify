package com.lopefied.spotiwify.time;

import org.joda.time.Interval;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lope on 4/23/15.
 */
public class TimeServiceImpl implements TimeService {

    @Override
    public Set<String> getTagsNow() {
        Set<String> tagsNow = new HashSet<>();
        if (isWorkingTime()) {
            tagsNow.add("WORK");
        }
        if (isHomeTime()) {
            tagsNow.add("HOME");
        }
        return tagsNow;
    }

    protected Boolean isWorkingTime() {
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        if (!((calStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ||
                (calStart.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))) {
            calStart.set(Calendar.HOUR_OF_DAY, 9);
            calEnd.set(Calendar.HOUR_OF_DAY, 19);
            return new Interval(calStart.getTime().getTime(), calEnd.getTime().getTime()).containsNow();
        }
        return false;
    }

    protected Boolean isHomeTime() {
        return !isWorkingTime();
    }

}
