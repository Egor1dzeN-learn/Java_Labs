package org.example.Services;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GsonFactory {
    private static volatile Gson INSTANCE;



    // Публичный метод для получения экземпляра Gson с синхронизацией
    public static Gson getInstance() {
        // Ленивая инициализация с двойной проверкой
        if (INSTANCE == null) {
            synchronized (GsonFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Gson();
                }
            }
        }
        return INSTANCE;
    }
}
