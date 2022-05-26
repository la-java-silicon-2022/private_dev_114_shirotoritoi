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

	@Autowired
	SharedRepository sharedRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
		session.invalidate();
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index() {
		session.setAttribute("flg1", true);
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String index(
			@RequestParam(name = "mail", defaultValue = "") String mail,
			@RequestParam(name = "password", defaultValue = "") String password) {
		List<User> user = userRepository.findByMailAndPassword(mail, password);
		if (user.size() == 0) {
			return "redirect:/login";
		}
		session.setAttribute("user", user.get(0));
		session.setAttribute("sharedList", sharedRepository.findAll());
		return "redirect:/tasks/1";
	}

	@RequestMapping(value = "/user")
	public String user() {
		return "user";
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String user(@RequestParam("mail") String mail,
			@RequestParam("name") String name,
			@RequestParam("password") String password) {
		userRepository.saveAndFlush(new User(mail, name, password));
		return "index";
	}
}
