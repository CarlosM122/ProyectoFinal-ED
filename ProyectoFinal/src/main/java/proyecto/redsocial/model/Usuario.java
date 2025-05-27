package proyecto.redsocial.model;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nombre;
    protected String correo;
    protected String contrasenia;
    protected String rutaArchivoImagen;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    public String getRutaArchivoImagen() {
        return rutaArchivoImagen;
    }
    public void setRutaArchivoImagen(String rutaArchivoImagen) {
        this.rutaArchivoImagen = rutaArchivoImagen;
    }
}
