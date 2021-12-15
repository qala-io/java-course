package io.qala.javatraining.utils;

import java.time.ZonedDateTime;

public class DateUtil {
    public static long beginningOfTime() {
        // Java8 Time API goes beyond of what DBs can hold so we explicitly generate the dates in the boundaries
        // that a DB can hold. Which looks like in boundaries of long type.
        // Also 808 comes presumably from the bug somewhere around Timestamp, Date, Calendar.. For some reason
        // dates near Long.MIN_VALUE underflow when we convert them to Timestamp. Starting from MIN + 808 they start
        // to work fine. Looks like another instance of http://bugs.java.com/view_bug.do?bug_id=7000693 Will need to
        // research at some point. Why don't they use randomized testing in Sun/Oracle..
        //
        // In later versions of Hibernate (tested on 5.6.2) in OffsetDateTimeJavaDescriptor they started to
        // check if the year is < 1905 and if so - they use Timestamp.valueOf() instead of Timestamp.from(),
        // this in turn causes sun.util.calendar.JulianCalender to be used which makes a weird setNormalizedYear()
        // for negative years: 1-year. This causes the date to become positive, and we get a positive year
        // stored in DB ¯\_(ツ)_/¯
        // For this reason our min date starts with year 1905.
        return ZonedDateTime.now().withYear(1905).toInstant().toEpochMilli();
    }
}
