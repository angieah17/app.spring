package com.midominio.group.app.spring.service;

import com.midominio.group.app.spring.entity.PreguntaVF;
import com.midominio.group.app.spring.repository.PreguntaRepository;
import com.midominio.group.app.spring.repository.PreguntaVFRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Servicio específico para preguntas Verdadero/Falso.
 */
@Service
public class PreguntaVerdaderoFalsoService extends AbstractPreguntaService<PreguntaVF> {

    private final PreguntaVFRepository pVFRepository;


    public PreguntaVerdaderoFalsoService(PreguntaRepository preguntaRepository,
                                        PreguntaVFRepository pVFRepository) {
        super(preguntaRepository);
        this.pVFRepository = pVFRepository;
    }

    public PreguntaVF crearPreguntaVerdaderoFalso(PreguntaVF pregunta) {
        return pVFRepository.save(pregunta);
    }

    public Page<PreguntaVF> listarVerdaderoFalso(Pageable pageable) {
        return preguntaRepository.findByTipoPregunta("VERDADERO_FALSO", pageable)
                .map(p -> (PreguntaVF) p);
    }

}
