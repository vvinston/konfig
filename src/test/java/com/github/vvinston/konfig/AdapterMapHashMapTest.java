package com.github.vvinston.konfig;

import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class AdapterMapHashMapTest {
    private final static Function<String, Long> MAPPING = s -> Long.parseLong(s);
    private final static Function<Long, String> RESERVE_MAPPING = l -> String.valueOf(l);

    private Map<String, String> payload;

    private AdaptedMap<String, Long, String> testSubject;

    @Before
    public void setUp() {
        payload = new HashMap<>();
        testSubject = new AdaptedMap<>(payload, MAPPING, RESERVE_MAPPING);
    }

    @Test
    public void testGet() {
        // given
        givenPayloadIsFilledWithSingleElement();

        // when
        final Long result = testSubject.get("a");

        // then
        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test
    public void testPutAndSize() {
        // given
        givenPayloadIsEmpty();

        // then
        Assert.assertEquals(0, testSubject.size());
        Assert.assertEquals(0, payload.size());

        // when
        final Long result = testSubject.put("a", Long.valueOf(3));

        // then
        Assert.assertEquals(Long.valueOf(3), result);
        Assert.assertEquals(1, testSubject.size());
        Assert.assertEquals(1, payload.size());
    }

    @Test
    public void testPutAndGet() {
        // given
        givenPayloadIsFilledWithSingleElement();

        // when
        testSubject.put("a", Long.valueOf(3));
        final Long result = testSubject.get("a");

        // then
        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test
    public void testClearAndIsEmpty() {
        // given
        givenPayloadIsFilledWithSingleElement();

        // then
        Assert.assertFalse(payload.isEmpty());
        Assert.assertFalse(testSubject.isEmpty());

        // when
        testSubject.clear();

        // then
        Assert.assertTrue(payload.isEmpty());
        Assert.assertTrue(testSubject.isEmpty());
    }

    @Test
    public void testContainsKey() {
        // given
        givenPayloadIsFilledWithSingleElement();

        // when
        final boolean result1 = testSubject.containsKey("a");
        final boolean result2 = testSubject.containsKey("b");
        final boolean result3 = testSubject.containsKey(Integer.valueOf(55));


        // then
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        Assert.assertFalse(result3);
    }

    @Test
    public void testContainsValue() {
        // given
        givenPayloadIsFilledWithSingleElement();

        // when
        final boolean result1 = testSubject.containsValue(Long.valueOf(3));
        final boolean result2 = testSubject.containsValue("3");
        final boolean result3 = testSubject.containsValue(Long.valueOf(6));
        final boolean result4 = testSubject.containsValue(Boolean.FALSE);

        // then
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        Assert.assertFalse(result3);
        Assert.assertFalse(result4);
    }

    @Test
    public void testEntrySet() {
        // given
        givenPayloadIsFilledWithMultipleElements();

        // when
        final Set<Map.Entry<String, Long>> result = testSubject.entrySet();

        // then
        Assert.assertEquals(3, result.size());
        assertContainsEntry(result, "b", 6L);
        assertContainsEntry(result, "c", 9L);
        assertContainsEntry(result, "d", 12L);
    }

    @Test
    public void testKeySet() {
        // given
        givenPayloadIsFilledWithMultipleElements();

        // when
        final Set<String> result = testSubject.keySet();

        // then
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains("b"));
        Assert.assertTrue(result.contains("c"));
        Assert.assertTrue(result.contains("d"));
    }

    @Test
    public void testRemoveAndSize() {
        // given
        givenPayloadIsFilledWithMultipleElements();

        // then
        Assert.assertEquals(3, testSubject.size());
        Assert.assertEquals(3, payload.size());

        // when
        final Long result = testSubject.remove("c");

        // then
        Assert.assertEquals(Long.valueOf(9), result);
        Assert.assertEquals(2, testSubject.size());
        Assert.assertEquals(2, payload.size());
        Assert.assertFalse(payload.containsKey("c"));
        Assert.assertFalse(payload.containsValue("9"));
    }

    @Test
    public void testPutAll() {
        // given
        givenPayloadIsFilledWithSingleElement();
        final Map<String, Long> furtherItems = new HashMap<>();
        furtherItems.put("b", 6L);
        furtherItems.put("c", 9L);
        furtherItems.put("d", 12L);

        // when
        testSubject.putAll(furtherItems);

        // then
        Assert.assertEquals(4, testSubject.size());
        Assert.assertEquals(4, payload.size());
        Assert.assertTrue(payload.containsKey("a"));
        Assert.assertEquals(Long.valueOf(3L), testSubject.get("a"));
        Assert.assertEquals("3", payload.get("a"));
        Assert.assertTrue(payload.containsKey("b"));
        Assert.assertEquals(Long.valueOf(6L), testSubject.get("b"));
        Assert.assertEquals("6", payload.get("b"));
        Assert.assertTrue(payload.containsKey("c"));
        Assert.assertEquals(Long.valueOf(9L), testSubject.get("c"));
        Assert.assertEquals("9", payload.get("c"));
        Assert.assertTrue(payload.containsKey("d"));
        Assert.assertEquals(Long.valueOf(12L), testSubject.get("d"));
        Assert.assertEquals("12", payload.get("d"));
    }

    public void test() {
        // given
        givenPayloadIsFilledWithMultipleElements();

        // when
        final Collection<Long> result = testSubject.values();

        // then
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(6L));
        Assert.assertTrue(result.contains(9L));
        Assert.assertTrue(result.contains(12L));
    }

    private void givenPayloadIsFilledWithSingleElement() {
        payload.put("a", "3");
    }

    private void givenPayloadIsFilledWithMultipleElements() {
        payload.put("b", "6");
        payload.put("c", "9");
        payload.put("d", "12");
    }

    private void givenPayloadIsEmpty() {}

    private void assertContainsEntry(Set<Map.Entry<String, Long>> entries, String key, Long value) {
        for (final Map.Entry<String, Long> entry : entries) {
            if (key.equals(entry.getKey()) && value.equals(entry.getValue())) {
                return;
            }
        }

        throw new AssertionFailedError("Entry set with key {" + key + "} and value {" + value + "} not found!");
    }
}
