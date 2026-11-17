package principal;

/*
 * Inteligencia Artificial
 * Unidad 1 - Árbol binario de búsqueda
 * Alumno: Daniel Jimenez Duarte
 * No. de control: 23170198
 */

public class Arbol {

    private Nodo raiz; // Raíz del árbol.

    public Arbol() {
        this.raiz = null;
    }

    public boolean vacio() {
        return raiz == null;
    }

    // Busca un nombre en el árbol.
    public Nodo buscarNodo(String nombre) {
        return buscar(raiz, nombre);
    }

    // Compara y continúa por el subárbol correspondiente.
    private Nodo buscar(Nodo actual, String nombre) {
        if (actual == null) {
            return null;
        }
        int comparacion = nombre.compareTo(actual.getNombre());
        if (comparacion == 0) {
            return actual;
        }
        if (comparacion < 0) {
            return buscar(actual.getIzquierdo(), nombre);
        }
        return buscar(actual.getDerecho(), nombre);
    }

    public void insertar(String nombre) {
        raiz = insertar(raiz, nombre);
    }

    // Inserta el nombre en su posición ordenada.
    private Nodo insertar(Nodo actual, String nombre) {
        if (actual == null) {
            return new Nodo(nombre);
        }
        int comparacion = nombre.compareTo(actual.getNombre());
        if (comparacion < 0) {
            actual.setIzquierdo(insertar(actual.getIzquierdo(), nombre));
        } else if (comparacion > 0) {
            actual.setDerecho(insertar(actual.getDerecho(), nombre));
        }
        return actual;
    }

    public Nodo getRaiz() {
        return raiz;
    }

}
