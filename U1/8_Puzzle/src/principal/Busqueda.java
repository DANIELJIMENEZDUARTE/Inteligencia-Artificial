package principal;
/*
 * Inteligencia Artificial
 * Unidad 1 - Búsquedas no informadas aplicadas al 8-puzzle
 *
 * Alumno: Jiménez Duarte Daniel
 * No. de control: 23170198
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

/*
 * Las búsquedas no informadas. Todas siguen la misma idea: tener una frontera
 * (nodos por visitar), sacar un nodo, comprobar si es la meta y, si no, meter sus hijos.
 * Lo único que cambia entre ellas es el orden en que la frontera entrega los nodos:
 *
 *   Anchura            -> cola (FIFO): primero el más antiguo
 *   Profundidad        -> pila (LIFO): primero el más reciente
 *   Costo uniforme     -> cola de prioridad: primero el de menor costo
 *   Limitada/Iterativa -> profundidad con un tope de niveles
 */
public class Busqueda {
    private final String inicial;
    private final String meta;

    public Busqueda(String inicial, String meta) {
        this.inicial = inicial;
        this.meta = meta;
    }

    // ---------------------------------------------------------------- Anchura
    public Resultado anchura() {
        Resultado r = nuevo("Primero en anchura (BFS)");
        long t0 = System.nanoTime();

        Deque<Nodo> frontera = new ArrayDeque<>();
        Set<String> visitados = new HashSet<>();
        Nodo raiz = new Nodo(inicial, null);
        frontera.add(raiz);
        visitados.add(inicial);
        r.generados = 1;
        r.maxFrontera = 1;

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.poll();
            if (actual.getEstado().equals(meta)) {
                return cerrar(r, actual, t0);
            }
            r.expandidos++;
            for (Nodo hijo : Puzzle.hijos(actual)) {
                if (visitados.add(hijo.getEstado())) { // add devuelve false si ya estaba
                    frontera.add(hijo);
                    r.generados++;
                }
            }
            r.maxFrontera = Math.max(r.maxFrontera, frontera.size());
        }
        return cerrar(r, null, t0);
    }

    // ---------------------------------------------------------- Costo uniforme
    public Resultado costoUniforme() {
        Resultado r = nuevo("Costo uniforme (UCS)");
        long t0 = System.nanoTime();

        PriorityQueue<Nodo> frontera = new PriorityQueue<>(Comparator.comparingInt(Nodo::getCosto));
        Map<String, Integer> mejorCosto = new HashMap<>();
        Nodo raiz = new Nodo(inicial, null);
        frontera.add(raiz);
        mejorCosto.put(inicial, 0);
        r.generados = 1;
        r.maxFrontera = 1;

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.poll();
            // La meta se comprueba al SACAR el nodo (no al generarlo): así se garantiza el camino más barato
            if (actual.getEstado().equals(meta)) {
                return cerrar(r, actual, t0);
            }
            // Si ya se encontró un camino más barato a este estado, esta entrada quedó vieja
            if (actual.getCosto() > mejorCosto.get(actual.getEstado())) {
                continue;
            }
            r.expandidos++;
            for (Nodo hijo : Puzzle.hijos(actual)) {
                Integer previo = mejorCosto.get(hijo.getEstado());
                if (previo == null || hijo.getCosto() < previo) {
                    mejorCosto.put(hijo.getEstado(), hijo.getCosto());
                    frontera.add(hijo);
                    r.generados++;
                }
            }
            r.maxFrontera = Math.max(r.maxFrontera, frontera.size());
        }
        return cerrar(r, null, t0);
    }

    // ------------------------------------------------------------- Profundidad
    public Resultado profundidad() {
        Resultado r = nuevo("Primero en profundidad (DFS)");
        long t0 = System.nanoTime();

        Deque<Nodo> frontera = new ArrayDeque<>(); // se usa como pila: push / pop
        Set<String> visitados = new HashSet<>();
        Nodo raiz = new Nodo(inicial, null);
        frontera.push(raiz);
        visitados.add(inicial);
        r.generados = 1;
        r.maxFrontera = 1;

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.pop();
            if (actual.getEstado().equals(meta)) {
                return cerrar(r, actual, t0);
            }
            r.expandidos++;
            for (Nodo hijo : Puzzle.hijos(actual)) {
                if (visitados.add(hijo.getEstado())) {
                    frontera.push(hijo);
                    r.generados++;
                }
            }
            r.maxFrontera = Math.max(r.maxFrontera, frontera.size());
        }
        return cerrar(r, null, t0);
    }

    // ---------------------------------------------------- Profundidad limitada
    public Resultado profundidadLimitada(int limite) {
        Resultado r = nuevo("Profundidad limitada (límite " + limite + ")");
        long t0 = System.nanoTime();

        Contadores c = new Contadores();
        Nodo meta = limitada(new Nodo(inicial, null), limite, new HashMap<>(), c);

        r.expandidos = c.expandidos;
        r.generados = c.generados;
        r.maxFrontera = c.maxNiveles;
        return cerrar(r, meta, t0);
    }

    // ---------------------------------------------------- Profundidad iterativa
    public Resultado profundidadIterativa() {
        Resultado r = nuevo("Profundidad iterativa (IDS)");
        long t0 = System.nanoTime();

        Contadores c = new Contadores();
        Nodo encontrado = null;
        // Repite la búsqueda limitada con límite 0, 1, 2... hasta hallar la meta.
        // El 8-puzzle se resuelve como máximo en 31 movimientos.
        for (int limite = 0; limite <= 31 && encontrado == null; limite++) {
            encontrado = limitada(new Nodo(inicial, null), limite, new HashMap<>(), c);
        }
        r.expandidos = c.expandidos;
        r.generados = c.generados;
        r.maxFrontera = c.maxNiveles;
        return cerrar(r, encontrado, t0);
    }

    /*
     * Núcleo recursivo de la búsqueda limitada.
     * 'vistos' recuerda a qué profundidad se llegó a cada estado: solo se vuelve a explorar
     * un estado si ahora se llega a él con menos profundidad (más margen restante). Esto evita
     * ciclos sin perder soluciones dentro del límite.
     */
    private Nodo limitada(Nodo actual, int limite, Map<String, Integer> vistos, Contadores c) {
        c.maxNiveles = Math.max(c.maxNiveles, actual.getProfundidad() + 1);
        if (actual.getEstado().equals(meta)) {
            return actual;
        }
        if (actual.getProfundidad() >= limite) {
            return null; // corte por límite
        }
        Integer previo = vistos.get(actual.getEstado());
        if (previo != null && previo <= actual.getProfundidad()) {
            return null; // ya se exploró desde un nivel igual o más alto
        }
        vistos.put(actual.getEstado(), actual.getProfundidad());
        c.expandidos++;

        for (Nodo hijo : Puzzle.hijos(actual)) {
            c.generados++;
            Nodo resultado = limitada(hijo, limite, vistos, c);
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }

    // ------------------------------------------------------------ Bidireccional
    /*
     * Dos búsquedas en anchura: una desde el estado inicial y otra desde el objetivo.
     * Se detiene cuando alguna genera un estado que la otra ya había visitado.
     * Funciona porque cada movimiento se puede deshacer (los operadores son reversibles).
     */
    public Resultado bidireccional() {
        Resultado r = nuevo("Bidireccional (opcional)");
        long t0 = System.nanoTime();

        if (inicial.equals(meta)) {
            r.encontrada = true;
            r.ruta = new ArrayList<>(List.of(inicial));
            r.generados = 1;
            r.milisegundos = (System.nanoTime() - t0) / 1_000_000;
            return r;
        }

        Map<String, Nodo> desdeInicio = new HashMap<>();
        Map<String, Nodo> desdeMeta = new HashMap<>();
        Deque<Nodo> colaInicio = new ArrayDeque<>();
        Deque<Nodo> colaMeta = new ArrayDeque<>();

        Nodo raizI = new Nodo(inicial, null);
        Nodo raizM = new Nodo(meta, null);
        desdeInicio.put(inicial, raizI);
        desdeMeta.put(meta, raizM);
        colaInicio.add(raizI);
        colaMeta.add(raizM);
        r.generados = 2;

        while (!colaInicio.isEmpty() && !colaMeta.isEmpty()) {
            // Se expande un nivel completo de la frontera más pequeña
            boolean avanzaInicio = colaInicio.size() <= colaMeta.size();
            Deque<Nodo> cola = avanzaInicio ? colaInicio : colaMeta;
            Map<String, Nodo> propios = avanzaInicio ? desdeInicio : desdeMeta;
            Map<String, Nodo> otros = avanzaInicio ? desdeMeta : desdeInicio;

            int tamanoNivel = cola.size();
            for (int i = 0; i < tamanoNivel; i++) {
                Nodo actual = cola.poll();
                r.expandidos++;
                for (Nodo hijo : Puzzle.hijos(actual)) {
                    if (propios.containsKey(hijo.getEstado())) {
                        continue;
                    }
                    propios.put(hijo.getEstado(), hijo);
                    cola.add(hijo);
                    r.generados++;

                    Nodo encuentro = otros.get(hijo.getEstado());
                    if (encuentro != null) {
                        Nodo delInicio = avanzaInicio ? hijo : encuentro;
                        Nodo delaMeta = avanzaInicio ? encuentro : hijo;
                        r.encontrada = true;
                        r.ruta = unirRutas(delInicio, delaMeta);
                        r.milisegundos = (System.nanoTime() - t0) / 1_000_000;
                        return r;
                    }
                }
            }
            r.maxFrontera = Math.max(r.maxFrontera, desdeInicio.size() + desdeMeta.size());
        }
        r.milisegundos = (System.nanoTime() - t0) / 1_000_000;
        return r;
    }

    // Une la mitad del inicio con la mitad de la meta (ambos nodos tienen el mismo estado)
    private List<String> unirRutas(Nodo delInicio, Nodo delaMeta) {
        List<String> ruta = rutaDesdeRaiz(delInicio);
        // De la mitad de la meta se recorre hacia arriba: ya va en dirección a la meta
        for (Nodo n = delaMeta.getPadre(); n != null; n = n.getPadre()) {
            ruta.add(n.getEstado());
        }
        return ruta;
    }

    // ---------------------------------------------------------------- Auxiliares
    private Resultado nuevo(String nombre) {
        Resultado r = new Resultado();
        r.algoritmo = nombre;
        return r;
    }

    private Resultado cerrar(Resultado r, Nodo nodoMeta, long t0) {
        r.milisegundos = (System.nanoTime() - t0) / 1_000_000;
        if (nodoMeta != null) {
            r.encontrada = true;
            r.ruta = rutaDesdeRaiz(nodoMeta);
        }
        return r;
    }

    // Sigue la cadena de padres desde la meta hasta la raíz y la invierte
    private List<String> rutaDesdeRaiz(Nodo nodo) {
        List<String> ruta = new ArrayList<>();
        for (Nodo n = nodo; n != null; n = n.getPadre()) {
            ruta.add(n.getEstado());
        }
        Collections.reverse(ruta);
        return ruta;
    }

    // Contadores compartidos entre las llamadas recursivas
    private static class Contadores {
        long expandidos;
        long generados;
        long maxNiveles;
    }
}
