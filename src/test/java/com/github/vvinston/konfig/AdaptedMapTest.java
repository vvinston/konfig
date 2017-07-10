package com.github.vvinston.konfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.function.Function;

@RunWith(MockitoJUnitRunner.class)
public class AdaptedMapTest {
    private final static Function<String, Long> MAPPING = s -> Long.parseLong(s);
    private final static Function<Long, String> RESERVE_MAPPING = l -> String.valueOf(l);

    @Mock
    private Map<String, String> payload;

    private AdaptedMap<String, Long, String> testSubject;

    @Before
    public void setUp() {
        Mockito.reset(payload);
        testSubject = new AdaptedMap<>(payload, MAPPING, RESERVE_MAPPING);
    }

    @Test
    public void testGet() {
        Mockito.when(payload.get("a")).thenReturn("3");

        // when
        final Long result = testSubject.get("a");

        // then
        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test
    public void testPut() {
        Mockito.when(payload.put("a", "3")).thenReturn("3");

        // when
        final Long result = testSubject.put("a", Long.valueOf(3));

        // then
        Assert.assertEquals(Long.valueOf(3), result);
    }

    @Test
    public void testPutAndGet() {
        Mockito.when(payload.put("a", "3")).thenReturn("3");
        Mockito.when(payload.get("a")).thenReturn("3");

        // when
        testSubject.put("a", Long.valueOf(3));
        final Long result = testSubject.get("a");

        // then
        Assert.assertEquals(Long.valueOf(3), result);
    }
}
