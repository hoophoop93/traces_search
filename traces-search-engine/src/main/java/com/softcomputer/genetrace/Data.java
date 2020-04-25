package com.softcomputer.genetrace;

public class Data {

	private int years =0,months =0,days =0,hours=0,minutes=0,seconds=0;
	
	
	public static Data parseDate(String x)
	{
		Data temp = new Data();
		try
		{
			if(x.length() == 19)
			{
			temp.days = Integer.parseInt(x.substring(0, 2));
			temp.months = Integer.parseInt(x.substring(3, 5));
			temp.years = Integer.parseInt(x.substring(6, 10));
			temp.hours = Integer.parseInt(x.substring(11, 13));
			temp.minutes = Integer.parseInt(x.substring(14, 16));
			temp.seconds = Integer.parseInt(x.substring(17, 19));
			}
		}
		catch(NumberFormatException x1)
		{
			return null;
		}
		catch(StringIndexOutOfBoundsException x2)
		{
			return null;
		}
	
		return temp;

	}
	
	public boolean isEqual(Data data1)
	{
		if(seconds == data1.seconds && minutes == data1.minutes && hours == data1.hours && days == data1.days
				&& months == data1.months && years == data1.years) 
			return true;
		return false;	
	}
	
	public boolean isAfter(Data data1)
	{
		if( data1.years > years)
			return true;
			else if(data1.years == years)
			{
				if(data1.months > months) return true;
				else if(data1.months == months)
				{
					if(data1.days > days) return true;
					else if(data1.days == days)
					{
						if(data1.hours > hours) return true;
						else if( data1.hours == hours)
						{
							if(data1.minutes > minutes) return true;
							else if(data1.minutes == minutes) 
							{
								if( data1.seconds > seconds) return true;
								else return false;
							}
						}
					}
				}
			}
		return false;
	}
	
	public boolean isBefore(Data data1)
	{
		
		if( data1.years < years)
			return true;
			else if(data1.years == years)
			{
				if(data1.months < months) return true;
				else if(data1.months == months)
				{
					if(data1.days < days) return true;
					else if(data1.days == days)
					{
						if(data1.hours < hours) return true;
						else if( data1.hours == hours)
						{
							if(data1.minutes < minutes) return true;
							else if(data1.minutes == minutes) 
							{
								if( data1.seconds < seconds) return true;
								else return false;
							}
						}
					}
				}
			}
		return false;
	}


	public int getYears() {
		return years;
	}


	public void setYears(int years) {
		this.years = years;
	}

	public int getMonths() {
		return	months;
	}


	public void setMonths(int months) {
		this.months = months;
	}
	
	public int getDays() {
		return days;
	}


	public void setDays(int days) {
		this.days= days;
	}
	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}
	
	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds= seconds;
	}
}
