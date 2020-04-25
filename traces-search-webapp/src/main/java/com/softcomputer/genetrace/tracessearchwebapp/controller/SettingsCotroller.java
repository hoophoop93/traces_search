package com.softcomputer.genetrace.tracessearchwebapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.softcomputer.genetrace.tracessearchwebapp.dao.SettingsRepository;
import com.softcomputer.genetrace.tracessearchwebapp.model.LogsPanelViewModel;
import com.softcomputer.genetrace.tracessearchwebapp.model.Settings;
import com.softcomputer.genetrace.tracessearchwebapp.model.SettingsViewModel;
import com.softcomputer.genetrace.tracessearchwebapp.service.SettingsService;

@Controller
public class SettingsCotroller {

	@Autowired
	SettingsService settingsService;
	
	@Autowired
	SettingsRepository settingsRepository;
	
	@GetMapping(value = "/settings")
	public ModelAndView loadSettingsPage(@ModelAttribute("model") SettingsViewModel model) {

		Settings settings = new Settings();
		settings = settingsRepository.findByIdSettings(1);
		
		if(settings != null) {
			model.setPathoffset(settings.getPathoffset());
			model.setLinesAfter(Integer.toString(settings.getLinesAfter()));
			model.setLinesBefore(Integer.toString(settings.getLinesBefore()));	
		}
		return new ModelAndView("/settings");
	}
	
	@PostMapping(value = "/settings")
	public ModelAndView postSettings(@Valid @ModelAttribute("model") SettingsViewModel model,
			 final BindingResult result,final RedirectAttributes redirectAttrs) {

		
		if (result.hasErrors()) {
			return new ModelAndView("/settings", "model", model);
		}

		settingsService.addSettngs(model);
		
		List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Properties set correctly.");

        redirectAttrs.addFlashAttribute("infos", infoMessages);
        
        return new ModelAndView("redirect:/");
	}

}
