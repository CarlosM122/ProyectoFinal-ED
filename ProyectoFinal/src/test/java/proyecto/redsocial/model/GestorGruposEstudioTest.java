package proyecto.redsocial.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proyecto.redsocial.factory.ModelFactory;
import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorGruposEstudioTest {
    private GestorGruposEstudio gestor;
    private Estudiante estudiante;
    private GrupoEstudio grupo;
    private ModelFactory modelFactory;


    @BeforeEach
    void setUp() {
        gestor = new GestorGruposEstudio();
        estudiante = new Estudiante();
        estudiante.setNombre("July");

        grupo = new GrupoEstudio();
        grupo.setTema("Matemáticas");
        grupo.setIdGrupoEstudio("grupo_matematicas");
        grupo.setMiembros(new ListaEnlazada<>());
        grupo.agregarMiembro(estudiante);

        // Agregar grupo a estudiante
        estudiante.getGruposEstudio().agregar(grupo);

        // Agregar grupo al gestor
        ListaEnlazada<GrupoEstudio> grupos = new ListaEnlazada<>();
        grupos.agregar(grupo);
        gestor.agregarGrupos(grupos);
    }

    @Test
    void obtenerGruposDeEstudiante() {
        List<GrupoEstudio> resultado = gestor.obtenerGruposDeEstudiante(estudiante);

        assertEquals(1, resultado.size());
        assertEquals("Matemáticas", resultado.get(0).getTema());
        assertTrue(resultado.get(0).esMiembro(estudiante));
    }
}