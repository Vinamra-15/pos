package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;

import io.swagger.annotations.ApiOperation;

import static com.increff.pos.util.ConvertUtil.convert;

@Controller
public class LoginController {
	@Autowired
	private UserDto userDto;

	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm loginForm) throws ApiException {
		boolean authenticated = userDto.login(req,loginForm);
		if (!authenticated) {
			return new ModelAndView("redirect:/site/login");
		}
		return new ModelAndView("redirect:/ui/home");
	}

	@ApiOperation(value = "Signs up a user")
	@RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView signUp(HttpServletRequest req, SignUpForm signUpForm) throws ApiException {
		boolean signedUp = userDto.signUp(req,signUpForm);
		if(!signedUp){
			return new ModelAndView("redirect:/site/signup");
		}
		return new ModelAndView("redirect:/ui/home");
	}

	@RequestMapping(path = "/session/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/logout");
	}



}
