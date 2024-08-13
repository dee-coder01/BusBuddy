package com.travellers_apis.nomadic_bus.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.travellers_apis.nomadic_bus.controllers.admin.AdminController;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.security.CustomAuthenticationToken;
import com.travellers_apis.nomadic_bus.security.UserRoles;
import com.travellers_apis.nomadic_bus.serviceTests.utils.AdminTestUtils;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;
import com.travellers_apis.nomadic_bus.testConfiguration.TestSecurityConfig;

@WebMvcTest(AdminController.class)
@Import(TestSecurityConfig.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminLoginService adminLoginService;

    @Test
    public void testAdminLogin() throws Exception {
        String requestBody = """
                    {
                        "email": "deepak@gmail.com",
                        "password": "Password123@",
                        "role": "ADMIN"
                    }
                """;
        AdminSession session = new AdminSession();
        Admin admin = AdminTestUtils.createAdmin();
        session.setId(1L);
        session.setAdmin(admin);
        session.setUuid("string");
        session.setTime(LocalDateTime.now());
        when(adminLoginService.createAdminSession("deepak@gmail.com")).thenReturn(session);
        System.out.println(session);
        mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        // .andExpect(MockMvcResultMatchers.jsonPath("$.admin").value("1"))
    }
}
