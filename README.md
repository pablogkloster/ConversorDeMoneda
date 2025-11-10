# 🪙 Conversor de Moneda

## 📖 Descripción del proyecto
**Conversor de Moneda** es una aplicación Java que permite convertir valores entre diferentes divisas en tiempo real utilizando la **API pública [ExchangeRate API](https://www.exchangerate-api.com/)**.  

El objetivo del proyecto es practicar conceptos fundamentales de **POO (Programación Orientada a Objetos)**, consumo de **APIs REST** y manejo de **GSON** para procesar respuestas JSON.  

El sistema mantiene una **lista local de monedas** (gestionada por la clase `GestorMonedas`) y permite agregar nuevas divisas validadas directamente desde la API.

---

## ⚙️ Funcionalidades principales

- ✅ Conversión entre dos monedas a partir de un importe ingresado.  
- 🌐 Conexión en tiempo real con **ExchangeRate API** para obtener tasas actualizadas.  
- 💾 Lista local de monedas (código y nombre) que se puede ampliar.  
- 🔍 Validación automática de códigos de moneda antes de realizar conversiones.  
- ➕ Funcionalidad para añadir nuevas monedas desde los datos de la API.  
- 🧩 Estructura modular y clara:
  - `ClienteHttp` → realiza las solicitudes a la API.  
  - `GestorMonedas` → administra la lista local de monedas.  
  - `MonedaApi`, `Moneda`, `IntercambioMoneda` → representan los datos obtenidos de la API.  
  - `Principal` → maneja el flujo principal e interacción con el usuario.

---

## 🧭 Cómo usar el proyecto

### 1️⃣ Clonar el repositorio

    git clone https://github.com/tu-usuario/conversor-moneda.git
    cd conversor-moneda

### 2️⃣ Configurar entorno

Requiere JDK 17 o superior.

Asegurarse de tener la librería GSON agregada al classpath.
(Ejemplo: gson-2.10.1.jar)

### 3️⃣ Ejecutar el programa

Desde tu IDE o desde la consola:

    javac -cp gson.jar src/com/conversor/*.java
    java -cp ".;gson.jar;src" com.conversor.Principal

### 4️⃣ Interacción con el usuario

Seleccioná una opción del menú (convertir moneda, agregar nueva, salir, etc.).

Ingresá los códigos de moneda según el listado local (ej: USD, ARS, EUR).

Ingresá el importe a convertir.

El programa mostrará el resultado con la tasa de cambio actual.

---

Estructura del proyecto

    conversor-moneda/
    │
    ├── src/
    │   └── com/conversor/
    │       ├── ClienteHttp.java
    │       ├── GestorMonedas.java
    │       ├── IntercambioMoneda.java
    │       ├── Moneda.java
    │       ├── MonedaApi.java
    │       └── Principal.java
    │
    ├── gson-2.10.1.jar
    ├── .gitignore
    └── README.md

---

🎥 Vista previa del proyecto

<img width="381" height="704" alt="image" src="https://github.com/user-attachments/assets/3aac63dd-0783-4c49-9a8a-67b6cd43179b" />

---

🆘 Ayuda y soporte

Si encontrás un error o querés proponer una mejora:

📬 Abrí un issue en el repositorio.

💡 Consultá la documentación oficial de la API:
https://www.exchangerate-api.com/docs

---

👨‍💻 Autor

Pablo Kloster
💬 GitHub: https://github.com/pablogkloster

---

⚖️ Licencia

Este proyecto se distribuye bajo la licencia MIT.
Podés usarlo, modificarlo y compartirlo libremente siempre que mantengas el reconocimiento al autor original.
