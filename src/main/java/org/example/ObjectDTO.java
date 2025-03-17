package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.plaf.synth.ColorType;
import java.awt.*;

@Data
@NoArgsConstructor
public class ObjectDTO {
    private TypeObjectDTO type;
//    private Color color;
    private String name;
    private String data;

    public ObjectDTO(TypeObjectDTO type, String name, String data) {
        this.data = data;
        this.name = name;
        this.type = type;
    }
}
