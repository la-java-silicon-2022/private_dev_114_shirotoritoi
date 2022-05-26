package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	@Autowired
	SharedRepository sharedRepository;

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public ModelAndView tasks(ModelAndView mv) {
		mv = setTasks(mv);
		return mv;
	}

	@RequestMapping(value = "/tasks/{num}", method = RequestMethod.GET)
	public String tasks(@PathVariable("num") Integer num) {
		session.setAttribute("flg1", false);
		session.setAttribute("flg2", false);
		session.setAttribute("flg3", false);
		switch (num) {
		case 1:
			session.setAttribute("flg1", true);
			break;
		case 2:
			session.setAttribute("flg2", true);
			session.setAttribute("shared", 0);
			break;
		case 3:
			session.setAttribute("flg3", true);
		}
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
	public String edit(@PathVariable("code") Integer code) {
		Task task = taskRepository.findById(code).get();
		session.setAttribute("task", task);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/edit", method = RequestMethod.POST)
	public String edit(
			@RequestParam("code") Integer code,
			@RequestParam("title") String title,
			@RequestParam("date") String strDate,
			@RequestParam("shared") Integer shared) {
		session.removeAttribute("task");
		if (taskRepository.findById(code).isEmpty()) {
			return "redirect:/tasks";
		}
		Task task = taskRepository.findById(code).get();
		task.setTitle(title);
		task.setShared(shared);
		task.setDate(cngDate(strDate));
		taskRepository.saveAndFlush(task);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/create", method = RequestMethod.POST)
	public String create(
			@RequestParam("title") String title,
			@RequestParam(name = "date", defaultValue = "") String strDate,
			@RequestParam("shared") Integer shared) {
		if (title.length() == 0) {
			return "redirect:/tasks";
		}
		Date date;
		if (strDate.length() == 0) {
			date = cngDate(cngString(new Date()));
		} else {
			date = cngDate(strDate);
		}
		User user = (User) session.getAttribute("user");
		Task task = new Task(user.getName(), title, date, false, shared);
		taskRepository.saveAndFlush(task);
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/shared/{code}", method = RequestMethod.GET)
	public String shareds(
			@PathVariable("code") Integer code) {
		if (session.getAttribute("shared").equals(code)) {
			session.setAttribute("shared", 0);
		} else {
			session.setAttribute("shared", code);
		}
		return "redirect:/tasks";
	}

	@RequestMapping(value = "/tasks/shared/create", method = RequestMethod.POST)
	public String createShared(
			@RequestParam("name") String name) {
		Shared shared = new Shared(name);
		sharedRepository.saveAndFlush(shared);
		return "redirect:/tasks/2";
	}

	@RequestMapping(value = "/tasks/shared/delete/{code}", method = RequestMethod.GET)
	public String deleteShared(
			@PathVariable("code") Integer code) {
		Shared shared = sharedRepository.findById(code).get();
		String name = ((User) session.getAttribute("user")).getName();
		List<Task> task = taskRepository.findBySharedAndName(code, name);
		for (Task data : task) {
			taskRepository.delete(data);
		}
		if (taskRepository.findBySharedAndCompleted(code, false).size() == 0) {
			sharedRepository.delete(shared);
		}
		return "redirect:/tasks/2";
	}

	@RequestMapping(value = "/tasks/completed/delete", method = RequestMethod.GET)
	public String deleteAll() {
		String name = ((User) session.getAttribute("user")).getName();
		List<Task> taskList = taskRepository.findByCompletedAndName(true, name);
		if (taskList.size() != 0) {
			for (Task data : taskList) {
				taskRepository.delete(data);
			}
		}
		return "redirect:/tasks";
	}

	public ModelAndView setTasks(ModelAndView mv) {
		List<Task> tasks = null;
		if ((Boolean) session.getAttribute("flg1")) {
			String name = ((User) session.getAttribute("user")).getName();
			tasks = taskRepository.findByCompletedAndSharedAndNameOrderByDate(false, 0, name);
			mv.addObject("color1", "#90b4ce");
		} else if ((Boolean) session.getAttribute("flg2")) {
			tasks = taskRepository.findByCompletedAndShared(false, (Integer) session.getAttribute("shared"));
			mv.addObject("color2", "#90b4ce");
		} else if ((Boolean) session.getAttribute("flg3")) {
			tasks = taskRepository.findByCompleted(true);
			mv.addObject("color3", "#90b4ce");
		}
		List<String> date = new ArrayList<String>();
		if (tasks.size() != 0) {
			date.add((tasks.get(0)).getDate());
			for (int i = 0; i < tasks.size(); i++) {
				if (!(date.get(date.size() - 1).equals((tasks.get(i)).getDate()))) {
					date.add((tasks.get(i)).getDate());
				}
			}
		}
		session.setAttribute("sharedList", sharedRepository.findAll());
		session.setAttribute("today", cngString(new Date()));
		mv.addObject("date", date);
		mv.addObject("tasks", tasks);
		mv.setViewName("tasks");
		return mv;
	}

	public String cngString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(new Date());
	}

	public Date cngDate(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = dateFormat.parse(dateFormat.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
