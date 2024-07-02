package com.travellers_apis.nomadic_bus.services;

import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.repositories.AdminRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSignUpService {
    final AdminRepo repo;

    @Transactional
    public Admin addNewAdmin(Admin admin) throws AdminException {
        return repo.save(admin);
    }
}
