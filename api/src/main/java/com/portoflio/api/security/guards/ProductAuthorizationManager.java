package com.portoflio.api.security.guards;

import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import java.util.Optional;
import java.util.function.Supplier;

public class ProductAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    ProductRepository productRepository;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        boolean admin = authentication.get().getAuthorities().equals("SCOPE_ADMIN");
        Long id = Long.valueOf(object.getRequest().getParameter("id"));
        Optional<Product> oProduct = productRepository.findById(id);
        if (oProduct.isPresent()) {
            if (!admin && oProduct.get().getVisibility() == false) return new AuthorizationDecision(false);
            return new AuthorizationDecision(true);
        } return new AuthorizationDecision(false);
    }
}
