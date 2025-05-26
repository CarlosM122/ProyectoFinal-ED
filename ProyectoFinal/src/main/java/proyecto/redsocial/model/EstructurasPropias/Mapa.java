package proyecto.redsocial.model.EstructurasPropias;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Iterator;

@Getter
@Setter
public class Mapa<K, V> implements Iterable<Mapa.Entry<K, V>>, Serializable {
    private static final long serialVersionUID = 1L;

    private ListaEnlazada<Entry<K, V>> entries = new ListaEnlazada<>();

    // ✅ Clase estática, pública y genérica
    public static class Entry<K, V> implements Serializable {
        private static final long serialVersionUID = 1L;
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    public Mapa() {}

    public void put(K key, V value) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
        entries.agregar(new Entry<>(key, value));
    }

    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
        }
    }

    public boolean containsKey(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V remove(K key) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getKey().equals(key)) {
                V value = entries.get(i).getValue();
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

    // ✅ Implementa Iterable para usar for-each
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entries.iterator();
    }
}
