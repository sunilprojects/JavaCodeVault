package code.filehandling;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import com.opencsv.CSVReader;

public class CsvFileOperation {
	 public static void main(String[] args) {
	        long startTime = System.currentTimeMillis();
	        Path filePath = Paths.get("D:\\Downloads\\Member Data.csv");
	        Path validFile = Paths.get("ValidatedMembers.csv");
	        Path invalidFile = Paths.get("FailedRecords.csv");

	        try (
	            CSVReader reader = new CSVReader(new FileReader(filePath.toFile()));
	            BufferedWriter validWriter = Files.newBufferedWriter(validFile);
	            BufferedWriter invalidWriter = Files.newBufferedWriter(invalidFile)
	        ) {
	            Set<String> uniqueChecker = new HashSet<>();// to maintain uniqueness
	            String[] columns = reader.readNext(); // Header
	            
	            //Checking header is null or not having expected columns
	            if (columns == null || columns.length < 14) {
	                System.out.println("Header missing or column count is insufficient");
	                return;
	            }
	            // adding header row to valid and invalid writters
	            validWriter.write(String.join(",", columns)+System.lineSeparator());
	            invalidWriter.write(String.join(",", columns)+ System.lineSeparator());
 
	            // reading each row
	            while ((columns = reader.readNext()) != null) {
	                if (columns.length < 14) {
	                    invalidWriter.write(String.join(",", columns) +System.lineSeparator() );
	                    continue;
	                }
                        //triming each field using ternary
	                for (int i = 0; i < columns.length; i++) {
	                    columns[i] = columns[i] != null ? columns[i].trim() : "";//ternary operator
	                }
                   // fields are created for necessary validations validations
	                String id = columns[0];
	                String firstName = columns[1];
	                String lastName = columns[2];
	                String dob = columns[3];
	                String gender = columns[4];
	                String address1 = columns[7];
	                String address2 = columns[8];
	                String mobile = columns[11];
	                
	                boolean hasEmpty = false;
                // checkinng fields wheather  emtry or not
	                for (int i = 0; i <= 11; i++) {
	                    if (columns[i].isEmpty()) {
	                        hasEmpty = true;
	                        break;
	                    }
	                }

	                if (hasEmpty) {
	                    invalidWriter.write(String.join(",", columns) + System.lineSeparator());
	                    continue;
	                }

	                String uniqueKey = firstName + lastName + gender + dob;
	                //checking unique fields
	                if (uniqueChecker.contains(uniqueKey)) {
	                    invalidWriter.write(String.join(",", columns) + System.lineSeparator());
	                    continue;
	                }
	                uniqueChecker.add(uniqueKey);

	                // validating phone number
	                mobile = mobile.replaceAll("[^0-9]", "");
	                columns[11] = mobile;
	                Pattern mobilePattern = Pattern.compile("^[789]\\d{9}$");
	                if (!mobilePattern.matcher(mobile).matches()) {
	                    invalidWriter.write(String.join(",", columns) + System.lineSeparator());
	                    continue;
	                }

	                // Removing special characters in address
	                Pattern p = Pattern.compile("[^a-zA-Z0-9\\s]");
	                columns[7] = p.matcher(address1).replaceAll("");
	                columns[8] = p.matcher(address2).replaceAll("");

	                List<DateTimeFormatter> formatters = Arrays.asList(
	                    DateTimeFormatter.ofPattern("dd-MM-yyyy"),
	                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
	                );

	                LocalDate dateOfBirth = null;
                         //parsing string to date object by checking format
	                for (DateTimeFormatter formatter : formatters) {
	                    try {
	                    	dateOfBirth = LocalDate.parse(dob, formatter);
	                        break;
	                    } catch (DateTimeParseException ignored) {}  //continue to trying other formates
	                }
	                //adding record to invalid if dateOf Bith is null
	                if  ( dateOfBirth == null) {
	                    invalidWriter.write(String.join(",", columns) + System.lineSeparator());
	                    continue;
	                }
                    //if dateOfBitth if future date than add it into invalid
	                if (dateOfBirth.isAfter(LocalDate.now())) {
	                    invalidWriter.write(String.join(",", columns) + System.lineSeparator());
	                    continue;
	                }

	                int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
	                if (age > 100) {
	                    invalidWriter.write(String.join(",", columns) + System.lineSeparator());
	                    continue;
	                }

	                validWriter.write(String.join(",", columns));
	            }

	            long endTime = System.currentTimeMillis();
	            System.out.println("Process completed,check Valid and invalid csv files");
                 System.out.println("Time taken:"+ (endTime - startTime)+ "ms");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
