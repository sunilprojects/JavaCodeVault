package code.filehandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvFile {
	public static void main(String[] args) {

		
		List<String> data = new ArrayList<String>();
		data.add("FirstName,LastName,Gender,DoB,Address,Phone");
	     data.add("sunil,sky,male,17/09/2000,cbpur,9399783223");
	     data.add("sunil,software,male,2000-08-09,\"mysore,doornoo56\",9399283223");
	     data.add("shreyas,lucky,male,20/03/2014,Banglore,9399232223");
	     data.add("shreyas,lucky,male,20/03/2018,Banglore,9399232223");
	     data.add("vinay,kumar,male,10/03/2008,\"Banglore,yelahanka\",8399232223");
	     data.add("sunil1,sky1,male,20/04/20002,mysore,7399283223");
	     data.add("dhoni,M S,male,07/03/1985,\"Jaipur,Ranchi\",7390032223");
	     data.add("virat,kholi,male,07/05/1979,\"New,delhi\",7392032223");
	     


		Path filePath = Paths.get("user.csv");
		  try {
			Files.write(filePath, data);
			System.out.println("data added to csv file");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
