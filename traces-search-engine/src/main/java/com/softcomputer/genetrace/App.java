package com.softcomputer.genetrace;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class App {

	public static void main(String[] args) throws IOException, ParseException {

		ReadFromFile readFromFile = new ReadFromFile();
		List<String> sciezki  = new ArrayList<String>();
		List<String> temp  = new ArrayList<String>();
		readFromFile.setPathOffset("C:\\logi\\wl\\domains\\PROD2\\servers");
		sciezki = readFromFile.checkFiles();
		readFromFile.setLinesBefore(10);
		readFromFile.setLinesAfter(10);
		readFromFile.setPhrase("ERROR");
	    Scanner scanner = new Scanner( System.in );

	    // e - nastepny plik, n - nastepne wystapienie frazy, b - czytanie nastepnych lin(do konca pliku),
	    //r - resetowanie buffera na poczatek aktualnego pliku, k-kilka lini
		for(int i=0; i<sciezki.size(); i++)
		{
			readFromFile.set(sciezki.get(i));
			char c = 'n';
			while(c!='e')
			{
				if(c=='n')
					temp = readFromFile.nextWithPhrase();
				else if(c=='b')
					temp = readFromFile.nextOneLine();
				else if(c=='r')
					readFromFile.resetLines();
				else if(c=='d')
					temp = readFromFile.nextWithDate("12-08-2018 17:37:31");
				else if(c=='k') {
					temp = readFromFile.nextSeveralLines();
				}
				
			for(int j=0;j<temp.size();j++)
			{
				if(j== readFromFile.getMiddle()-1)//Srodek listy(linia z wyszukiwana fraza)
				{
				System.out.println("================================================================================================================================");
				System.out.println(temp.get(j));
				System.out.println("================================================================================================================================");

				}
				else 
				System.out.println(temp.get(j));

			}
			
			System.out.println("***********************************************************************************************************************************");
			System.out.println(readFromFile.getCurrentPath());
			c = scanner.next().charAt(0);
			}

		}scanner.close();
	}

}
