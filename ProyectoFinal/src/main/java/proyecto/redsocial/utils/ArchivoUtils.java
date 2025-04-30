package proyecto.redsocial.utils;

import proyecto.redsocial.model.Sistema;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArchivoUtils {

    public static void guardarSerializadoXML(String rutaModeloXml, Sistema sistema) throws IOException {
        XMLEncoder encoder;

        encoder = new XMLEncoder(new FileOutputStream(rutaModeloXml));
        encoder.writeObject(sistema);
        encoder.flush();
        encoder.close();
    }

    public static Object cargarRecursoSerializadoXML(String rutaModeloXml) throws FileNotFoundException {
        XMLDecoder decodificadorXML;
        Object objetoXML;

        decodificadorXML = new XMLDecoder(new FileInputStream(rutaModeloXml));
        objetoXML = decodificadorXML.readObject();
        decodificadorXML.close();
        return objetoXML;
    }
}
