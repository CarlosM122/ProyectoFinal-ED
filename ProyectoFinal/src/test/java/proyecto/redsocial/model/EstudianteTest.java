package proyecto.redsocial.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteTest {

    @Test
    void agregarInteres() {
        Estudiante estudiante = new Estudiante();

        estudiante.agregarInteres("Matemáticas");
        estudiante.agregarInteres("Música");
        estudiante.agregarInteres("Matemáticas");

        // Verifica los intereses recorriendo la lista
        List<String> intereses = new ArrayList<>();
        for (String interes : estudiante.getIntereses()) {
            intereses.add(interes);
        }

        assertEquals(2, intereses.size());
        assertTrue(intereses.contains("Matemáticas"));
        assertTrue(intereses.contains("Música"));
    }
}