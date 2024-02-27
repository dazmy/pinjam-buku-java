package com.dazmy.pinjam.buku.resolver;

import com.dazmy.pinjam.buku.entity.User;
import com.dazmy.pinjam.buku.repository.UserRepository;
import com.dazmy.pinjam.buku.service.BasicAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final BasicAuthService basicAuthService;
    private final TransactionOperations transactionOperations;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorization = request.getHeader("Authorization");
        String basicAuth = authorization.substring(6);

        String decodeAuth = basicAuthService.toDecodeAuth(basicAuth);
        Optional<String> username = Arrays.stream(decodeAuth.split(":")).findFirst();

        if (username.isPresent()) {
            return transactionOperations.execute(status -> checkActiveUsername(username.get()));
        } else {
            throw new ResponseStatusException(401, "Username invalid resolver", null);
        }
    }

    private User checkActiveUsername(String username) {
        User user = userRepository.findByCredential_Username(username)
                .orElseThrow(() -> new ResponseStatusException(401, "Username invalid resolver", null));

        if (!user.getActive()) {
            throw new ResponseStatusException(401, "Please Login First", null);
        }

        return user;
    }
}
