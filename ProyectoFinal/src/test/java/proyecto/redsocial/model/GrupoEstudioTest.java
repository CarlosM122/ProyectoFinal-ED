package proyecto.redsocial.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GrupoEstudioTest {

    private GrupoEstudio grupo;

    @BeforeEach
    void setUp() {
        grupo = new GrupoEstudio();
        try {
            Field miembrosField = GrupoEstudio.class.getDeclaredField("miembros");
            miembrosField.setAccessible(true);
            miembrosField.set(grupo, new ListaEnlazada<>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Error al inicializar el campo 'miembros': " + e.getMessage());
        }
    }

    @Test
    void agregarMiembro() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("July");

        grupo.agregarMiembro(estudiante);

        // Verifica que el miembro fue agregado correctamente
        assertEquals(1, grupo.getMiembros().size());
        assertSame(estudiante, grupo.getMiembros().get(0));
    }
}