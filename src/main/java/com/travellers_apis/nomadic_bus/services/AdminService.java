package com.travellers_apis.nomadic_bus.services;

import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.repositories.AdminRepo;

@Service
public class AdminService {
    private AdminRepo repo;

    public AdminService(AdminRepo repo) {
        this.repo = repo;
    }

    public Admin addNewAdmin(Admin admin) throws AdminException {
        System.out.println(admin);
        if (admin == null)
            throw new AdminException("Admin can't be null.");
        return repo.save(admin);
    }
}
