package com.sesac.baas.tenant.service;


import com.sesac.baas.tenant.dto.TenantDto;
import com.sesac.baas.tenant.entity.Tenant;
import com.sesac.baas.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
