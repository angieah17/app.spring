
-- ==========================================
-- DATA.SQL - Datos iniciales Verdadero/Falso
-- ==========================================

-- ===============================
-- PREGUNTA 1
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    1, -- Se decide poner manualmente los id en esta fase de desarrollo, teniendo cuidado de no pisar el autoincrement
    'La Tierra es plana',
    'Ciencia',
    NOW(),
    TRUE,
    'VERDADERO_FALSO'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta, explicacion
) VALUES (
    1,
    FALSE,
    'La evidencia científica demuestra que la Tierra tiene forma esférica.'
);

-- ===============================
-- PREGUNTA 2
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    2,
    'Java permite herencia múltiple de clases',
    'Programación',
    NOW(),
    TRUE,
    'VERDADERO_FALSO'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta, explicacion
) VALUES (
    2,
    FALSE,
    'Java no soporta herencia múltiple de clases; usa interfaces.'
);

-- ===============================
-- PREGUNTA 3 (inactiva)
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    3,
    'El Sol es una estrella',
    'Astronomía',
    NOW(),
    FALSE,
    'VERDADERO_FALSO'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta, explicacion
) VALUES (
    3,
    TRUE,
    'El Sol es una estrella de tipo espectral G2V.'
);

-- ===============================
-- PREGUNTA 4
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    4,
    'Spring Boot es un framework de Python',
    'Programación',
    NOW(),
    TRUE,
    'VERDADERO_FALSO'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta, explicacion
) VALUES (
    4,
    FALSE,
    'Spring Boot pertenece al ecosistema Java/Spring.'
);

-- ===============================
-- PREGUNTA 5
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    5,
    'JPA significa Java Persistence API',
    'Programación',
    NOW(),
    TRUE,
    'VERDADERO_FALSO'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta, explicacion
) VALUES (
    5,
    TRUE,
    'JPA es la especificación estándar para ORM en Java.'
);

-- ===============================
-- PREGUNTA 6
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    6,
    'El agua hierve a 100°C a nivel del mar',
    'Ciencia',
    NOW(),
    TRUE,
    'VERDADERO_FALSO'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta, explicacion
) VALUES (
    6,
    TRUE,
    'A 1 atm de presión, el agua hierve a 100°C.'
);
