# Tarea 7. Clasificación de dígitos 0-9 (MNIST)

Materia: Inteligencia Artificial
Alumno: Daniel Jimenez Duarte
No. de control: 23170198

## ¿De qué trata?

Entrenamos una red neuronal con el dataset **MNIST** (dígitos escritos a mano) para que reconozca los números del 0 al 9.
Guardamos el modelo ya entrenado en un archivo y lo cargamos en una pequeña aplicación en Python donde se dibuja un
número con el mouse y el programa dice cuál es, en tiempo real.

Lo que pedía la tarea:

1. Usar el dataset MNIST.
2. Entrenar y guardar el modelo ya entrenado.
3. En una aplicación en Python, cargar el modelo y probarlo en tiempo real.

## Archivos de esta carpeta

| Archivo | Para qué sirve |
|---|---|
| `entrenar_modelo_mnist.ipynb` | Cuaderno donde entrenamos y guardamos el modelo |
| `reconocer_digito.py` | Aplicación que carga el modelo y reconoce lo que dibujamos |
| `modelo_mnist.keras` | Modelo ya entrenado (se genera con el cuaderno) |
| `requirements.txt` | Librerías que necesita la aplicación |

## Cómo usarlo

### 1. Entrenar el modelo

1. Abrimos `entrenar_modelo_mnist.ipynb` en Google Colab (Archivo > Subir cuaderno).
2. Entorno de ejecución > Ejecutar todo. Tarda menos de un minuto.
3. Al final se descarga solo el archivo `modelo_mnist.keras`. Lo copiamos a esta misma carpeta, junto a
   `reconocer_digito.py`.

### 2. Probarlo en tiempo real

Instalamos las librerías y corremos la aplicación:

```bash
pip install -r requirements.txt
python reconocer_digito.py
```

Se abre una ventana con un lienzo negro. Se dibuja un número con el mouse (clic izquierdo y arrastrar) y a la
derecha aparece el dígito que predice el modelo, con su porcentaje de confianza y la probabilidad de cada número.

| Control | Acción |
|---|---|
| Mouse (clic izquierdo y arrastrar) | Dibujar |
| C | Borrar el dibujo |
| Q o ESC | Salir |

Esta aplicación abre una ventana, así que se corre en una computadora normal y no en Codespaces ni en Colab.
TensorFlow necesita una versión de Python compatible (en general de la 3.10 a la 3.13). Se revisa con
`python --version`.

## El modelo

Es una red neuronal densa (perceptrón multicapa):

| Capa | Qué hace |
|---|---|
| Rescaling | Pasa los píxeles de 0-255 a 0-1 |
| Flatten | Convierte la imagen de 28 x 28 en 784 números |
| Dense 256 (ReLU) | Capa oculta |
| Dropout 0.2 | Apaga el 20 % de las neuronas al entrenar para evitar el sobreajuste |
| Dense 128 (ReLU) | Capa oculta |
| Dense 10 (softmax) | Una probabilidad por cada dígito |

Se entrenó 10 épocas con el optimizador Adam y lotes de 128, y alcanza alrededor del 98 % de precisión en las 10,000
imágenes de prueba. El cuaderno muestra las gráficas, la matriz de confusión y algunos errores del modelo.

## Cómo prepara el dibujo la aplicación

Un dibujo hecho con el mouse no se parece a una imagen de MNIST: es más grande, está en otro lugar del lienzo y el
trazo es más grueso. Por eso `reconocer_digito.py` hace lo mismo que se hizo con los datos de MNIST antes de
predecir: recorta alrededor del trazo, lo reduce para que mida 20 píxeles de largo, lo centra en una imagen de
28 x 28 y lo mueve para que su centro de masa quede en el medio. Sin ese paso el modelo falla mucho más.
