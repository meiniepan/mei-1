package com.wuyou.user;

import org.junit.Test;

import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        HashMap map =new HashMap();
        map.put(null,"aaaaaaaaaaaaaaaaaaaaaaaa");
        Object o = map.get(null);
        System.out.println(o.toString());
        TreeMap treeMap = new TreeMap();
        assertEquals(4, 2 + 2);
    }
}