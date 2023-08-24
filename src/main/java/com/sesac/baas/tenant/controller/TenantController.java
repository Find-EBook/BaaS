package com.sesac.baas.tenant.controller;


import com.sesac.baas.tenant.dto.TenantDto;
import com.sesac.baas.tenant.entity.Tenant;
import com.sesac.baas.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping("/")
    public String createTenantForm(Model model) {
        model.addAttribute("tenantDto", new TenantDto());
        return "index";
    }


    @PostMapping("/createTenant")
    public String createTenant(@ModelAttribute TenantDto tenantDto,Model model) {
        Tenant createTenant = tenantService.createTenant(tenantDto);

        // 생성된 Tenanat 정보를 Model에 추가
        model.addAttribute("createdTenant", createTenant);

        // 결과 페이지로 리다이렉트하거나 결과 페이지 반환
        return "tenantResult";
    }


}
