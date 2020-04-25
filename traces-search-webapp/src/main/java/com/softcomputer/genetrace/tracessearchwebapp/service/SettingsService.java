package com.softcomputer.genetrace.tracessearchwebapp.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softcomputer.genetrace.tracessearchwebapp.dao.SettingsRepository;
import com.softcomputer.genetrace.tracessearchwebapp.model.Settings;
import com.softcomputer.genetrace.tracessearchwebapp.model.SettingsViewModel;


@Service
public class SettingsService {

	@Autowired
	SettingsRepository settingsRepository;
	
	 public void addSettngs(SettingsViewModel settingsViewModel){
	       Settings settings = new Settings();
	        
	      settings.setIdSettings(1);
	      settings.setPathoffset(settingsViewModel.getPathoffset());
	      settings.setLinesAfter(Integer.parseInt(settingsViewModel.getLinesAfter()));
	      settings.setLinesBefore(Integer.parseInt(settingsViewModel.getLinesBefore()));
	      
	     settingsRepository.save(settings);

	        
	    }
}
