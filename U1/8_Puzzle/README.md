# Búsquedas no informadas: 8-puzzle

Programa de la Unidad 1. Compara búsquedas en anchura, costo uniforme,
profundidad, profundidad limitada, profundidad iterativa y bidireccional.

## Datos usados

- Estado inicial: `7621 3458`
- Estado meta: `12345678 `
- Límite de profundidad: `20`
- Cada movimiento cuesta `1`.

## Complejidad teórica

En la tabla, `b` es el factor de ramificación, `d` la profundidad de la
solución y `m` la profundidad máxima.

| Búsqueda | Tiempo | Espacio | Completa | Óptima |
|---|---:|---:|---|---|
| Anchura (BFS) | O(b^d) | O(b^d) | Sí | Sí, si el costo es uniforme |
| Costo uniforme (UCS) | O(b^(C*/c)) | O(b^(C*/c)) | Sí | Sí |
| Profundidad (DFS) | O(b^m) | O(bm) | No, en general | No |
| Profundidad limitada | O(b^l) | O(bl) | No, si l < d | No |
| Profundidad iterativa (IDS) | O(b^d) | O(bd) | Sí | Sí, si el costo es uniforme |
| Bidireccional | O(b^(d/2)) | O(b^(d/2)) | Sí, con operadores reversibles | Sí, con BFS |

`C*` es el costo de la solución óptima, `c` el costo mínimo de un movimiento y
`l` el límite elegido. La tabla describe el crecimiento teórico; la columna
`Memoria` de la salida del programa mide estructuras distintas según cada
algoritmo, por lo que no debe interpretarse como una medición asintótica
directamente comparable.

## Ejecución

```bash
javac -d bin src/principal/*.java
java -cp bin principal.App
```

El tiempo se mide con `System.nanoTime()` y se muestra en milisegundos con
decimales.
