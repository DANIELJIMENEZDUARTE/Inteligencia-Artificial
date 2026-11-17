package principal;
/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Jiménez Duarte Daniel
 * No. de control: 23170198
 */

/*
 * Nodo del árbol de búsqueda.
 * Guarda un estado del tablero y los datos necesarios para reconstruir la ruta
 * de solución: quién es su padre, a qué nivel está y cuánto costó llegar a él.
 */
public class Nodo {
    private final String estado;  // Tablero de 9 caracteres leído por renglones; el espacio es el hueco
    private final Nodo padre;     // Nodo del que se generó. null en la raíz
    private final int profundidad; // Número de movimientos desde el estado inicial
    private final int costo;       // Costo acumulado. Cada movimiento cuesta 1

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
