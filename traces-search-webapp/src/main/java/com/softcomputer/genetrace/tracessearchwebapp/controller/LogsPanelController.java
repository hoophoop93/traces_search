package com.softcomputer.genetrace.tracessearchwebapp.controller;

import javax.validation.Valid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.softcomputer.genetrace.tracessearchwebapp.model.LogsPanelViewModel;

@Controller
@Scope("session")
public class LogsPanelController {

	@GetMapping(value = "/")
	public ModelAndView loadPanelPage() {

		return new ModelAndView("/logsPanel", "model", new LogsPanelViewModel());
	}

	@PostMapping(value = "/")
	public ModelAndView postSelectOption(@Valid @ModelAttribute("model") LogsPanelViewModel model,
			 final BindingResult result,final RedirectAttributes redirectAttrs) {


		if (result.hasErrors()) {
			return new ModelAndView("/logsPanel", "model", model);
		}

		
		redirectAttrs.addAttribute("dateStart",model.getDateStart());
		redirectAttrs.addAttribute("dateEnd",model.getDateEnd());
		redirectAttrs.addAttribute("phrase",model.getPhrase());
		redirectAttrs.addAttribute("Split",model.getSplitFlag());

		
		return new ModelAndView("redirect:/main");
	}

	@GetMapping(value = "/",params="action=clear")
	public ModelAndView clearAll() {

		return new ModelAndView("/logsPanel", "model", new LogsPanelViewModel());
	}
}




