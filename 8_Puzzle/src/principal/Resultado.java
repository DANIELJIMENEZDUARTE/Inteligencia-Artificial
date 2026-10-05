package principal;

/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Jiménez Duarte Daniel
 * No. de control: 23170198
 */

import java.util.List;

/*
 * Resultado de una búsqueda: la ruta encontrada y sus datos estadísticos.
 * Son los datos que se comparan en la tabla comparativa del entregable.
 */
public class Resultado {
    private static final int MAX_PASOS_A_IMPRIMIR = 40;

    String algoritmo;
    boolean encontrada;
    List<String> ruta;      // Estados desde el inicial hasta el objetivo
    long expandidos;        // Nodos a los que se les generaron hijos
    long generados;         // Nodos creados en total
    long maxFrontera;       // Máximo de nodos guardados a la vez (memoria usada)
    long milisegundos;      // Tiempo de ejecución

    public int pasos() {
        return ruta == null ? 0 : ruta.size() - 1;
    }

    public void imprimir() {
        System.out.println("==================================================");
        System.out.println("Búsqueda: " + algoritmo);
        System.out.println("==================================================");
        if (!encontrada) {
            System.out.println("No se encontró solución.");
        } else if (pasos() <= MAX_PASOS_A_IMPRIMIR) {
            for (int i = 0; i < ruta.size(); i++) {
                System.out.println("Paso " + i + ":");
                System.out.print(Puzzle.formato(ruta.get(i)));
            }
        } else {
            System.out.println("La ruta tiene " + pasos() + " movimientos (demasiado larga para imprimirla).");
        }
        System.out.println("--- Datos estadísticos ---");
        System.out.println("Solución encontrada : " + (encontrada ? "sí" : "no"));
        if (encontrada) {
            System.out.println("Profundidad / costo : " + pasos());
        }
        System.out.println("Nodos expandidos    : " + expandidos);
        System.out.println("Nodos generados     : " + generados);
        System.out.println("Máx. nodos en memoria: " + maxFrontera);
        System.out.println("Tiempo              : " + milisegundos + " ms");
        System.out.println();
    }
}
