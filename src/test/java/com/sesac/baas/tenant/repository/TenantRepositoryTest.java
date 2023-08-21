package com.sesac.baas.tenant.repository;

import com.sesac.baas.tenant.entity.Tenant;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;

import com.sesac.baas.user.entity.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;



@DataJpaTest
public class TenantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TenantRepository tenantRepository;

    @Test
    public void testSaveTenant() {
        // Given
        Tenant tenant = Tenant.builder()
                .serviceName("TestService")
                .users(Set.of(new User("test@example.com", "password123")))
                .build();

        // When
        Tenant savedTenant = entityManager.persistAndFlush(tenant);

        // Then
        Tenant retrievedTenant = tenantRepository.findById(savedTenant.getId()).orElse(null);
        assertThat(retrievedTenant).isNotNull();
        assertThat(retrievedTenant.getServiceName()).isEqualTo("TestService");
        assertThat(retrievedTenant.getUsers()).hasSize(1);
        assertThat(retrievedTenant.getUsers().
                iterator().next().getEmail()).isEqualTo("test@example.com");
    }
}