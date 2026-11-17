package principal;

/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Daniel Jimenez Duarte
 * No. de control: 23170198
 */

import java.util.List;

/* Guarda la ruta y las estadísticas de una búsqueda. */
public class Resultado {
    private static final int MAX_PASOS_A_IMPRIMIR = 40;

    String algoritmo;
    boolean encontrada;
    List<String> ruta;      // Estados desde el inicial hasta el objetivo
    long expandidos;        // Nodos a los que se les generaron hijos
    long generados;         // Nodos creados en total
    long maxFrontera;       // Máximo de nodos guardados a la vez (memoria usada)
    double milisegundos;    // Tiempo de ejecución

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
        System.out.printf("Tiempo              : %.3f ms%n", milisegundos);
        System.out.println();
    }
}
