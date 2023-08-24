/*
package com.sesac.baas.tenant.service;

import com.sesac.baas.tenant.dto.TenantDto;
import com.sesac.baas.tenant.entity.Tenant;
import com.sesac.baas.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class TenantServiceTest {

    @InjectMocks
    private TenantService tenantService;

    @Mock
    private TenantRepository tenantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println(tenantService); // tenantService 객체 상태 출력
        System.out.println(tenantRepository); // mock 객체 상태 출력
    }

    @Test
    public void testCreateTenant() {
        // Given
        TenantDto tenantDto = new TenantDto();
        tenantDto.setServiceName("TestService");

        Tenant mockTenant = Tenant.builder()
                .serviceName(tenantDto.getServiceName())
                .build();
        when(tenantRepository.save(any(Tenant.class))).thenReturn(mockTenant);

        // When
        Tenant savedTenant = tenantService.createTenant(tenantDto);

        // Then
        assertThat(savedTenant).isNotNull(); // 추가한 코드
        assertThat(savedTenant.getServiceName()).isEqualTo("TestService");
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

}*/
