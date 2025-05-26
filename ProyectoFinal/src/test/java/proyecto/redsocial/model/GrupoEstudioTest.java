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

    @Test
    void esMiembro() {
        Estudiante estudiante1 = new Estudiante();
        estudiante1.setNombre("Carlos");

        Estudiante estudiante2 = new Estudiante();
        estudiante2.setNombre("Ana");

        // Agregar solo estudiante1
        grupo.agregarMiembro(estudiante1);

        // Verificar que estudiante1 es miembro
        assertTrue(grupo.esMiembro(estudiante1));

        // Verificar que estudiante2 no es miembro
        assertFalse(grupo.esMiembro(estudiante2));
    }
}