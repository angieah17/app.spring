-- data.sql
-- Script de datos iniciales para preguntas Verdadero/Falso

-- Ejemplo 1: Pregunta activa sobre ciencia
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('La Tierra es plana', 'Ciencia', NOW(), TRUE, 'VERDADERO_FALSO');
SET @id1 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id1, FALSE, 'La evidencia científica demuestra que la Tierra tiene forma esférica.');

-- Ejemplo 2: Pregunta activa sobre programación
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('Java permite herencia múltiple de clases', 'Programación', NOW(), TRUE, 'VERDADERO_FALSO');
SET @id2 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id2, FALSE, 'Java no soporta herencia múltiple de clases; en su lugar usa interfaces para lograr polimorfismo múltiple.');

-- Ejemplo 3: Pregunta inactiva para probar borrado lógico
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('El Sol es una estrella', 'Astronomía', NOW(), FALSE, 'VERDADERO_FALSO');
SET @id3 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id3, TRUE, 'El Sol es una estrella de tipo espectral G2V ubicada en el centro de nuestro sistema solar.');

-- Ejemplo 4: Más preguntas para probar paginación
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('Spring Boot es un framework de Python', 'Programación', NOW(), TRUE, 'VERDADERO_FALSO');
SET @id4 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id4, FALSE, 'Spring Boot es un framework del ecosistema Java/Spring.');

-- Ejemplo 5: Pregunta de programación
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('JPA significa Java Persistence API', 'Programación', NOW(), TRUE, 'VERDADERO_FALSO');
SET @id5 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id5, TRUE, 'JPA es la especificación estándar de Java para el mapeo objeto-relacional.');

-- Ejemplo 6: Pregunta de ciencia
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('El agua hierve a 100°C a nivel del mar', 'Ciencia', NOW(), TRUE, 'VERDADERO_FALSO');
SET @id6 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id6, TRUE, 'A presión atmosférica normal (1 atm), el agua hierve a 100°C o 212°F.');