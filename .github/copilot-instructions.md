# Copilot Instructions – Proyecto Spring Boot Preguntas

## Descripción general
Esta aplicación tiene como objetivo gestionar preguntas y realizar evaluaciones
sobre temas específicos. Proporciona:

- Una API REST para operaciones CRUD
- Una interfaz web navegable
- Consumo desde React y React Native

El sistema gestiona distintos tipos de preguntas aplicando herencia.

---

## Stack tecnológico obligatorio

### Backend
- Spring Boot
- Java
- JPA / Hibernate
- Bases de datos:
  - MySQL (relacional)
  - MongoDB (no relacional)
- Spring Security (autenticación obligatoria)
- Thymeleaf (solo para Home)
- Swagger para documentación REST

### Frontend
- React (web)
- React Native (móvil, funcionalidades limitadas)

### Estilos y recursos
- Bootstrap o Tailwind
- Todos los recursos deben estar **descargados localmente**
- NO usar CDN ni librerías externas online
- Los recursos estáticos se sirven desde `/static`

---

## Reglas generales del proyecto

- Arquitectura por capas:
  - Controller
  - Service
  - Repository
- No lógica de negocio en controladores
- Uso de DTOs para entrada/salida
- API REST completa con:
  - GET
  - POST
  - PUT
  - DELETE
- Todas las vistas deben ser navegables desde la página Home
- Implementar paginación en TODOS los listados
- Gestión personalizada de errores:
  - 404
  - 500

---

## Dominio de la aplicación

### Tipos de preguntas
- Verdadero / Falso
- Selección Única
- Selección Múltiple

### Herencia
- Aplicar herencia entre los distintos tipos de preguntas
- Usar una clase base `Pregunta`
- Cada tipo concreto extiende de `Pregunta`

---

## Funcionalidades obligatorias

### CRUD de preguntas
- Crear, editar, listar y eliminar preguntas
- Soporte para filtros por:
  - Temática
  - Tipo de pregunta
- CRUD completo para:
  - Verdadero/Falso
  - Selección Única
  - Selección Múltiple

### Funcionalidades adicionales
- Generación de:
  - Preguntas sueltas
  - Secuencias de preguntas
- Validación y revisión de respuestas
- Subida masiva de preguntas desde archivo
- Almacenamiento en base de datos

---

## Relaciones entre entidades
- Definir relaciones entre:
  - Preguntas
  - Temáticas / Categorías
- Usar correctamente JPA para las relaciones

---

## Seguridad
- La aplicación debe estar securizada en servidor
- Acceso solo mediante credenciales
- Gestión de usuarios:
  - Backend: API REST
  - Frontend: React

---

## API REST
- API REST completa para:
  - Preguntas
  - Usuarios
- Consumida por:
  - React
  - React Native
- Documentación obligatoria con Swagger

---

## Testing y evaluación
- Implementar tests
- Contabilizar resultados
- Almacenar resultados por usuario
- Tests realizados desde React

---

## Acceso móvil
- Acceso desde React Native
- Solo un subconjunto reducido de funcionalidades

---

## Microservicios
- Implementar al menos dos microservicios sencillos
- Comunicación entre servicios bien definida

---

## Restricciones importantes

- NO usar librerías desde internet en tiempo de ejecución
- NO saltarse capas
- NO mezclar responsabilidades
- Respetar estrictamente la arquitectura definida
