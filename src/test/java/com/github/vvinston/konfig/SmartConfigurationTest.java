package com.github.vvinston.konfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@RunWith(MockitoJUnitRunner.class)
public class SmartConfigurationTest {

    private final static Predicate<ConfigValue> PREDICATE = configValue -> configValue.asString().startsWith("$");
    private final static Function<ConfigValue, String> KEY_FACTORY = configValue -> configValue.asString().substring(1);

    @Mock
    private Configuration configuration;


    private SmartConfiguration testSubject;

    @Before
    public void setUp() {
        Mockito.reset(configuration);
        testSubject = new SmartConfiguration(configuration, PREDICATE, KEY_FACTORY);
    }

    @Test
    public void simpleReadExisting() {
        // given
        BDDMockito.given(configuration.read("a")).willReturn(Optional.of(new StaticConfigValue("x")));

        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("x", result.get().asString());
    }

    @Test
    public void simpleReadMissing() {
        // given
        BDDMockito.given(configuration.read(Mockito.anyString())).willReturn(Optional.empty());

        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void simpleReadReferencingExisting() {
        // given
        BDDMockito.given(configuration.read("a")).willReturn(Optional.of(new StaticConfigValue("$b")));
        BDDMockito.given(configuration.read("b")).willReturn(Optional.of(new StaticConfigValue("x")));

        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("x", result.get().asString());
    }

    @Test
    public void multipleReadLinkedReferencingExisting() {
        // given
        BDDMockito.given(configuration.read("a")).willReturn(Optional.of(new StaticConfigValue("$b")));
        BDDMockito.given(configuration.read("b")).willReturn(Optional.of(new StaticConfigValue("$c")));
        BDDMockito.given(configuration.read("c")).willReturn(Optional.of(new StaticConfigValue("$d")));
        BDDMockito.given(configuration.read("d")).willReturn(Optional.of(new StaticConfigValue("x")));

        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals("x", result.get().asString());
    }

    @Test
    public void simpleReadReferencingMissing() {
        // given
        BDDMockito.given(configuration.read("a")).willReturn(Optional.of(new StaticConfigValue("$b")));
        BDDMockito.given(configuration.read(Mockito.anyString())).willReturn(Optional.empty());

        // when
        final Optional<ConfigValue> result = testSubject.read("a");

        // then
        Assert.assertFalse(result.isPresent());
    }

    @Test(expected = IllegalStateException.class)
    public void selfReadReferencingThrowsException() {
        // given
        BDDMockito.given(configuration.read("a")).willReturn(Optional.of(new StaticConfigValue("$a")));

        // when
        testSubject.read("a");
    }


    @Test(expected = IllegalStateException.class)
    public void circularReadReferenceThrowsException() {
        // given
        BDDMockito.given(configuration.read("a")).willReturn(Optional.of(new StaticConfigValue("$b")));
        BDDMockito.given(configuration.read("b")).willReturn(Optional.of(new StaticConfigValue("$a")));

        // when
        testSubject.read("a");
    }

    @Test
    public void simpleFindReturnsPayloadResult() {
        // given
        BDDMockito.given(configuration.find("a")).willReturn(givenSimpleFindResult());

        // when
        final Map<String, ConfigValue> result = testSubject.find("a");

        // then
        Assert.assertEquals(givenSimpleFindResult(), result);
        final InOrder inOrder = BDDMockito.inOrder(configuration);
        inOrder.verify(configuration, BDDMockito.times(1)).find("a");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void simpleReferencedReturnsResolved() {
        // given
        BDDMockito.given(configuration.read("b")).willReturn(Optional.of(new StaticConfigValue("x")));
        BDDMockito.given(configuration.find("a")).willReturn(givenSimpleReferencedFindResult());

        // when
        final Map<String, ConfigValue> result = testSubject.find("a");

        // then
        Assert.assertEquals(givenSimpleReferencedExpectations(), result);
        final InOrder inOrder = BDDMockito.inOrder(configuration);
        inOrder.verify(configuration, BDDMockito.times(1)).find("a");
        inOrder.verify(configuration, BDDMockito.times(1)).read("b");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void crossReferencedReturnsResolved() {
        // given
        BDDMockito.given(configuration.read("a.c")).willReturn(Optional.of(new StaticConfigValue("x")));
        BDDMockito.given(configuration.find("a")).willReturn(givenCrossReferencedFindResult());

        // when
        final Map<String, ConfigValue> result = testSubject.find("a");

        // then
        Assert.assertEquals(givenCrossReferencedExpecatation(), result);
        final InOrder inOrder = BDDMockito.inOrder(configuration);
        inOrder.verify(configuration, BDDMockito.times(1)).find("a");
        inOrder.verify(configuration, BDDMockito.times(1)).read("a.c");
        inOrder.verifyNoMoreInteractions();
    }

    private Map<String, ConfigValue> givenSimpleFindResult() {
        final Map<String, ConfigValue> result = new HashMap<>();
        result.put("a.a", new StaticConfigValue("a"));
        result.put("a.b", new StaticConfigValue("b"));
        return result;
    }

    private Map<String, ConfigValue> givenSimpleReferencedFindResult() {
        final Map<String, ConfigValue> result = new HashMap<>();
        result.put("a.a", new StaticConfigValue("a"));
        result.put("a.b", new StaticConfigValue("$b"));
        return result;
    }

    private Map<String, ConfigValue> givenCrossReferencedFindResult() {
        final Map<String, ConfigValue> result = new HashMap<>();
        result.put("a.a", new StaticConfigValue("a"));
        result.put("a.b", new StaticConfigValue("$a.c"));
        result.put("a.c", new StaticConfigValue("x"));
        return result;
    }

    private Map<String, ConfigValue> givenSimpleReferencedExpectations() {
        final Map<String, ConfigValue> result = new HashMap<>();
        result.put("a.a", new StaticConfigValue("a"));
        result.put("a.b", new StaticConfigValue("x"));
        return result;
    }

    private Map<String, ConfigValue> givenCrossReferencedExpecatation() {
        final Map<String, ConfigValue> result = new HashMap<>();
        result.put("a.a", new StaticConfigValue("a"));
        result.put("a.b", new StaticConfigValue("x"));
        result.put("a.c", new StaticConfigValue("x"));
        return result;
    }
}
