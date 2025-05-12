package proyecto.redsocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Moderador {
    private String nombre;
    private String correo;
    private String contrasenia; // ← Añadir este campo

    public void gestionarUsuarios() {
        // Implementar lógica
    }

    public void gestionarContenido() {
        // Implementar lógica
    }

    public void generarReporteConexiones() {
        // Implementar lógica
    }

    public void visualizarGrafoAfinidad() {
        // Implementar lógica
    }
}


