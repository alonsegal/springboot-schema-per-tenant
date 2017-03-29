package com.alonsegal.web;

import com.alonsegal.domain.UserTenantRelation;
import com.alonsegal.multitenancy.TenantContext;
import com.alonsegal.multitenancy.TenantNameFetcher;
import com.alonsegal.jwt.JwtAuthenticationRequest;
import com.alonsegal.jwt.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class LoginController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TenantNameFetcher tenantResolver;

    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws AuthenticationException {
        //Resolve the user's tenantId
        try {
            tenantResolver.setUsername(authenticationRequest.getUsername());
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<UserTenantRelation> utrFuture = es.submit(tenantResolver);
            UserTenantRelation utr = utrFuture.get();
            es.shutdown();
            //TODO: handle utr == null, user is not found
            //Got the tenant, now switch to the context
            TenantContext.setCurrentTenant(utr.getTenant());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Perform the authentication
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        //Generate JWT for user and send it as a Secured & HttpOnly cookie
        final UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String token = "generated_jwt_token";//jwtTokenUtil.generateToken(user);
        Cookie cookie = new Cookie(tokenHeader, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>(new JwtAuthenticationResponse(token, user), HttpStatus.OK);
    }
}