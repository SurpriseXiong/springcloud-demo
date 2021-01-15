package com.spring.college.myspringcollege.test;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 芒草<xiongwen>
 * @DATE: 2021/1/15 4:14 下午
 */
public class tetsx {
    private static final String STATUS_DELIMITER = "_";

    @Test
    public void main() {
        Map<String, AtomicInteger> instanceCountMap = new TreeMap<String, AtomicInteger>();

        for (int i = 0; i < 12; i++) {
            AtomicInteger instanceCount = instanceCountMap.computeIfAbsent(i + "name",
                    k -> new AtomicInteger(0));
            instanceCount.incrementAndGet();
        }

        String reconcileHashCode = getReconcileHashCode(instanceCountMap);
        System.out.println();


    }


    public static String getReconcileHashCode(Map<String, AtomicInteger> instanceCountMap) {
        StringBuilder reconcileHashCode = new StringBuilder(75);
        for (Map.Entry<String, AtomicInteger> mapEntry : instanceCountMap.entrySet()) {
            reconcileHashCode.append(mapEntry.getKey()).append(STATUS_DELIMITER).append(mapEntry.getValue().get())
                    .append(STATUS_DELIMITER);
        }
        return reconcileHashCode.toString();
    }
}
