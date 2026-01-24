package com.midominio.group.app.spring.service;

import com.midominio.group.app.spring.entity.PreguntaVF;
import com.midominio.group.app.spring.repository.PreguntaVFRepository;

import org.springframework.stereotype.Service;

/**
 * Servicio específico para preguntas Verdadero/Falso.
 */
@Service
public class PreguntaVFService extends AbstractPreguntaService<PreguntaVF> {

    public PreguntaVFService(PreguntaVFRepository repository) {
        super(repository);
    }

    // Aquí luego se añadirán métodos específicos para VF
}