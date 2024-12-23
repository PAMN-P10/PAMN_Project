# README para el Proyecto "Let’s Cook!" 🧑🏻‍🍳

## Descripción

**Let’s Cook!** es una aplicación móvil diseñada para entusiastas de la cocina, que ofrece una plataforma para explorar, gestionar y compartir recetas de manera dinámica y personalizada. Desarrollada en Kotlin utilizando Android Studio, la aplicación sigue la arquitectura **MVVM**, lo que facilita la escalabilidad y el mantenimiento.

## Características principales

1. **Interfaz intuitiva y moderna**:
   - Diseño adaptable para modo claro y oscuro.
   - Uso de **Jetpack Compose** para una construcción de interfaces declarativa y eficiente.
2. **Gestión completa de recetas**:
   - Búsqueda avanzada por ingredientes, categorías y dificultad.
   - Creación y edición de recetas con subida de imágenes.
   - Funcionalidad de favoritos.
3. **Soporte para personalización**:
   - Edición de perfiles de usuario con fotos personalizadas.
4. **Integración de tecnologías modernas**:
   - Reconocimiento de voz para búsquedas rápidas.
   - Firebase como backend para autenticación y almacenamiento de datos.
   - Corrutinas de Kotlin para operaciones asíncronas.
5. **Compatibilidad**:
   - Adaptación dinámica a diferentes configuraciones de dispositivos.


## Paleta de colores

### Colores principales utilizados (Modo Claro)
![Negro](https://img.shields.io/badge/Negro-0E0A01?style=for-the-badge&logo=appveyor&logoColor=white)
![Blanco](https://img.shields.io/badge/Blanco-FEFDF6?style=for-the-badge&logo=appveyor&logoColor=black)
![Amarillo](https://img.shields.io/badge/Amarillo-ECCA22?style=for-the-badge&logo=appveyor&logoColor=black)
![Verde Pastel](https://img.shields.io/badge/Verde%20Pastel-C4F486?style=for-the-badge&logo=appveyor&logoColor=black)
![Verde Intenso](https://img.shields.io/badge/Verde%20Intenso-7FEF43?style=for-the-badge&logo=appveyor&logoColor=black)
![Beige](https://img.shields.io/badge/Beige-F0E8BD?style=for-the-badge&logo=appveyor&logoColor=black)

### Colores principales utilizados (Modo Oscuro)
![Blanco](https://img.shields.io/badge/Blanco-F0E8BD?style=for-the-badge&logo=appveyor&logoColor=black)
![Negro](https://img.shields.io/badge/Negro-090801?style=for-the-badge&logo=appveyor&logoColor=white)
![Amarillo](https://img.shields.io/badge/Amarillo-DDBB13?style=for-the-badge&logo=appveyor&logoColor=black)
![Verde Oscuro](https://img.shields.io/badge/Verde%20Oscuro-4A790B?style=for-the-badge&logo=appveyor&logoColor=black)
![Verde Intenso](https://img.shields.io/badge/Verde%20Cantoso-4CBC10?style=for-the-badge&logo=appveyor&logoColor=black)
![Marrón Oscuro](https://img.shields.io/badge/Marrón%20Oscuro-433B0F?style=for-the-badge&logo=appveyor&logoColor=white)


## Estructura del Proyecto

```
letscook/
├── navigation/
│   └── NavigationWrapper.kt          # Controla la navegación entre pantallas.
├── data/
│   ├── local/                        # Base de datos local o caché.
│   └── repositories/                 # Gestión de datos en Firebase.
├── domain/
│   ├── models/                       # Clases de dominio (e.g., Recipe, Ingredient).
│   └── useCases/                     # Lógica de negocio encapsulada en casos de uso.
├── presentation/
│   ├── components/                   # Componentes visuales reutilizables.
│   ├── screens/                      # Pantallas específicas de la app.
│   └── viewmodel/                    # Gestión del estado de la aplicación.
├── services/
│   ├── AuthService                   # Servicios de autenticación.
│   └── RecipeService                 # Operaciones relacionadas con recetas.
├── ui/
│   ├── font/                         # Tipografías.
│   ├── theme/                        # Definición de colores y temas.
├── MainActivity.kt                   # Punto de entrada de la app.
```

## Instalación y configuración

1. **Requisitos previos**:
   - Android Studio instalado.
   - Firebase configurado con autenticación y Firestore habilitados.

2. **Clonación del repositorio**:
   ```bash
   git clone https://github.com/PAMN-P10/PAMN_Project
   cd PAMN_Project
   ```

3. **Configuración de Firebase**:
   - Descarga el archivo `google-services.json` desde tu consola de Firebase y colócalo en el directorio `app/`.

4. **Ejecutar la aplicación**:
   - Abre el proyecto en Android Studio y sincroniza las dependencias.
   - Conecta un dispositivo físico o usa un emulador para ejecutar la app.

## Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **Frameworks**:
  - Jetpack Compose
  - Firebase (Authentication y Firestore)
- **Herramientas**:
  - Android Studio
  - Figma para diseño de UI
  - Realtime Colors para la selección de paleta
  - Xrecorder para creación de la demo

## Recursos adicionales

- **[Repositorio en GitHub](https://github.com/PAMN-P10/PAMN_Project)**
- **[Diseño en Figma](https://www.figma.com/design/htmsFqPuD6mSZTQv9klp2N/PAMN?node-id=0-1&t=jG7enLGYzNC7UqTh-1)**
- **[Vídeo de la demo](https://drive.google.com/drive/folders/1mtAmlIBz2bBTKAVWASfgpWeDxwIuwf3k?usp=sharing)**

## Autores

- **[Sara Expósito Suárez](https://github.com/SaraE5)**
- **[Asmae Ez Zaim Driouch](https://github.com/A-rar)**

