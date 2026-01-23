## Estado actual del desarrollo

En la fase actual del proyecto se están tomando las siguientes decisiones
técnicas y de alcance

- SOLO se implementa el CRUD de preguntas de tipo VerdaderoFalso.
- El resto de tipos de preguntas (Selección Única y Selección Múltiple)
  se implementarán en fases posteriores.
- La base de datos utilizada actualmente es H2, con fines de desarrollo
  y pruebas.
- La persistencia se implementa con JPA aplicando herencia.
- La API REST se está construyendo y probando inicialmente contra H2.

Estas decisiones son temporales y podrán modificarse en fases futuras
del proyecto.
