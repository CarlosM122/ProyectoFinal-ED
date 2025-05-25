package proyecto.redsocial.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SistemaTest {

    private Sistema sistema;

    @BeforeEach
    void setUp() {
        sistema= new Sistema();
    }

    @Test
    void guardarEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Ana");

        sistema.guardarEstudiante(estudiante);

        // Verifica que el estudiante tiene ID asignado como 1
        assertEquals(1, estudiante.getId());

        // Verifica que el estudiante fue agregado dos veces
        assertEquals(2, sistema.getListaEstudiantes().size());
        assertSame(sistema.getListaEstudiantes().get(0), estudiante);
        assertSame(sistema.getListaEstudiantes().get(1), estudiante);
    }
}