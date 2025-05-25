package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class Mapa <K,V> implements Serializable {
    private static final long serialVersionUID = 1L;

    private ListaEnlazada<Entry<K,V>> entries = new ListaEnlazada<>();

    private class Entry<K,V> implements Serializable{
        private static final long serialVersionUID = 1L;
        private K key;
        private V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    public Mapa() {
    }

    public void put(K key, V value) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        entries.agregar(new Entry<>(key, value));
    }

    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V remove(K key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).key.equals(key)) {
                V value = entries.get(i).value;
                entries.eliminar(i);
                return value;
            }
        }
        return null;
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.estaVacia();
    }


}
