package proyecto.redsocial.model.EstructurasPropias;

import proyecto.redsocial.model.SolicitudAyuda;

public class ColaPrioridadSolicitudes {
    private NodoSolicitud cabeza;

    public void insertar (SolicitudAyuda solicitudAyuda){
        NodoSolicitud nuevo = new NodoSolicitud(solicitudAyuda);
        if (cabeza==null||nuevo.getSolicitud().getUrgencia()>cabeza.getSolicitud().getUrgencia()){
            nuevo.setSiguiente(cabeza);
            cabeza=nuevo;
        }else {
            NodoSolicitud actual = cabeza;
            while (actual.getSiguiente()!=null&&actual.getSiguiente().getSolicitud().getUrgencia()>nuevo.getSolicitud().getUrgencia()){
                actual=actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
    }

    public SolicitudAyuda desencolar(){
        if (cabeza==null) return null;
        SolicitudAyuda solicitud = cabeza.getSolicitud();
        cabeza=cabeza.getSiguiente();
        return solicitud;
    }

    public boolean estaVacia(){
        return cabeza == null;
    }
}
