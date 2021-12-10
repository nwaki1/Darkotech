package com.darkotech.app.service.impl;

import com.darkotech.app.domain.Absence;
import com.darkotech.app.repository.AbsenceRepository;
import com.darkotech.app.service.AbsenceService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Absence}.
 */
@Service
@Transactional
public class AbsenceServiceImpl implements AbsenceService {

    private final Logger log = LoggerFactory.getLogger(AbsenceServiceImpl.class);

    private final AbsenceRepository absenceRepository;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    @Override
    public Absence save(Absence absence) {
        log.debug("Request to save Absence : {}", absence);
        return absenceRepository.save(absence);
    }

    @Override
    public Optional<Absence> partialUpdate(Absence absence) {
        log.debug("Request to partially update Absence : {}", absence);

        return absenceRepository
            .findById(absence.getId())
            .map(existingAbsence -> {
                if (absence.getImage() != null) {
                    existingAbsence.setImage(absence.getImage());
                }
                if (absence.getImageContentType() != null) {
                    existingAbsence.setImageContentType(absence.getImageContentType());
                }
                if (absence.getTime() != null) {
                    existingAbsence.setTime(absence.getTime());
                }

                return existingAbsence;
            })
            .map(absenceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Absence> findAll() {
        log.debug("Request to get all Absences");
        return absenceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Absence> findOne(Long id) {
        log.debug("Request to get Absence : {}", id);
        return absenceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Absence : {}", id);
        absenceRepository.deleteById(id);
    }
}
