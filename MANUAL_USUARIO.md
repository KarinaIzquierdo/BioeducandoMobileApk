# Manual de Usuario — Bioeducando con Oswal (App Móvil Android)

**Versión:** 1.0
**Última actualización:** Septiembre 2026
**Plataforma:** Android (API 33+)

---

## 1. Introducción

**Bioeducando con Oswal** es una aplicación educativa ambiental que permite a la comunidad educativa interactuar con contenidos sobre medio ambiente: retos ecológicos, proyectos STEAM, proyectos PRAE, noticias ambientales y una comunidad donde publicar y comentar.

La app se conecta al servidor Bioeducando para sincronizar toda la información con la versión web: lo que se publica en la app se ve en la web y viceversa.

---

## 2. Requisitos

- Dispositivo Android o emulador (Android 13 / API 33 o superior recomendado).
- Conexión a internet.
- El servidor backend debe estar activo (la app muestra errores de conexión si el servidor está apagado).

---

## 3. Roles de usuario

La app maneja tres roles. El rol determina qué pantallas y funciones se muestran:

| Rol | Descripción |
|---|---|
| **Usuario** | Acceso a comunidad, retos, STEAM, PRAE, noticias, ECO-ESTUDIO y su perfil. Puede publicar, dar like y comentar. |
| **Docente** | Mismo acceso que usuario (rol intermedio). |
| **Administrador** | Todo lo anterior más: dashboard, gestión de usuarios, crear/editar retos, crear noticias, crear proyectos STEAM, gestionar PRAE y eliminar publicaciones. |

---

## 4. Acceso a la aplicación

### 4.1 Iniciar sesión

1. Abre la app. Verás la pantalla de bienvenida con el logo.
2. En la pestaña **"iniciar sesión"** escribe tu correo electrónico y contraseña.
3. Marca **"mostrar contraseña"** si quieres verificar lo que escribes.
4. Pulsa el botón verde **"INICIAR SESIÓN"**.
5. Según tu rol, entrarás al panel de administrador o a la vista de usuario.

### 4.2 Registrarse

1. En la misma pantalla, toca la pestaña **"registrarse"**.
2. Completa: **nombre**, **correo electrónico**, **contraseña** y **confirmar contraseña** (mínimo 8 caracteres).
3. Pulsa **"registrarse"**.
4. La cuenta se crea automáticamente con rol **usuario** y quedas dentro de la app. Tu registro también aparecerá en la lista de usuarios de la versión web.

> Nota: los roles admin y docente solo los puede asignar un administrador desde el panel de usuarios.

### 4.3 Recuperar contraseña

- Toca **"¿Olvidaste tu contraseña?"** en la pantalla de login y sigue las instrucciones (el restablecimiento se gestiona por correo).

### 4.4 Credenciales de prueba (entorno local)

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | `admin@bio.co` | `password` |
| Usuario | `usuario@bio.co` | `password123` |

---

## 5. Navegación general

- **Menú lateral (drawer):** toca el ícono de tres líneas (☰) en la esquina superior izquierda de cualquier pantalla. El menú se abre de inmediato ocupando la pantalla, con el logo de Bioeducando.
- **Cerrar el menú:** toca fuera del panel o selecciona una opción.
- **Cerrar sesión:** opción al final del menú lateral.
- **Tus datos:** tu nombre y foto aparecen en la parte superior de las pantallas principales.

---

## 6. Secciones públicas (sin cuenta)

Sin iniciar sesión puedes explorar en modo lectura:

- **Comunidad** — ver las publicaciones.
- **Noticias** — ver noticias ambientales publicadas.
- **Retos** — ver los retos ecológicos activos.

Para publicar, comentar o dar like debes iniciar sesión.

---

## 7. Funciones del rol USUARIO

Menú lateral del usuario:

| Opción | Qué puedes hacer |
|---|---|
| **Retos Ecológicos** | Ver los retos ambientales disponibles, su descripción, puntos y fechas. |
| **Comunidad Ambiental** | Ver publicaciones, **crear una publicación** (texto + imagen), dar **like** (corazón) y **comentar** en los posts. |
| **Proyectos STEAM** | Consultar los proyectos STEAM registrados. |
| **Proyectos PRAE** | Ver los documentos y actividades del PRAE institucional. |
| **Noticias** | Leer las noticias ambientales, dar like y comentar. |
| **ECO-ESTUDIO** | Contenido de estudio ambiental. |
| **Configuración** | Ajustes de la app. |
| **cerrar sesión** | Salir de tu cuenta. |

### Publicar en la comunidad

1. Entra a **Comunidad Ambiental**.
2. Escribe tu texto en el campo de publicación (puedes adjuntar una imagen).
3. Pulsa publicar. Tu post aparece de inmediato y también se ve en la web.

### Comentar y dar like

- **Like:** toca el corazón bajo la publicación; el contador sube/baja al instante.
- **Comentar:** toca el ícono de comentario, escribe y envía.

---

## 8. Funciones del rol ADMINISTRADOR

Menú lateral del administrador:

| Opción | Qué puedes hacer |
|---|---|
| **Comunidad Activa** | Ver publicaciones, publicar, dar like, comentar y **eliminar publicaciones**. |
| **Retos Ecológicos** | Ver, **crear** y **editar** retos ecológicos. |
| **Proyectos STEAM** | Ver y **crear nuevos proyectos STEAM**. |
| **Gestionar PRAE** | Administrar documentos y actividades del PRAE. |
| **Noticias** | Ver y **crear noticias** ambientales. |
| **Perfil** | Ver y editar tu información de perfil y foto. |
| **Usuarios** | Ver todos los usuarios registrados y **crear usuarios** asignando rol (admin/docente/usuario). |
| **Cerrar Sesión** | Salir de la cuenta. |

### Eliminar una publicación (solo admin)

1. En **Comunidad Activa**, cada publicación muestra un menú de **tres puntos (⋮)** arriba a la derecha.
2. Toca ⋮ → **"Eliminar"**.
3. Confirma en el diálogo **"¿Estás seguro de eliminar esta publicación?"**.
4. La publicación desaparece de la app y de la web (se elimina también su imagen del servidor).

### Crear un usuario

1. Ve a **Usuarios** en el menú.
2. Pulsa **"Agregar Nuevo"**.
3. Completa nombre, correo, contraseña, confirmación y selecciona el **rol** en el desplegable.
4. Pulsa **"Crear"**. El usuario aparece en la lista y puede entrar desde la app o la web.

### Crear contenido (retos, noticias, proyectos)

- Cada sección tiene un botón **"Agregar"** o similar que abre el formulario correspondiente.
- Los retos también se pueden **editar** desde su tarjeta.

---

## 9. Solución de problemas

| Problema | Causa probable | Solución |
|---|---|---|
| "Las credenciales proporcionadas no coinciden" | Correo o contraseña mal escritos (ojo con espacios al copiar/pegar) | Escribir a mano; verificar mayúsculas |
| Error de conexión / pantalla vacía | El servidor backend está apagado | Iniciar el servidor Laravel (`php artisan serve`) |
| No se ven publicaciones nuevas | Falta de conexión o sesión expirada | Cerrar sesión y volver a entrar |
| No aparece el menú de 3 puntos en un post | No eres admin ni dueño del post | Solo el administrador (o el autor) puede eliminar |
| La app y la web no muestran lo mismo | La app apunta al servidor local, no al de producción | Actualizar `BASE_URL` en la configuración de la app |

---

## 10. Estructura técnica (referencia)

- **App:** Kotlin + Jetpack Compose.
- **Repositorio móvil:** `github.com/KarinaIzquierdo/BioeducandoMobileApk`
- **Repositorio web/backend:** `github.com/KarinaIzquierdo/Bioeducando_con_oswald`
- **Servidor API por defecto:** `http://10.0.2.2:8000/` (emulador → localhost del equipo).

---

*Manual generado para Bioeducando con Oswal — Septiembre 2026.*
