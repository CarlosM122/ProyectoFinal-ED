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
        estudiante.setNombre("Karol");

        sistema.guardarEstudiante(estudiante);

        // Verifica que el estudiante tiene ID asignado como 1
        assertEquals(1, estudiante.getId());

        // Verifica que el estudiante fue agregado dos veces
        assertEquals(2, sistema.getListaEstudiantes().size());
        assertSame(sistema.getListaEstudiantes().get(0), estudiante);
        assertSame(sistema.getListaEstudiantes().get(1), estudiante);
    }

    @Test
    void buscarModerador() {
        Moderador mod1 = new Moderador();
        mod1.setCorreo("mod1@correo.com");

        Moderador mod2 = new Moderador();
        mod2.setCorreo("mod2@correo.com");

        sistema.getListaModeradores().agregar(mod1);
        sistema.getListaModeradores().agregar(mod2);

        Moderador resultado = sistema.buscarModerador("mod2@correo.com");

        assertNotNull(resultado);
        assertEquals("mod2@correo.com", resultado.getCorreo());
        assertSame(mod2, resultado);

        // Verifica que si el correo no existe, se retorna null
        Moderador inexistente = sistema.buscarModerador("noexiste@correo.com");
        assertNull(inexistente);
    }
}