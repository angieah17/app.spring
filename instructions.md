PROYECTO: App Spring Boot + React - CRUD de Preguntas
----------------------------------------------------------
ESTADO ACTUAL:
- Backend funcionando para PreguntaVF (Verdadero/Falso).
- Frontend React con PreguntaVF.tsx y servicio funcionando.
- CRUD completo probado y operativo.

SIGUIENTE FASE: Crear Pregunta Única (Opción múltiple, una respuesta correcta)
------------------------------------------------------------------------------
ENFOQUE MÍNIMO Y DIRECTO:

BACKEND (Spring):
1. PreguntaUnica.java → extiende Pregunta.java
   - Campos: opciones (List<String>), respuestaCorrecta (int/índice).
2. PreguntaUnicaRepository.java → interface JpaRepository<PreguntaUnica, Long>
3. PreguntaUnicaService.java → extiende AbstractPreguntaService<PreguntaUnica>
4. PreguntaUnicaController.java → igual que PreguntaVFController pero con PreguntaUnica

FRONTEND (React/TypeScript):
1. PreguntaUnicaService.ts → igual que PreguntaVFService.ts pero con ruta /api/pregunta-unica
2. PreguntaUnica.tsx → mismo CRUD UI que PreguntaVF.tsx, adaptando campos para opciones múltiples.

REGLAS ESTRICTAS:
- SOLO funcionalidad base CRUD.
- REUTILIZAR lo ya existente (AbstractPreguntaService, excepciones, SecurityConfig).
- MISMOS patrones de código y estructura.
- NO añadir: Bootstrap, Swagger, gestión de usuarios, seguridad extra, logs, validaciones extras.

OBJETIVO FINAL:
CRUD de PreguntaUnica funcionando igual que PreguntaVF, sin cambios en arquitectura ni añadidos.