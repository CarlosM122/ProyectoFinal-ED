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

    @Test
    void tieneInteresComun() {
        Estudiante estudiante1 = new Estudiante();
        estudiante1.agregarInteres("Matemáticas");
        estudiante1.agregarInteres("Música");

        Estudiante estudiante2 = new Estudiante();
        estudiante2.agregarInteres("Historia");
        estudiante2.agregarInteres("Música");

        Estudiante estudiante3 = new Estudiante();
        estudiante3.agregarInteres("Física");
        estudiante3.agregarInteres("Química");

        // Debe retornar true porque ambos comparten "Música"
        assertTrue(estudiante1.tieneInteresComun(estudiante2));

        // Debe retornar false porque no comparten intereses
        assertFalse(estudiante1.tieneInteresComun(estudiante3));
    }
}