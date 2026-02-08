-- ==========================================
-- DATA.SQL - Datos iniciales Verdadero/Falso
-- ==========================================

-- ===============================
-- PREGUNTA 1
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    1,
    'La Tierra es plana',
    'Ciencia',
    NOW(),
    TRUE,
    'VERDADERO_FALSO',
    'La evidencia científica demuestra que la Tierra tiene forma esférica.'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta
) VALUES (
    1,
    FALSE
);

-- ===============================
-- PREGUNTA 2
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    2,
    'Java permite herencia múltiple de clases',
    'Programación',
    NOW(),
    TRUE,
    'VERDADERO_FALSO',
    'Java no soporta herencia múltiple de clases; usa interfaces.'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta
) VALUES (
    2,
    FALSE
);

-- ===============================
-- PREGUNTA 3 (inactiva)
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    3,
    'El Sol es una estrella',
    'Astronomía',
    NOW(),
    FALSE,
    'VERDADERO_FALSO',
    'El Sol es una estrella de tipo espectral G2V.'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta
) VALUES (
    3,
    TRUE
);

-- ===============================
-- PREGUNTA 4
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    4,
    'Spring Boot es un framework de Python',
    'Programación',
    NOW(),
    TRUE,
    'VERDADERO_FALSO',
    'Spring Boot pertenece al ecosistema Java/Spring.'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta
) VALUES (
    4,
    FALSE
);

-- ===============================
-- PREGUNTA 5
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    5,
    'JPA significa Java Persistence API',
    'Programación',
    NOW(),
    TRUE,
    'VERDADERO_FALSO',
    'JPA es la especificación estándar para ORM en Java.'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta
) VALUES (
    5,
    TRUE
);

-- ===============================
-- PREGUNTA 6
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    6,
    'El agua hierve a 100°C a nivel del mar',
    'Ciencia',
    NOW(),
    TRUE,
    'VERDADERO_FALSO',
    'A 1 atm de presión, el agua hierve a 100°C.'
);

INSERT IGNORE INTO preguntas_verdadero_falso (
    id, respuesta_correcta
) VALUES (
    6,
    TRUE
);

-- ==========================================
-- Datos iniciales Pregunta Única
-- ==========================================

-- ===============================
-- PREGUNTA 7
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    7,
    '¿Cuál es la capital de Francia?',
    'Geografía',
    NOW(),
    TRUE,
    'UNICA',
    'París es la capital y ciudad más grande de Francia.'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta
) VALUES (
    7,
    1
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
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    8,
    '¿Qué lenguaje se utiliza principalmente para desarrollo web front-end?',
    'Programación',
    NOW(),
    TRUE,
    'UNICA',
    'JavaScript es el lenguaje estándar para desarrollo web front-end.'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta
) VALUES (
    8,
    2
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
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    9,
    '¿Cuál es el planeta más grande del Sistema Solar?',
    'Ciencia',
    NOW(),
    TRUE,
    'UNICA',
    'Júpiter es el planeta más grande con un diámetro de 142,984 km.'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta
) VALUES (
    9,
    1
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
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    10,
    '¿Qué framework Java se utiliza para crear aplicaciones web?',
    'Programación',
    NOW(),
    FALSE,
    'UNICA',
    'Spring Boot es el framework más popular para aplicaciones Java web.'
);

INSERT IGNORE INTO preguntas_unica (
    id, respuesta_correcta
) VALUES (
    10,
    0
);

INSERT IGNORE INTO pregunta_unica_opciones (pregunta_id, opcion, orden) VALUES
(10, 'Spring Boot', 0),
(10, 'Django', 1),
(10, 'Express', 2),
(10, 'Laravel', 3);

-- ==========================================
-- Datos iniciales Pregunta Múltiple
-- ==========================================

-- ===============================
-- PREGUNTA 11
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    11,
    '¿Cuáles de los siguientes son lenguajes de programación orientados a objetos?',
    'Programación',
    NOW(),
    TRUE,
    'MULTIPLE',
    'Java, Python y C++ son lenguajes OOP. JavaScript es un lenguaje orientado a prototipos, aunque también soporta características OOP.'
);

INSERT IGNORE INTO preguntas_multiple (
    id
) VALUES (
    11
);

INSERT IGNORE INTO pregunta_multiple_opciones (pregunta_id, opcion, orden) VALUES
(11, 'Java', 0),
(11, 'Python', 1),
(11, 'JavaScript', 2),
(11, 'C++', 3);

INSERT IGNORE INTO pregunta_multiple_respuestas (pregunta_id, respuesta_correcta, orden) VALUES
(11, 0, 0),
(11, 1, 1),
(11, 3, 2);

-- ===============================
-- PREGUNTA 12
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    12,
    '¿Cuáles de las siguientes son características de Spring Boot?',
    'Programación',
    NOW(),
    TRUE,
    'MULTIPLE',
    'Spring Boot proporciona configuración automática, servidor embebido y dependencias preconfiguradas. No requiere XML de configuración tradicional.'
);

INSERT IGNORE INTO preguntas_multiple (
    id
) VALUES (
    12
);

INSERT IGNORE INTO pregunta_multiple_opciones (pregunta_id, opcion, orden) VALUES
(12, 'Autoconfiguration', 0),
(12, 'Servidor embebido (Tomcat)', 1),
(12, 'Requiere configuración XML obligatoria', 2),
(12, 'Starters predefinidos', 3);

INSERT IGNORE INTO pregunta_multiple_respuestas (pregunta_id, respuesta_correcta, orden) VALUES
(12, 0, 0),
(12, 1, 1),
(12, 3, 2);

-- ===============================
-- PREGUNTA 13
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    13,
    '¿Cuáles de los siguientes planetas tienen anillos?',
    'Astronomía',
    NOW(),
    TRUE,
    'MULTIPLE',
    'Saturno, Júpiter, Urano y Neptuno tienen sistemas de anillos. Aunque Saturno es el más conocido por sus anillos visibles.'
);

INSERT IGNORE INTO preguntas_multiple (
    id
) VALUES (
    13
);

INSERT IGNORE INTO pregunta_multiple_opciones (pregunta_id, opcion, orden) VALUES
(13, 'Tierra', 0),
(13, 'Saturno', 1),
(13, 'Júpiter', 2),
(13, 'Urano', 3),
(13, 'Neptuno', 4);

INSERT IGNORE INTO pregunta_multiple_respuestas (pregunta_id, respuesta_correcta, orden) VALUES
(13, 1, 0),
(13, 2, 1),
(13, 3, 2),
(13, 4, 3);

-- ===============================
-- PREGUNTA 14 (inactiva)
-- ===============================
INSERT IGNORE INTO preguntas (
    id, enunciado, tematica, fecha_creacion, activa, tipo_pregunta, explicacion
) VALUES (
    14,
    '¿Cuáles de las siguientes son bases de datos SQL?',
    'Programación',
    NOW(),
    FALSE,
    'MULTIPLE',
    'MySQL, PostgreSQL y Oracle son bases de datos SQL. MongoDB es una base de datos NoSQL orientada a documentos.'
);

INSERT IGNORE INTO preguntas_multiple (
    id
) VALUES (
    14
);

INSERT IGNORE INTO pregunta_multiple_opciones (pregunta_id, opcion, orden) VALUES
(14, 'MySQL', 0),
(14, 'MongoDB', 1),
(14, 'PostgreSQL', 2),
(14, 'Oracle', 3);

INSERT IGNORE INTO pregunta_multiple_respuestas (pregunta_id, respuesta_correcta, orden) VALUES
(14, 0, 0),
(14, 2, 1),
(14, 3, 2);