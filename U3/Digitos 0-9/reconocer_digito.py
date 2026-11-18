# Alumno: Daniel Jimenez Duarte
# No. de control: 23170198
#
# Aplicacion que carga el modelo entrenado de MNIST y reconoce en tiempo real
# el digito que se dibuja con el mouse.
#
# Controles:
#   Mouse (clic izquierdo y arrastrar) -> dibujar
#   C                                  -> borrar el dibujo
#   Q o ESC                            -> salir

import os

os.environ.setdefault("TF_CPP_MIN_LOG_LEVEL", "2")  # menos mensajes de TensorFlow

import cv2
import numpy as np
import keras

# ---------------- Configuracion ----------------
RUTA_MODELO = os.path.join(os.path.dirname(os.path.abspath(__file__)), "modelo_mnist.keras")
LADO_LIENZO = 280    # el lienzo es de 280x280 (10 veces el tamano de MNIST)
GROSOR_TRAZO = 20    # grosor del pincel en pixeles
ANCHO_PANEL = 320    # ancho del panel con las probabilidades
ALTO_BARRA = 40      # alto de la barra de instrucciones
# -----------------------------------------------

NOMBRE_VENTANA = "Reconocer digito"


def preparar_imagen(lienzo):
    # Convierte el dibujo a una imagen de 28x28 parecida a las de MNIST.
    # MNIST guarda cada digito dentro de una caja de 20x20 y centrado por su centro de masa,
    # por eso se hace lo mismo aqui para que el modelo reconozca mejor.
    puntos = cv2.findNonZero(lienzo)
    if puntos is None:
        return None  # no hay nada dibujado

    # Recorta justo alrededor del trazo
    x, y, ancho, alto = cv2.boundingRect(puntos)
    recorte = lienzo[y:y + alto, x:x + ancho]

    # Reduce para que el lado mas largo mida 20 pixeles
    escala = 20.0 / max(ancho, alto)
    nuevo_ancho = max(1, int(round(ancho * escala)))
    nuevo_alto = max(1, int(round(alto * escala)))
    recorte = cv2.resize(recorte, (nuevo_ancho, nuevo_alto), interpolation=cv2.INTER_AREA)

    # Lo pega en el centro de una imagen negra de 28x28
    imagen = np.zeros((28, 28), dtype=np.uint8)
    inicio_x = (28 - nuevo_ancho) // 2
    inicio_y = (28 - nuevo_alto) // 2
    imagen[inicio_y:inicio_y + nuevo_alto, inicio_x:inicio_x + nuevo_ancho] = recorte

    # Mueve el dibujo para que su centro de masa quede en el centro de la imagen
    momentos = cv2.moments(imagen)
    if momentos["m00"] > 0:
        centro_x = momentos["m10"] / momentos["m00"]
        centro_y = momentos["m01"] / momentos["m00"]
        desplazamiento = np.float32([[1, 0, 14 - centro_x], [0, 1, 14 - centro_y]])
        imagen = cv2.warpAffine(imagen, desplazamiento, (28, 28))
    return imagen


def predecir(modelo, lienzo):
    # Devuelve las 10 probabilidades (una por digito), o None si el lienzo esta vacio
    imagen = preparar_imagen(lienzo)
    if imagen is None:
        return None
    entrada = imagen.reshape(1, 28, 28).astype("float32")  # el modelo ya normaliza por dentro
    return modelo(entrada, training=False).numpy()[0]


def construir_vista(lienzo, probabilidades):
    # Arma la ventana: lienzo a la izquierda, panel de resultados a la derecha y barra abajo
    alto_total = LADO_LIENZO + ALTO_BARRA
    vista = np.zeros((alto_total, LADO_LIENZO + ANCHO_PANEL, 3), dtype=np.uint8)

    # Lienzo
    vista[:LADO_LIENZO, :LADO_LIENZO] = cv2.cvtColor(lienzo, cv2.COLOR_GRAY2BGR)
    cv2.rectangle(vista, (0, 0), (LADO_LIENZO - 1, LADO_LIENZO - 1), (90, 90, 90), 1)

    # Panel de resultados
    x0 = LADO_LIENZO + 15
    cv2.putText(vista, "Prediccion", (x0, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (200, 200, 200), 1)

    if probabilidades is None:
        cv2.putText(vista, "Dibuja un digito", (x0, 100), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (150, 150, 150), 1)
    else:
        digito = int(np.argmax(probabilidades))
        confianza = float(probabilidades[digito])
        cv2.putText(vista, str(digito), (x0, 105), cv2.FONT_HERSHEY_SIMPLEX, 3.0, (0, 255, 0), 4)
        cv2.putText(vista, f"{confianza:.1%}", (x0 + 90, 100), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 255), 2)

        # Barras con la probabilidad de cada digito
        for i, p in enumerate(probabilidades):
            y = 130 + i * 14
            color = (0, 255, 0) if i == digito else (160, 160, 160)
            cv2.putText(vista, str(i), (x0, y + 9), cv2.FONT_HERSHEY_SIMPLEX, 0.4, color, 1)
            largo = int(p * (ANCHO_PANEL - 100))
            cv2.rectangle(vista, (x0 + 18, y), (x0 + 18 + max(largo, 1), y + 10), color, -1)
            cv2.putText(vista, f"{p:.1%}", (x0 + ANCHO_PANEL - 68, y + 9), cv2.FONT_HERSHEY_SIMPLEX, 0.35, color, 1)

    # Barra de instrucciones
    cv2.putText(vista, "Dibuja con el mouse  |  C: borrar  |  Q o ESC: salir", (8, LADO_LIENZO + 26),
                cv2.FONT_HERSHEY_SIMPLEX, 0.5, (200, 200, 200), 1)
    return vista


def main():
    if not os.path.exists(RUTA_MODELO):
        print("No se encontro el modelo:", RUTA_MODELO)
        print("Entrena primero con el cuaderno y copia modelo_mnist.keras a esta carpeta.")
        return

    modelo = keras.models.load_model(RUTA_MODELO)
    print("Modelo cargado. Dibuja un digito en la ventana.")

    lienzo = np.zeros((LADO_LIENZO, LADO_LIENZO), dtype=np.uint8)
    estado = {"dibujando": False, "ultimo": None, "cambio": False}

    def mouse(evento, x, y, flags, parametros):
        # Dibuja una linea desde el ultimo punto hasta donde esta el mouse
        if evento == cv2.EVENT_LBUTTONDOWN:
            estado["dibujando"] = True
            estado["ultimo"] = (x, y)
            cv2.circle(lienzo, (x, y), GROSOR_TRAZO // 2, 255, -1)
            estado["cambio"] = True
        elif evento == cv2.EVENT_MOUSEMOVE and estado["dibujando"]:
            cv2.line(lienzo, estado["ultimo"], (x, y), 255, GROSOR_TRAZO)
            estado["ultimo"] = (x, y)
            estado["cambio"] = True
        elif evento == cv2.EVENT_LBUTTONUP:
            estado["dibujando"] = False

    cv2.namedWindow(NOMBRE_VENTANA)
    cv2.setMouseCallback(NOMBRE_VENTANA, mouse)

    probabilidades = None
    while True:
        # Solo vuelve a predecir cuando el dibujo cambio
        if estado["cambio"]:
            probabilidades = predecir(modelo, lienzo)
            estado["cambio"] = False

        cv2.imshow(NOMBRE_VENTANA, construir_vista(lienzo, probabilidades))
        tecla = cv2.waitKey(15) & 0xFF

        if tecla in (ord("c"), ord("C")):
            lienzo[:] = 0
            probabilidades = None
        elif tecla in (ord("q"), ord("Q"), 27):
            break
        if cv2.getWindowProperty(NOMBRE_VENTANA, cv2.WND_PROP_VISIBLE) < 1:
            break  # se cerro la ventana con la X

    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()
