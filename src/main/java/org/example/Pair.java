package org.example;

import lombok.Data;

@Data
public class Pair<K, V> {
    private final K x;
    private final V y;
}