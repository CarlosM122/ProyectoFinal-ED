package proyecto.redsocial.model;

import proyecto.redsocial.model.EstructurasPropias.ListaEnlazada;
import proyecto.redsocial.utils.Persistencia;

public class TestSerializacion {
    public static void main(String[] args) throws Exception {
        Sistema sistema = new Sistema();
        Estudiante e1 = new Estudiante();
        e1.setNombre("Pedro");
        e1.setCorreo("pedro123");
        e1.setId(23);
        sistema.getListaEstudiantes().agregar(e1);

        Persistencia.guardarRecursosXML(sistema);


        Sistema sistemaCargado = Persistencia.cargarRecursosXML();
        ListaEnlazada<Estudiante> estudiantes = sistemaCargado.getListaEstudiantes();
        estudiantes.imprimir();
    }
}
