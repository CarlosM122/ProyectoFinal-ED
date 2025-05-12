package proyecto.redsocial.utils;

import proyecto.redsocial.model.Sistema;

import java.io.File;

public class Persistencia {

    private static final String RUTA_MODELO_XML = "data/model.xml";



    public static boolean existeArchivoXML() {
        File archivo = new File(RUTA_MODELO_XML);
        return archivo.exists() && archivo.length() > 0;
    }

    public static void guardarRecursosXML(Sistema sistema) {
        try {
            ArchivoUtils.guardarSerializadoXML(RUTA_MODELO_XML,sistema);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Sistema cargarRecursosXML() {
        Sistema sistema = null;

        try {
            sistema = (Sistema)ArchivoUtils.cargarRecursoSerializadoXML(RUTA_MODELO_XML);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sistema;
    }
}
