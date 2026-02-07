-- ==========================================
-- DATA.SQL - Datos iniciales Verdadero/Falso
-- ==========================================

-- ===============================
-- PREGUNTA 1
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    1,
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

-- ==========================================
-- Datos iniciales Pregunta Única
-- ==========================================

-- ===============================
-- PREGUNTA 7
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    7,
    '¿Cuál es la capital de Francia?',
    'Geografía',
    NOW(),
    TRUE,
    'UNICA'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta, explicacion
) VALUES (
    7,
    1,
    'París es la capital y ciudad más grande de Francia.'
);

INSERT IGNORE INTO pregunta_unica_opciones (pregunta_id, opcion, orden) VALUES
(7, 'Londres', 0),
(7, 'París', 1),
(7, 'Berlín', 2),
(7, 'Madrid', 3);

-- ===============================
-- PREGUNTA 8
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    8,
    '¿Qué lenguaje se utiliza principalmente para desarrollo web front-end?',
    'Programación',
    NOW(),
    TRUE,
    'UNICA'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta, explicacion
) VALUES (
    8,
    2,
    'JavaScript es el lenguaje estándar para desarrollo web front-end.'
);

INSERT IGNORE INTO pregunta_unica_opciones (pregunta_id, opcion, orden) VALUES
(8, 'Python', 0),
(8, 'Java', 1),
(8, 'JavaScript', 2),
(8, 'C++', 3);

-- ===============================
-- PREGUNTA 9
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    9,
    '¿Cuál es el planeta más grande del Sistema Solar?',
    'Ciencia',
    NOW(),
    TRUE,
    'UNICA'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta, explicacion
) VALUES (
    9,
    1,
    'Júpiter es el planeta más grande con un diámetro de 142,984 km.'
);

INSERT IGNORE INTO pregunta_unica_opciones (pregunta_id, opcion, orden) VALUES
(9, 'Saturno', 0),
(9, 'Júpiter', 1),
(9, 'Neptuno', 2),
(9, 'Urano', 3);

-- ===============================
-- PREGUNTA 10 (inactiva)
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta
) VALUES (
    10,
    '¿Qué framework Java se utiliza para crear aplicaciones web?',
    'Programación',
    NOW(),
    FALSE,
    'UNICA'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta, explicacion
) VALUES (
    10,
    0,
    'Spring Boot es el framework más popular para aplicaciones Java web.'
);

INSERT IGNORE INTO pregunta_unica_opciones (pregunta_id, opcion, orden) VALUES
(10, 'Spring Boot', 0),
(10, 'Django', 1),
(10, 'Express', 2),
(10, 'Laravel', 3);