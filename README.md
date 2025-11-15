# Club Deportivo Mobile 🏋️‍♂️

Aplicación móvil Android para la gestión de socios, cuotas y pagos de un club deportivo.

## 📋 Descripción

Esta aplicación permite administrar las operaciones de un club deportivo, incluyendo:
- Gestión de socios y no socios
- Control de cuotas y pagos
- Visualización de cuotas vencidas
- Carnet digital de socio
- Sistema de autenticación con roles (Admin/Socio)

## 🛠️ Tecnologías

- **Lenguaje**: Kotlin 2.0.21
- **SDK Mínimo**: Android 8.0 (API 26)
- **SDK Target**: Android 15 (API 35)
- **Base de datos**: Room 2.8.2
- **UI**: Material Design 3
- **Arquitectura**: MVVM con DAO pattern

## 📦 Dependencias Principales

```gradle
- Room Database (ORM)
- Material Design Components
- AndroidX Core KTX
- Kotlin Parcelize
- KSP (Kotlin Symbol Processing)
```

## ⚙️ Requisitos Previos

Antes de ejecutar el proyecto en local, asegúrate de tener instalado:

1. **Android Studio** (versión Hedgehog o superior recomendada)
   - Descarga desde: https://developer.android.com/studio

2. **JDK 11 o superior**
   - El proyecto está configurado para Java 11

3. **SDK de Android**
   - API Level 26 (mínimo)
   - API Level 35 (recomendado para testing)

4. **Dispositivo de prueba**
   - Emulador Android con API 26+
   - O dispositivo físico con USB debugging habilitado

## 🚀 Configuración e Instalación

### 1. Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd club-deportivo-mobile
```

### 2. Configurar el Proyecto

#### Archivo `local.properties`
Crea o verifica que exista el archivo `local.properties` en la raíz del proyecto con la ruta de tu Android SDK:

```properties
sdk.dir=C\:\\Users\\TU_USUARIO\\AppData\\Local\\Android\\Sdk
```

Para Windows PowerShell, también puedes usar:
```properties
sdk.dir=C:/Users/TU_USUARIO/AppData/Local/Android/Sdk
```

#### Verificar `gradle.properties`
El archivo `gradle.properties` ya debe estar configurado con las siguientes propiedades:

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
android.enableJetifier=true
```

### 3. Sincronizar Dependencias

En Android Studio:
1. Abre el proyecto
2. Espera a que Gradle sincronice automáticamente
3. O manualmente: `File > Sync Project with Gradle Files`

Desde terminal:
```bash
./gradlew build
```

En Windows PowerShell:
```powershell
.\gradlew.bat build
```

### 4. Ejecutar la Aplicación

#### Desde Android Studio:
1. Conecta un dispositivo físico o inicia un emulador
2. Haz clic en el botón "Run" (▶️) o presiona `Shift + F10`

#### Desde Terminal:
```bash
./gradlew installDebug
```

## 🗄️ Base de Datos

La aplicación utiliza **Room Database** con las siguientes entidades:

### Entidades

- **User**: Usuarios del sistema con autenticación
- **Socio**: Miembros del club con información completa
- **NoSocio**: Visitantes o usuarios temporales
- **Cuota**: Registro de pagos y cuotas mensuales

### Migraciones

La base de datos se crea automáticamente en la primera ejecución. La versión actual es la **versión 4**.

⚠️ **IMPORTANTE**: Si modificas el esquema de la base de datos:
1. Incrementa el número de versión en `AppDatabase.kt`
2. Implementa una estrategia de migración o usa `.fallbackToDestructiveMigration()`
3. Los datos existentes se perderán si no implementas migraciones

### Inspeccionar la Base de Datos

#### Opción 1: Database Inspector (Android Studio)
1. Ejecuta la app en un emulador o dispositivo con API 26+
2. Ve a `View > Tool Windows > App Inspection`
3. Selecciona la pestaña "Database Inspector"
4. Explora las tablas: `users`, `socios`, `cuotas`, `no_socios`

#### Opción 2: ADB (Android Debug Bridge)
```bash
# Listar bases de datos
adb shell run-as com.aislados.clubdeportivo ls /data/data/com.aislados.clubdeportivo/databases/

# Exportar la base de datos
adb exec-out run-as com.aislados.clubdeportivo cat databases/club_deportivo_database > club_deportivo.db
```

Luego puedes abrir el archivo `.db` con herramientas como:
- DB Browser for SQLite
- DBeaver
- DataGrip

## 🎨 Estructura del Proyecto

```
app/src/main/java/com/aislados/clubdeportivo/
├── database/
│   ├── AppDatabase.kt          # Configuración de Room
│   ├── Converters.kt            # Convertidores de tipos
│   ├── UserDAO.kt               # DAO de usuarios
│   ├── SocioDAO.kt              # DAO de socios
│   ├── CuotaDAO.kt              # DAO de cuotas
│   └── NoSocioDAO.kt            # DAO de no socios
├── model/
│   ├── User.kt                  # Entidad Usuario
│   ├── Socio.kt                 # Entidad Socio
│   ├── Cuota.kt                 # Entidad Cuota
│   ├── NoSocio.kt               # Entidad No Socio
│   ├── UserRole.kt              # Enum de roles
│   └── LocalDateParceler.kt     # Parceler para LocalDate
├── extensions/
│   └── IntentExtensions.kt      # Extensiones de Intent
├── LoginActivity.kt             # Pantalla de login
├── RegistroActivity.kt          # Registro de usuarios
├── MenuPrincipal.kt             # Menú principal
├── AltaSocioActivity.kt         # Alta de socios
├── AltaNoSocioActivity.kt       # Alta de no socios
├── CobroActivity.kt             # Gestión de cobros
├── CarnetActivity.kt            # Carnet digital
├── CuotasVencidasActivity.kt    # Lista de cuotas vencidas
└── CuotasAdapter.kt             # Adapter para RecyclerView
```

## 🔑 Credenciales por Defecto

### Usuario Administrador
- **Usuario**: `admin`
- **Contraseña**: `admin`
- **Rol**: ADMIN

⚠️ **Nota**: El usuario admin se crea automáticamente en la primera ejecución si no existe.

## 🐛 Problemas Comunes y Soluciones

### 1. Error de Sincronización de Gradle
```
Solution: File > Invalidate Caches / Restart
```

### 2. Error de KSP
```
Solution: ./gradlew clean build --refresh-dependencies
```

### 3. Error de Parcelize
Verifica que el plugin esté en `app/build.gradle.kts`:
```kotlin
plugins {
    id("kotlin-parcelize")
}
```

### 4. Error con LocalDate en Parcelable
El proyecto incluye `LocalDateParceler.kt` para serializar `LocalDate`. Asegúrate de que esté presente.

### 5. Base de datos no actualizada en App Inspection
- Realiza una acción que modifique la BD
- Cierra y reabre App Inspection
- Verifica que estés en modo Debug
- El dispositivo debe tener API 26+

### 6. Error al pasar objetos por Intent
Los modelos `Socio`, `User`, `UserRole` y `NoSocio` implementan `Parcelable` con `@Parcelize`.

## 📱 Funcionalidades Principales

### Para Administradores
- ✅ Dar de alta socios y no socios
- ✅ Registrar pagos de cuotas
- ✅ Ver listado de cuotas vencidas
- ✅ Gestionar carnets digitales

### Para Socios
- ✅ Ver carnet digital con estado de cuenta
- ✅ Consultar información personal
- ✅ Ver estado de su suscripción (Activo/En mora)

## 📄 Licencia

Este proyecto es parte de un trabajo académico/institucional.

## 👥 Contribución

Para contribuir al proyecto:
1. Haz fork del repositorio
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📞 Soporte

Si encuentras problemas o tienes preguntas:
1. Revisa la sección de "Problemas Comunes"
2. Verifica los logs en Android Studio (Logcat)
3. Crea un issue en el repositorio con detalles del error

---

**Última actualización**: Noviembre 2025  
**Versión**: 1.0.0