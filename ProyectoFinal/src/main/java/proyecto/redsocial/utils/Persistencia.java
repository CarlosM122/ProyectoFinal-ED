package proyecto.redsocial.utils;

import proyecto.redsocial.model.Sistema;

public class Persistencia {

    private static final String RUTA_MODELO_XML = "data/model.xml";

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
