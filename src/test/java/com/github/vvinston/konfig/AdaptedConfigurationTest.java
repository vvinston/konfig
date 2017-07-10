package com.github.vvinston.konfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RunWith(MockitoJUnitRunner.class)
public class AdaptedConfigurationTest {

    @Mock
    private Function<String, String> readAdapter;

    @Mock
    private Function<String, Map<String, String>> findAdapter;

    private AdaptedConfiguration testSubject;

    @Before
    public void setUp() {
        Mockito.reset(readAdapter, findAdapter);
        testSubject = new AdaptedConfiguration(readAdapter, findAdapter);
    }

    @Test
    public void testReadFromAdaptedWhenExists() {
        // given
        Mockito.when(readAdapter.apply("a")).thenReturn("b");

        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("b", result.get().asString());
    }

    @Test
    public void testReadFromAdaptedWhenNotSet() {
        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testFindFromAdapted() {
        // given
        Mockito.when(findAdapter.apply("a")).thenReturn(givenFindResults());

        // when
        final Map<String, ConfigValue> result = testSubject.find("a");

        // then
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("a1", result.get("a.a").asString());
        Assert.assertEquals("a2", result.get("a.b").asString());
        Assert.assertEquals("a3", result.get("a.c").asString());
    }

    private Map<String, String> givenFindResults() {
        final Map<String, String> result = new HashMap<>();
        result.put("a.a", "a1");
        result.put("a.b", "a2");
        result.put("a.c", "a3");
        return result;
    }
}
