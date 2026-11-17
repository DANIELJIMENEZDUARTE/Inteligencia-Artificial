package principal;
public class Nodo {
    private String nombre; // Clave que determina la posición del nodo dentro del árbol
    private Nodo izquierdo; // Raíz del subárbol con los nombres menores que este
    private Nodo derecho; // Raíz del subárbol con los nombres mayores que este
 
    public Nodo(String nombre) {
        this.nombre = nombre;
        this.izquierdo = null;
        this.derecho = null;
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
 