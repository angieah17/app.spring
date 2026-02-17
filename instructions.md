Pasos accionables (resumen corto)
Agregar assets locales en static/ y descargar Bootstrap.
Integrar Swagger (springdoc) y anotar controladores públicos.
Estandarizar paginación: backend Page<T> y frontend componente Pagination.

Contexto del proyecto (qué ya existe)

Arquitectura actual

Proyecto monolítico Spring Boot con enfoque API-first + vistas Thymeleaf mínimas.
Capa web en controladores REST y un controlador MVC para Home: HomeController.java.
Capa de negocio separada por dominio (usuarios, preguntas, tests): service.
Persistencia con Spring Data JPA y herencia de entidades de preguntas: Pregunta.java.
Seguridad centralizada con Spring Security + Basic Auth + roles: SecurityConfig.java.
Stack y configuración

Dependencias principales: Web, Data JPA, Security, Thymeleaf, Validation, MySQL, DevTools, Lombok, Test en pom.xml.
Java 21 y Spring Boot 4.0.1 definidos en pom.xml:1-40.
Configuración de BD MySQL local (puerto 3307), ddl-auto update e inicialización SQL en application.properties.
Entorno docker para MySQL en docker-compose.yml.
Dominio implementado (preguntas)

Entidad abstracta Pregunta con campos comunes: enunciado, temática, fecha, estado activo, explicación.
Estrategia JOINED + discriminador por tipo de pregunta en Pregunta.java.
Subtipos operativos:
Verdadero/Falso: PreguntaVF.java
Selección Única: PreguntaUnica.java
Selección Múltiple: PreguntaMultiple.java
Validaciones con Jakarta Validation en entidades y controladores con @Valid.
APIs implementadas

Auth:
POST /auth/register y GET /auth/me en AuthController.java.
Usuarios (admin):
CRUD paginado en /api/usuarios desde UsuarioController.java.
Administración de preguntas:
Listado paginado con filtros, búsqueda, detalle, CRUD por tipo, activar/desactivar, upload CSV en AdminPreguntaController.java.
Tests:
Generar test, enviar respuestas/corregir y consultar historial en TestController.java.
Servicios clave

Búsqueda avanzada y filtros combinables de preguntas + importación CSV en PreguntaSearchService.java.
Lógica de test completa: selección aleatoria, corrección por tipo, scoring y guardado de resultados en TestService.java.
Gestión de usuarios (registro, perfil autenticado, CRUD admin) en UsuarioService.java.
Frontend servidor (Thymeleaf)

Home disponible en home.html.
Fragmentos reutilizables en header.html y footer.html.
Errores personalizados 404/500 en 404.html y 500.html.
Datos iniciales y ejecución

Seed SQL con usuario admin y preguntas de distintos tipos/temáticas/estados en data.sql.
Seguridad espera roles y protege rutas admin/tests según reglas en SecurityConfig.java:20-60.


Swagger:
<dependency><groupId>org.springdoc</groupId><artifactId>springdoc-openapi-ui</artifactId><version>1.8.0</version></dependency>
Ajustar versiones según la versión de Spring Boot en pom.xml.

---

## Faltantes por desarrollar del backend (alineado a requisitos.md)

### Resumen rápido

Backend ya cubre buena parte de CRUD, seguridad, paginación, carga CSV y lógica de tests.
Los pendientes backend principales son: **MongoDB dual**, **Swagger/OpenAPI**, **integración con API externa**, y **separación en microservicios**.

### Pendientes backend por requisito

#### #1 — BD dual MySQL + MongoDB (**PENDIENTE**)

Estado actual:
- Solo hay configuración MySQL en `application.properties`.
- No existen dependencias ni repositorios MongoDB.

Qué falta implementar:
1. Añadir dependencia de MongoDB en `pom.xml` (`spring-boot-starter-data-mongodb`).
2. Configurar conexión Mongo en `application.properties` (`spring.data.mongodb.uri` o host/port/database).
3. Definir qué dominio irá en Mongo (ej. auditoría de intentos de test, logs de actividad o histórico detallado de respuestas).
4. Crear `@Document`, repositorio `MongoRepository` y servicio para persistir/consultar esos datos.
5. Integrar el guardado/lectura Mongo desde flujo real de negocio (no dejarlo aislado).

---

#### #15 — Funcionalidad adicional con API externa (**PENDIENTE**)

Estado actual:
- No hay integración externa visible en servicios/controladores.

Qué falta implementar (opción recomendada):
1. Definir API externa (ej. API de trivia/categorías, traducción, o datos de cultura general).
2. Crear cliente HTTP en backend (`WebClient` recomendado).
3. Implementar servicio de integración con timeout, reintentos básicos y manejo de errores.
4. Exponer endpoint backend que use esa API (por ejemplo `/api/preguntas/sugerencias` o `/api/tests/generar-externo`).
5. Añadir trazabilidad mínima (logs y fallback cuando la API externa falle).

---

#### #17 — Documentación API REST con Swagger (**PENDIENTE**)

Estado actual:
- No están las dependencias/configuración Swagger en el backend (solo mencionado en texto de este archivo).

Qué falta implementar:
1. Agregar dependencia springdoc compatible con Spring Boot 4.
2. Configurar metadatos OpenAPI (título, versión, descripción, esquema de seguridad Basic).
3. Verificar endpoints de documentación (`/swagger-ui.html` o ruta equivalente según versión).
4. Documentar controladores críticos (auth, usuarios, preguntas admin, tests) con anotaciones OpenAPI.
5. Validar que los endpoints protegidos muestren correctamente autenticación en Swagger UI.

---

#### #25 — Al menos dos microservicios (**PENDIENTE**)

Estado actual:
- Proyecto actual es monolito único.

Qué falta implementar (mínimo para cumplir):
1. Separar en al menos dos servicios (ejemplo):
	- `ms-preguntas` (CRUD, filtros, importación, catálogo)
	- `ms-tests-usuarios` (auth, usuarios, generación/corrección/historial)
2. Definir comunicación entre servicios (REST síncrono inicial).
3. Gestionar seguridad entre servicios y/o gateway (según alcance del curso).
4. Separar persistencia por servicio (bases o esquemas independientes recomendados).
5. Crear `docker-compose` para levantar stack completo de microservicios.

---

### Requisitos backend en estado **parcial** (revisar alcance exacto de evaluación)

#### #3, #4, #5 — estáticos + Bootstrap/Tailwind local

Estado observado:
- Existen templates Thymeleaf y referencias a `/css` y `/js`.
- Actualmente no hay archivos en `src/main/resources/static`.

Impacto backend:
- Aunque es capa web, se sirve desde backend Spring, por lo que conviene completarlo aquí.

### No backend (no incluidos en este plan)

- #22, #23, #24 corresponden a React/React Native (frontend/móvil), fuera de este checklist backend.