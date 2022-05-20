package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginContloller {
	@Autowired
	HttpSession session;

	@Autowired
	UserRepository userRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
		session.invalidate();
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index() {
		session.setAttribute("flg", true);
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String index(
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "password", defaultValue = "") String password) {
		List<User> user = userRepository.findByNameAndPassword(name, password);
		if (user.size() == 0) {
			return "redirect:/login";
		}
		session.setAttribute("user", user.get(0));
		session.setAttribute("flg", false);
		return "redirect:/tasks";
	}
}
