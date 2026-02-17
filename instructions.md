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