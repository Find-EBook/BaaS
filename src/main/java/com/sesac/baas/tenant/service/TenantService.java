package com.sesac.baas.tenant.service;


import com.sesac.baas.tenant.dto.TenantDto;
import com.sesac.baas.tenant.entity.Tenant;
import com.sesac.baas.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public Tenant createTenant(TenantDto tenantDto){
        Tenant tenant = Tenant.builder()
                .serviceName(tenantDto.getServiceName())
                .build();
        tenantRepository.save(tenant);
        return tenant;
    }
    public Tenant findById(Long id){
        return tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant with ID: " + id + " not found."));
    }
    public boolean existsById(Long id){
        boolean byId = tenantRepository.existsById(id);
        if(!byId){
            return false;
        }
        else{
            return true;
        }
    }
}
