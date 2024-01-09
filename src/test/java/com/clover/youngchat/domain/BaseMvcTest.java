package com.clover.youngchat.domain;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.clover.youngchat.global.MockSpringSecurityFilter;
import com.clover.youngchat.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.UserTest;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class BaseMvcTest implements UserTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    protected Principal mockPrincipal;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
        mockUserSetup();
    }

    private void mockUserSetup() {
        UserDetails testUserDetails = new UserDetailsImpl(TEST_USER);
        mockPrincipal = new UsernamePasswordAuthenticationToken(
            testUserDetails, "", testUserDetails.getAuthorities());
    }
}
