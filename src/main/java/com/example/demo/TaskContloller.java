package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskContloller {
	@Autowired
	HttpSession session;

	@Autowired
	TaskRepository taskRepository;

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public ModelAndView tasks(ModelAndView mv) {
		Boolean flg = (Boolean) session.getAttribute("flg");
		List<Task> tasks = taskRepository.findByCompleted(flg);
		if (!(flg)) {
			mv.addObject("color1", "#90b4ce");
		} else {
			mv.addObject("color3", "#90b4ce");
		}
		mv.addObject("tasks", tasks);
		mv.setViewName("tasks");
		return mv;
	}

	@RequestMapping(value = "/tasks/{flg}", method = RequestMethod.GET)
	public String tasks(@PathVariable("flg") Boolean flg) {
		session.setAttribute("flg", flg);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/completed/{code}", method = RequestMethod.GET)
	public String completed(
			@PathVariable("code") Integer code) {
		Task task = taskRepository.findById(code).get();
		task.setCompleted(true);
		taskRepository.saveAndFlush(task);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/delete/{code}", method = RequestMethod.GET)
	public String delete(
			@PathVariable("code") Integer code) {
		Task task = taskRepository.findById(code).get();
		taskRepository.delete(task);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/edit/{code}", method = RequestMethod.GET)
	public ModelAndView edit(
			@PathVariable("code") Integer code,
			ModelAndView mv) {
		Task task = taskRepository.findById(code).get();
		mv.addObject("task", task);
		mv.setViewName("edit");
		return mv;
	}

	@RequestMapping(value = "/tasks/edit", method = RequestMethod.POST)
	public String edit(
			@RequestParam("code") Integer code,
			@RequestParam("title") String title) {
		Task task = taskRepository.findById(code).get();
		task.setTitle(title);
		taskRepository.saveAndFlush(task);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/create", method = RequestMethod.GET)
	public String create() {
		return "create";
	}

	@RequestMapping(value = "/tasks/create", method = RequestMethod.POST)
	public String create(
			@RequestParam("title") String title) {
		User user = (User) session.getAttribute("user");
		Task task = new Task(user.getName(), title, false);
		taskRepository.saveAndFlush(task);
		return "redirect:/tasks";
	}
}
