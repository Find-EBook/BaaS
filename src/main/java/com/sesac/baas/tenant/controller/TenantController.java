package com.sesac.baas.tenant.controller;


import com.sesac.baas.tenant.dto.TenantDto;
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
    public String createTenant(@ModelAttribute TenantDto tenantDto) {
        tenantService.createTenant(tenantDto);
        return "redirect:/"; // Redirecting back to index or wherever you want.
    }


}
