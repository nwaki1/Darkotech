package com.darkotech.app.service.impl;

import com.darkotech.app.domain.Auhority;
import com.darkotech.app.repository.AuhorityRepository;
import com.darkotech.app.service.AuhorityService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Auhority}.
 */
@Service
@Transactional
public class AuhorityServiceImpl implements AuhorityService {

    private final Logger log = LoggerFactory.getLogger(AuhorityServiceImpl.class);

    private final AuhorityRepository auhorityRepository;

    public AuhorityServiceImpl(AuhorityRepository auhorityRepository) {
        this.auhorityRepository = auhorityRepository;
    }

    @Override
    public Auhority save(Auhority auhority) {
        log.debug("Request to save Auhority : {}", auhority);
        return auhorityRepository.save(auhority);
    }

    @Override
    public Optional<Auhority> partialUpdate(Auhority auhority) {
        log.debug("Request to partially update Auhority : {}", auhority);

        return auhorityRepository
            .findById(auhority.getId())
            .map(existingAuhority -> {
                if (auhority.getAuhorityName() != null) {
                    existingAuhority.setAuhorityName(auhority.getAuhorityName());
                }

                return existingAuhority;
            })
            .map(auhorityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Auhority> findAll() {
        log.debug("Request to get all Auhorities");
        return auhorityRepository.findAllWithEagerRelationships();
    }

    public Page<Auhority> findAllWithEagerRelationships(Pageable pageable) {
        return auhorityRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Auhority> findOne(Long id) {
        log.debug("Request to get Auhority : {}", id);
        return auhorityRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Auhority : {}", id);
        auhorityRepository.deleteById(id);
    }
}
