package com.carsync.challenge.api.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
public class AuthInterceptor implements HandlerInterceptor {

	public static final ThreadLocal<UserContext> context = new ThreadLocal<>();
	private static final String AUTH_HEADER = "authorization";

	@Autowired
	private JwtTokenProvider _tokenProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String authToken = (String) request.getHeader(AUTH_HEADER);
		if (authToken != null && authToken.startsWith("Bearer ")) {
			authToken = authToken.substring(7, authToken.length());
			context.set(new UserContext());
			context.get().setUserId(getTokenProvider().getUserId(authToken));
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		context.remove();

	}

}
