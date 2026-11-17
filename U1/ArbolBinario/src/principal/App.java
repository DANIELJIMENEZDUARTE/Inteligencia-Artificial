package principal;
/*
    Inteligencia Artificial
    Unidad 1 - Árbol binario de búsqueda
    Horario: 6:00 - 7:00 p.m.
    Alumno: Daniel Jimenez Duarte
    No. de control: 23170198
*/

public class App {
    public static void main(String[] args) throws Exception {
        Arbol arbol = new Arbol();

        System.out.println("Árbol recién creado");
        System.out.println("¿Está vacío? " + arbol.vacio());

        // Inserta los nombres.
        String[] nombres = { "Daniel", "Miriam", "Angel", "Luigi", "German", "Evelyn" };
        for (String nombre : nombres) {
            arbol.insertar(nombre);
        }

        System.out.println();
        System.out.println("Después de insertar " + nombres.length + " nombres");
        System.out.println("¿Está vacío? " + arbol.vacio());
        System.out.println("Nodo raíz: " + arbol.getRaiz().getNombre());

        // Muestra los nombres ordenados.
        System.out.print("Recorrido en orden: ");
        enOrden(arbol.getRaiz());
        System.out.println();

        // Prueba nombres existentes y uno inexistente.
        System.out.println();
        System.out.println("Pruebas de buscarNodo");
        buscar(arbol, "German");
        buscar(arbol, "Evelyn");
        buscar(arbol, "Luis");
    }

    private static void buscar(Arbol arbol, String nombre) {
        Nodo encontrado = arbol.buscarNodo(nombre);
        if (encontrado == null) {
            System.out.println("  " + nombre + " no está en el árbol");
        } else {
            System.out.println("  " + nombre + " encontrado, hijos: izquierdo "
                    + textoHijo(encontrado.getIzquierdo()) + ", derecho "
                    + textoHijo(encontrado.getDerecho()));
        }
    }

    // Muestra un hijo o un guion.
    private static String textoHijo(Nodo hijo) {
        if (hijo == null) {
            return "-";
        }
        return hijo.getNombre();
    }

    // Recorre izquierda, nodo y derecha.
    private static void enOrden(Nodo actual) {
        if (actual == null) {
            return;
        }
        enOrden(actual.getIzquierdo());
        System.out.print(actual.getNombre() + " ");
        enOrden(actual.getDerecho());
    }
}
