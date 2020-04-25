package com.softcomputer.genetrace.tracessearchwebapp.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softcomputer.genetrace.tracessearchwebapp.model.Settings;


@Repository
public interface SettingsRepository extends CrudRepository<Settings,Integer> {

	 public Settings findByIdSettings(int id);
}
