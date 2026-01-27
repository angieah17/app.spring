-- Ejemplo 1
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('La Tierra es plana', 'Ciencia', NOW(), 1, 'VERDADERO_FALSO');
SET @id1 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id1, 0, 'Evidencia científica demuestra que la Tierra tiene forma esférica.');

-- Ejemplo 2
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('Java permite herencia múltiple de clases', 'Programación', NOW(), 1, 'VERDADERO_FALSO');
SET @id2 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id2, 0, 'Java no soporta herencia múltiple de clases; usa interfaces.');

-- Ejemplo 3 (registro inactivo para probar borrado lógico)
INSERT INTO preguntas (enunciado, tematica, fecha_creacion, activa, tipo_pregunta)
VALUES ('El SOL es una estrella', 'Astronomía', NOW(), 0, 'VERDADERO_FALSO');
SET @id3 = LAST_INSERT_ID();
INSERT INTO preguntas_verdadero_falso (id, respuesta_correcta, explicacion)
VALUES (@id3, 1, 'El Sol es una estrella de tipo espectral G2V.');