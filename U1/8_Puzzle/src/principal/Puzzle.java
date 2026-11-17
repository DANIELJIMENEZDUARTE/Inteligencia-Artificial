package principal;
/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Daniel Jimenez Duarte
 * No. de control: 23170198
 */

import java.util.ArrayList;
import java.util.List;

/* Contiene las reglas del 8-puzzle. */
public class Puzzle {

    // Valida un estado.
    public static boolean esValido(String estado) {
        if (estado == null || estado.length() != 9) {
            return false;
        }
        String esperado = "12345678 ";
        for (char c : esperado.toCharArray()) {
            if (estado.indexOf(c) < 0 || estado.indexOf(c) != estado.lastIndexOf(c)) {
                return false;
            }
        }
        return true;
    }

    // Comprueba la paridad de inversiones.
    public static boolean esResoluble(String inicial, String meta) {
        return inversiones(inicial) % 2 == inversiones(meta) % 2;
    }

    private static int inversiones(String estado) {
        int total = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                char a = estado.charAt(i);
                char b = estado.charAt(j);
                if (a != ' ' && b != ' ' && a > b) {
                    total++;
                }
            }
        }
        return total;
    }

    // Genera movimientos posibles.
    public static List<Nodo> hijos(Nodo padre) {
        List<Nodo> lista = new ArrayList<>();
        String estado = padre.getEstado();
        int hueco = estado.indexOf(' ');
        int fila = hueco / 3;
        int columna = hueco % 3;

        if (fila > 0) {
            lista.add(new Nodo(intercambiar(estado, hueco, hueco - 3), padre)); // arriba
        }
        if (fila < 2) {
            lista.add(new Nodo(intercambiar(estado, hueco, hueco + 3), padre)); // abajo
        }
        if (columna > 0) {
            lista.add(new Nodo(intercambiar(estado, hueco, hueco - 1), padre)); // izquierda
        }
        if (columna < 2) {
            lista.add(new Nodo(intercambiar(estado, hueco, hueco + 1), padre)); // derecha
        }
        return lista;
    }

    private static String intercambiar(String estado, int a, int b) {
        char[] c = estado.toCharArray();
        char temp = c[a];
        c[a] = c[b];
        c[b] = temp;
        return new String(c);
    }

    // Formatea el tablero.
    public static String formato(String estado) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i += 3) {
            sb.append(" ");
            for (int j = i; j < i + 3; j++) {
                char c = estado.charAt(j);
                sb.append(c == ' ' ? '_' : c).append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
