package proyecto.redsocial.utils;

import proyecto.redsocial.model.Sistema;

import java.io.*;

public class Persistencia {

    private static final String RUTA_ARCHIVO_BINARIO = "src/main/resources/persistencia/sistema.dat";

    public static void guardarRecursosXML(Sistema sistema) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO_BINARIO));
            oos.writeObject(sistema);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Sistema cargarRecursosXML() {
        Sistema sistema = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RUTA_ARCHIVO_BINARIO));
            sistema = (Sistema) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sistema;
    }

    public static boolean existeArchivoXML() {
        return new File(RUTA_ARCHIVO_BINARIO).exists();
    }
}