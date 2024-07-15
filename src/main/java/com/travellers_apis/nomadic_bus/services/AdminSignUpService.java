package com.travellers_apis.nomadic_bus.services;

import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.repositories.AdminRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSignUpService {
    final AdminRepository repository;

    @Transactional
    public Admin addNewAdmin(Admin admin) throws AdminException {
        return repository.save(admin);
    }
}
