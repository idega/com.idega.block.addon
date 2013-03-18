package com.idega.restful.servlet;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.presentation.IWContext;
import com.idega.util.CoreUtil;
import com.idega.util.StringUtil;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public class DefaulRestfulServlet extends SpringServlet {

	private static final long serialVersionUID = 8737746855252117898L;

	@Override
	public int service(URI baseUri, URI requestUri, HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		initializeContext(request, response);
		return super.service(baseUri, requestUri, request, response);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initializeContext(request, response);
		super.service(request, response);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		initializeContext(request, response);
		super.doFilter(request, response, chain);
	}

	private void initializeContext(ServletRequest request, ServletResponse response) {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null)
			iwc = new IWContext((HttpServletRequest) request, (HttpServletResponse) response, getServletContext());

		String localeString = request.getParameter(LocaleSwitcher.languageParameterString);
		if (!StringUtil.isEmpty(localeString)) {
			Locale locale = ICLocaleBusiness.getLocaleFromLocaleString(localeString);
			if (locale == null)
				Logger.getLogger(getClass().getName()).warning("Unable to resolve locale from provided value: " + localeString);
			else
				iwc.setCurrentLocale(locale);
		}
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		initializeContext(request, response);
		super.doFilter(request, response, chain);
	}

}