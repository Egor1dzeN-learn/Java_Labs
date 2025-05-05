package org.example.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

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
