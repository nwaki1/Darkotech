package com.darkotech.app.service.impl;

import com.darkotech.app.domain.Permission;
import com.darkotech.app.repository.PermissionRepository;
import com.darkotech.app.service.PermissionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Permission}.
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission save(Permission permission) {
        log.debug("Request to save Permission : {}", permission);
        return permissionRepository.save(permission);
    }

    @Override
    public Optional<Permission> partialUpdate(Permission permission) {
        log.debug("Request to partially update Permission : {}", permission);

        return permissionRepository
            .findById(permission.getId())
            .map(existingPermission -> {
                if (permission.getPermissionName() != null) {
                    existingPermission.setPermissionName(permission.getPermissionName());
                }

                return existingPermission;
            })
            .map(permissionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findOne(Long id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
    }
}
