package principal;

/*
 * Inteligencia Artificial
 * Unidad 1 - Árbol binario de búsqueda
 * Alumno: Daniel Jimenez Duarte
 * No. de control: 23170198
 */

public class Nodo {
    private String nombre; // Nombre guardado.
    private Nodo izquierdo; // Hijo izquierdo.
    private Nodo derecho; // Hijo derecho.

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.izquierdo = null;
        this.derecho = null;
    }

    public Nodo(String inicial, Object object) {
        //TODO Auto-generated constructor stub
    }

    public String getNombre() {
        return nombre;
    }

    public Nodo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public Nodo getDerecho() {
        return derecho;
    }

    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }
}
