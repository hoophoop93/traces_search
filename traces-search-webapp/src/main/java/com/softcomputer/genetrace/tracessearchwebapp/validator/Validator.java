package com.softcomputer.genetrace.tracessearchwebapp.validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.web.servlet.ModelAndView;

import com.softcomputer.genetrace.Data;

public class Validator {

	String dateStart;
	String dateEnd;
	Data test = new Data();
	LocalDateTime localDate = LocalDateTime.now();
		
	String message;

	public ModelAndView validateDate(String dateStart, String dateEnd) {
		ModelAndView modelAndView = new ModelAndView();
		
		if (!dateEnd.equals("") && !dateStart.equals("") && Data.parseDate(dateEnd).isEqual(Data.parseDate(dateStart))) {
			message = "Dates can not be equal";
			System.out.println(message);
			modelAndView.setViewName("/error");
			return modelAndView.addObject("message", message);
		}
		if (!dateEnd.equals("")  && !dateStart.equals("") && Data.parseDate(dateEnd).isAfter(Data.parseDate(dateStart))) {
			message = "Start date has to be before end date";
			System.out.println(message);
			modelAndView.setViewName("/error");
			return modelAndView.addObject("message", message);
		}

		return null;
	}

	public ModelAndView validateFutureDate(String dateStart, String dateEnd) {
		ModelAndView modelAndView = new ModelAndView();
		
		
		test.setDays(localDate.getDayOfMonth());
		test.setMonths(localDate.getMonthValue());
		test.setYears(localDate.getYear());
		test.setHours(localDate.getHour());
		test.setMinutes(localDate.getMinute());
		test.setSeconds(localDate.getSecond());
		

		if(dateStart.equals("") && dateEnd.equals("")){

		}else {
			if ((dateEnd.equals("") && test.isAfter(test.parseDate(dateStart))) || (!dateStart.equals("") && !dateEnd.equals("") && test.isAfter(test.parseDate(dateStart)))) {
				message = "The start date can not be higher than today's date";
				System.out.println(message);
				modelAndView.setViewName("/error");
				return modelAndView.addObject("message", message);
			}
			if ((dateStart.equals("") && test.isAfter(test.parseDate(dateEnd))) || (!dateStart.equals("") && !dateEnd.equals("") && test.isAfter(test.parseDate(dateEnd)))) {
				message = "The end date can not be higher than today's date";
				System.out.println(message);
				modelAndView.setViewName("/error");
				return modelAndView.addObject("message", message);
			}
		}
		return null;
	}

}
