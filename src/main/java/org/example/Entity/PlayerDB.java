package org.example.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@Table(name = "players")
@NoArgsConstructor
public class PlayerDB {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int countWin = 0;

    public PlayerDB(String name, int countWin) {
        this.countWin = countWin;
        this.name = name;
    }
}
