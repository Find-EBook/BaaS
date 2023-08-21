package com.sesac.baas.tenant.repository;

import com.sesac.baas.tenant.entity.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {


}
