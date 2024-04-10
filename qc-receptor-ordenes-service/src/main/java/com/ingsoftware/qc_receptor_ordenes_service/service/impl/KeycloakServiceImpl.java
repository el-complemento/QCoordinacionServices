package com.ingsoftware.qc_receptor_ordenes_service.service.impl;

import com.ingsoftware.qc_receptor_ordenes_service.controllers.dto.UserDTO;
import com.ingsoftware.qc_receptor_ordenes_service.service.IKeycloakService;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public class KeycloakServiceImpl implements IKeycloakService {
    @Override
    public List<UserRepresentation> findAllUsers() {
        return null;
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return null;
    }

    @Override
    public String createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {

    }
}
