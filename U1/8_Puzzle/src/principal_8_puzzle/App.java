package principal_8_puzzle;
/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Daniel Jimenez Duarte
 * No. de control: 23170198
 */

import java.util.ArrayList;
import java.util.List;

/* Ejecuta las búsquedas y muestra sus resultados. */
public class App {
    // El espacio representa el hueco.
    private static final String ESTADO_INICIAL = "7621 3458";
    private static final String ESTADO_META = "12345678 ";
    private static final int LIMITE_PROFUNDIDAD = 20;

    public static void main(String[] args) {
        if (!Puzzle.esValido(ESTADO_INICIAL) || !Puzzle.esValido(ESTADO_META)) {
            System.out.println("Estado inválido: usa las fichas 1-8 y un espacio, sin repetir.");
            return;
        }
        if (!Puzzle.esResoluble(ESTADO_INICIAL, ESTADO_META)) {
            System.out.println("Ese estado inicial no tiene solución hacia el objetivo dado.");
            return;
        }

        System.out.println("Estado inicial:");
        System.out.print(Puzzle.formato(ESTADO_INICIAL));
        System.out.println("Estado objetivo:");
        System.out.print(Puzzle.formato(ESTADO_META));
        System.out.println();

        Busqueda busqueda = new Busqueda(ESTADO_INICIAL, ESTADO_META);
        List<Resultado> resultados = new ArrayList<>();
        resultados.add(busqueda.anchura());
        resultados.add(busqueda.costoUniforme());
        resultados.add(busqueda.profundidad());
        resultados.add(busqueda.profundidadLimitada(LIMITE_PROFUNDIDAD));
        resultados.add(busqueda.profundidadIterativa());
        resultados.add(busqueda.bidireccional());

        for (Resultado r : resultados) {
            r.imprimir();
        }
        imprimirTabla(resultados);
    }

    // Imprime los resultados.
    private static void imprimirTabla(List<Resultado> resultados) {
        System.out.println("================== TABLA RESUMEN ==================");
        System.out.printf("%-38s %-6s %-6s %-11s %-11s %-11s %-10s%n",
                "Búsqueda", "Sol.", "Costo", "Expandidos", "Generados", "Memoria", "ms");
        for (Resultado r : resultados) {
            System.out.printf("%-38s %-6s %-6s %-11d %-11d %-11d %-10.3f%n",
                    r.algoritmo,
                    r.encontrada ? "sí" : "no",
                    r.encontrada ? String.valueOf(r.pasos()) : "-",
                    r.expandidos, r.generados, r.maxFrontera, r.milisegundos);
        }
    }
}
