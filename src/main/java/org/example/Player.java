package org.example;

import lombok.Data;
import lombok.ToString;

import java.awt.*;
import java.util.Objects;

@Data
@ToString
public class Player {
    private String name;
    private int x;
    private int y = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
