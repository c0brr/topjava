package ru.javawebinar.topjava.util;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class ResultUtil {

    public static final Logger log = getLogger("result");

    public static final StringBuilder results = new StringBuilder();

    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
        results.setLength(0);
    }

    public static Stopwatch getStopwatch() {
        return new Stopwatch() {
            @Override
            protected void finished(long nanos, Description description) {
                String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
                results.append(result);
                log.info(result + " ms\n");
            }
        };
    }
}
