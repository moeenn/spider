package com.spider;

import java.time.LocalTime;

public class Log {
    public static void println(String message) {
        String time = getTime();
        System.out.printf("%s - %s\n", time, message);
    }

    public static void printf(String format, Object... args) {
        String time = getTime();
        Object[] allArgs = new Object[args.length + 1];
        allArgs[0] = time;
        System.arraycopy(args, 0, allArgs, 1, args.length);
        System.out.printf("%s - " + format + "\n", allArgs);
    }

    private static String getTime() {
        var now = LocalTime.now();
        return String.format("%d:%d:%d", now.getHour(), now.getMinute(), now.getSecond());
    }
}
