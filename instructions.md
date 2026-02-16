Pasos accionables (resumen corto)
Añadir templates Thymeleaf y fragments (home, header, footer).
Agregar assets locales en static/ y descargar Bootstrap.
Implementar endpoint POST /api/preguntas/upload + servicio parser CSV/JSON.
Integrar Swagger (springdoc) y anotar controladores públicos.
Añadir templates de error 404/500.
Completar endpoints REST CRUD de Usuario y crear componentes React para gestión (si el frontend se añade aquí).
Estandarizar paginación: backend Page<T> y frontend componente Pagination.
Contexto del proyecto (qué ya existe)

Stack: Spring Boot backend (JPA, Security) y frontend React externo/pendiente.
Usuarios (parcial): registro y perfil ya implementados.
Servicio: UsuarioService.java:1-80
Repositorio: UsuarioRepository.java:1-200
Auth controller: AuthController.java:1-80
Seguridad: UserDetailsServiceImpl.java:1-80
Paginación y búsqueda: ya soportadas en servicios/repositorios (Pageable / Page).
Ej.: AbstractPreguntaService.java:1-80
Búsqueda: PreguntaSearchService.java:1-120
Recursos estáticos / Thymeleaf: actualmente NO existen templates/ ni static/ en resources (sólo application.properties y data.sql).
application.properties:1-40
Integración de los PUNTOS FALTANTES — qué implementar y dónde

Home con Thymeleaf

Controller: agregar HomeController en controller con @GetMapping("/") que devuelve "home".
Template: crear src/main/resources/templates/home.html que inserte fragments header/footer y enlace a assets locales.
Fragmentos Thymeleaf

Archivos: src/main/resources/templates/fragments/header.html y footer.html.
Uso: en templates usar th:insert="fragments/header :: header" y th:insert="fragments/footer :: footer".
Archivos estáticos (estructura sugerida)

Crear directorio:
src/main/resources/static/vendor/bootstrap/css/bootstrap.min.css
src/main/resources/static/vendor/bootstrap/js/bootstrap.bundle.min.js
src/main/resources/static/css/ (app css)
src/main/resources/static/js/ (app js)
src/main/resources/static/img/
En header.html incluir <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet"> y al final de body incluir <script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>.
Bootstrap local (sin CDN)

Descargar Bootstrap compilado y copiar los archivos a static/vendor/bootstrap/.... No usar CDN.
Subir Preguntas desde Archivo (CSV / JSON)

Endpoint: crear UploadController o añadir en PreguntaController:
@PostMapping("/api/preguntas/upload")
Método: public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file)
Detección y parsing:
Por extensión/content-type: .csv → CSV parser, .json → Jackson.
CSV: usar com.opencsv:opencsv o org.apache.commons:commons-csv para parsear filas.
JSON: ObjectMapper para mapear a DTO (crear UploadPreguntaDTO si necesario).
Mapping:
Campos esperados (ejemplo): tipo, texto, tematica, opciones (JSON array o separadas por |), respuestasCorrectas (índices separados por ;).
Para PreguntaMultiple parsear opciones → List<String> y respuestasCorrectas → List<Integer>.
Servicio: PreguntaUploadService que valida y crea entidades usando los AbstractPreguntaService/repositorios existentes.
Errores: devolver 400 con mensaje en caso de filas inválidas; reutilizar BadRequestException.
Swagger / OpenAPI

Dependencia (pom.xml):
org.springdoc:springdoc-openapi-ui:1.8.0 (ajustar versión según Spring Boot).
Config: crear OpenApiConfig en src/main/java/.../config con metadata (title, version).
Acceso: UI en /swagger-ui.html o /swagger-ui/index.html.
Anotaciones: añadir @Operation(summary=...) y @ApiResponses en controladores públicos (Preguntas, Usuarios, Upload).
Páginas Error 404 / 500

Crear src/main/resources/templates/error/404.html y 500.html reutilizando fragments.
Spring Boot las servirá automáticamente para esos códigos.
Gestión Usuarios (backend REST completo)

Controller: crear UsuarioController en controller/ con endpoints:
GET /api/usuarios → lista paginada (Pageable).
GET /api/usuarios/{id}
POST /api/usuarios
PUT /api/usuarios/{id}
DELETE /api/usuarios/{id}
Service: extender UsuarioService o crear métodos CRUD (list(Pageable), get, create, update, delete).
DTOs: UsuarioDTO para transporte (evitar exponer password), y mapear manualmente.
Seguridad: reutilizar SecurityConfig existente; proteger rutas según el patrón actual.
Gestión Usuarios (React)

Estructura sugerida (si frontend se añade aquí):
frontend/src/services/UsuarioService.ts
frontend/src/components/Usuarios/Usuarios.tsx
frontend/src/components/Usuarios/UsuarioForm.tsx
frontend/src/components/common/Pagination.tsx
API contract: endpoints REST del backend descritos arriba; listar debe aceptar page y size, devolver objeto estandarizado:
{ content: [...], page: 0, size: 10, totalElements: 123, totalPages: 13 }.
Paginación Total (reutilizable)

Backend: devolver Page<T> o DTO estándar desde controladores listados.
Frontend: Pagination component que recibe page, size, totalPages, onChange(page) y se reutiliza en todos los listados.
Dependencias sugeridas (pom.xml)

Swagger:
<dependency><groupId>org.springdoc</groupId><artifactId>springdoc-openapi-ui</artifactId><version>1.8.0</version></dependency>
CSV:
<dependency><groupId>com.opencsv</groupId><artifactId>opencsv</artifactId><version>5.7.1</version></dependency>
Ajustar versiones según la versión de Spring Boot en pom.xml.