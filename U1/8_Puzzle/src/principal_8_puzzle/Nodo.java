package principal_8_puzzle;
/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Daniel Jimenez Duarte
 * No. de control: 23170198
 */


/* Guarda un estado y su ruta de búsqueda. */
public class Nodo {
    private final String estado;  // Estado del tablero.
    private final Nodo padre;     // Nodo anterior.
    private final int profundidad; // Nivel del nodo.
    private final int costo;       // Costo acumulado.

    public Nodo(String estado, Nodo padre) {
        this.estado = estado;
        this.padre = padre;
        this.profundidad = (padre == null) ? 0 : padre.profundidad + 1;
        this.costo = (padre == null) ? 0 : padre.costo + 1;
    }

    public String getEstado() {
        return estado;
    }

    public Nodo getPadre() {
        return padre;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public int getCosto() {
        return costo;
    }
}
