# Tarea 6. Dataset propio de rostros

Materia: Inteligencia Artificial
Alumno: Daniel Jimenez Duarte
No. de control: 23170198

Equipo:

- Miriam Alejandra Bazaldua Machado
- Daniel Jimenez Duarte
- José de Jesús Quintero Noris
- Rodrigo Villegas Valdez

## ¿De qué trata?

Para esta tarea armamos un dataset de rostros con 100 fotos de cada integrante del equipo y también con fotos de
famosos. Para hacerlo escribimos dos programas en Python:

- `capturaRostros.py`: con este nos tomamos las fotos nosotros usando la cámara de la computadora.
- `recortarRostrosFamosos.py`: con este recortamos la cara de las fotos de los famosos que descargamos.

Los dos usan OpenCV para encontrar el rostro y guardan solo la cara, no la foto completa.

## Archivos de esta carpeta

| Archivo | Para qué sirve |
|---|---|
| `capturaRostros.py` | Toma fotos con la cámara y guarda el recorte del rostro |
| `recortarRostrosFamosos.py` | Recorta el rostro de fotos que ya tenemos descargadas |
| `fotos.zip` | Las imágenes del dataset, comprimidas |

## Qué se necesita para correrlos

Python 3 y OpenCV:

```bash
pip install opencv-python
```

El segundo programa también usa `tkinter` para abrir la ventana donde se escogen las fotos. En Windows y Mac ya
viene con Python. En Linux hay que instalarlo con `sudo apt install python3-tk`.

Como los dos abren ventanas (y el primero usa la cámara) hay que correrlos en una computadora normal. En Codespaces
o en Google Colab no funcionan.

## Cómo detecta los rostros

Usamos un clasificador de OpenCV llamado Haar Cascade (`haarcascade_frontalface_default.xml`). Ya viene con la
librería, así que no hubo que descargar nada. Funciona bien cuando la persona está de frente y con buena luz, pero
le cuesta más cuando la cara está muy de lado o hay poca luz.

## Programa 1: `capturaRostros.py`

Abre la cámara, busca la cara en cada imagen y guarda el recorte automáticamente. Solo guarda cuando ve
exactamente un rostro, para que no se cuelen fondos ni otras personas.

Para usarlo:

1. Abrimos el archivo y cambiamos `CARPETA_SALIDA` por el nombre de la persona, por ejemplo `"Daniel_Jimenez"`.
   Así las fotos de cada quien quedan en su propia carpeta.
2. Lo corremos con `python capturaRostros.py`.
3. Cuando se abre la ventana de la cámara, apretamos la barra espaciadora para empezar a capturar.
4. Mientras toma las fotos movemos un poco la cabeza y cambiamos de gesto, para que no salgan todas iguales.
5. Se detiene solo al llegar a 100 fotos. También se puede salir con la tecla Q o con ESC.

Las fotos se guardan como `rostro_001.jpg`, `rostro_002.jpg`, y así. Si la carpeta ya tenía fotos, sigue la
numeración desde donde se quedó.

Lo que se puede cambiar al inicio del archivo:

| Variable | Valor | Qué hace |
|---|---|---|
| `CARPETA_SALIDA` | `"fotos_rostro"` | Carpeta donde se guardan las fotos |
| `TOTAL_FOTOS` | `100` | Cuántas fotos toma |
| `INTERVALO` | `0.3` | Segundos que espera entre una foto y otra |
| `INDICE_CAMARA` | `0` | Qué cámara usa (si no abre, probar con 1) |
| `MARGEN` | `0.0` | Espacio extra alrededor de la cara (0.1 sería 10 %) |
| `TAMANO_SALIDA` | `None` | Tamaño final de la foto; con `None` se queda como salió |

## Programa 2: `recortarRostrosFamosos.py`

Este sirve para las fotos de los famosos, que ya teníamos descargadas. Recorta la cara de cada una.

Para usarlo:

1. Lo corremos con `python recortarRostrosFamosos.py`.
2. Se abre el explorador de archivos y escogemos las fotos de un famoso. Se pueden seleccionar varias a la vez con
   Ctrl o Shift (acepta `.jpg`, `.jpeg`, `.png` y `.webp`).
3. En la terminal escribimos el nombre de la carpeta donde queremos guardar los recortes, por ejemplo
   `dataset_proyecto/famoso_1`. Si no existe, el programa la crea.
4. Al terminar nos dice cuántos rostros recortó y cuántas fotos descartó porque no encontró ninguna cara.

Esto lo repetimos con cada famoso.

Los recortes salen cuadrados de 250 × 250 píxeles y con un 10 % de espacio alrededor de la cara. También se guardan
como `rostro_001.jpg`, `rostro_002.jpg`, etc.

## Consejos que nos sirvieron

- Tomar las fotos con buena luz y con la cara completa y de frente.
- Cambiar de ángulo, de gesto y de iluminación. Como el programa toma una foto cada 0.3 segundos, si uno se queda
  quieto salen todas casi iguales.
- Al terminar, revisar las carpetas y borrar los recortes que no sean una cara.
