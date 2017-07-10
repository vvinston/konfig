package com.github.vvinston.konfig;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final class AdaptedMap<K, V, T> implements Map<K, V> {

    private final Map<K, T> map;
    private final Function<T, V> mapping;
    private final Function<V, T> reverseMapping;

    public AdaptedMap(Map<K, T> map, Function<T, V> mapping, Function<V, T> reverseMapping) {
        this.map = map;
        this.mapping = mapping;
        this.reverseMapping = reverseMapping;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        try {
            // TODO
            return map.containsValue(reverseMapping.apply((V) value));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public V get(Object key) {
        return mapping.apply(map.get(key));
    }

    @Override
    public V put(K key, V value) {
        map.put(key, reverseMapping.apply(value));
        return value;
    }

    @Override
    public V remove(Object key) {
        return mapping.apply(map.remove(key));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(new AdaptedMap(m, reverseMapping, mapping));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values().stream().map(mapping).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet().stream().map(e -> new AdaptedEntry(e) ).collect(Collectors.toSet());
    }

    private class AdaptedEntry implements Entry<K, V> {

        private final Entry<K, T> entry;

        private AdaptedEntry(final Entry<K, T> entry) {
            this.entry = entry;
        }

        @Override
        public K getKey() {
            return entry.getKey();
        }

        @Override
        public V getValue() {
            return mapping.apply(entry.getValue());
        }

        @Override
        public V setValue(V value) {
            entry.setValue(reverseMapping.apply(value));
            return value;
        }
    }
}
