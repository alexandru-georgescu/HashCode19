import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class IO {

	public static ArrayList<ArrayList<Photo>> read(Scanner scanner) {
		long startTime = System.nanoTime();  
		ArrayList<Photo> horizontalPhotos = new ArrayList<Photo>();
		ArrayList<Photo> verticalPhotos = new ArrayList<Photo>();
		
		int numberOfPhotos = scanner.nextInt();
		scanner.nextLine();
		
		Integer helperInteger;
		char type;
		int numberOfTags;
		Photo.tagNumber = 0; //unique tag number associated with the string
		Photo.numberOfPhotos = numberOfPhotos;
		Photo.numberOfVerticals = 0;
		Photo.numberOfHorizontals = 0;
		
		for (int i = 0; i < numberOfPhotos; i++) {
			String[] currentLine = scanner.nextLine().split(" ");
			type = currentLine[0].charAt(0);
			numberOfTags = Integer.parseInt(currentLine[1]);
			Set <Integer> tags = new HashSet <Integer>();
			for (int j = 0; j < numberOfTags; j++) {
				helperInteger = Photo.allTags.get(currentLine[j+2]);
				if (helperInteger == null) {
					Photo.allTags.put(currentLine[j+2], Photo.tagNumber);
					helperInteger = Photo.tagNumber;
					Photo.tagNumber++;
				}
				tags.add(helperInteger);
			}
			if (type == 'H') horizontalPhotos.add(new Photo(String.valueOf(i), numberOfTags, tags));
			else {
				verticalPhotos.add(new Photo(String.valueOf(i), numberOfTags, tags));
				Photo.numberOfVerticals++;
			}
		}
		Photo.numberOfHorizontals = Photo.numberOfPhotos - Photo.numberOfVerticals;
		ArrayList<ArrayList<Photo>> result = new ArrayList<ArrayList<Photo>>();
		result.add(horizontalPhotos);
		result.add(verticalPhotos);
		
		long finishTime = System.nanoTime() - startTime;
		double seconds = (double)finishTime / 1_000_000_000.0;
		System.out.println("Reading input terminated in " + seconds + " seconds");
		
		return result;
	}
	
	public static void write(PrintWriter printer, Queue<Photo> results) {
		long startTime = System.nanoTime();  
		printer.print(results.size() + "\n");
		Photo helper;
		while (!results.isEmpty()) {
			helper = results.poll();
			if (results.size() == 0) {
				printer.print(helper.photoID);
				break;
			}
			printer.print(helper.photoID + "\n");
		}
		long finishTime = System.nanoTime() - startTime;
		double seconds = (double)finishTime / 1_000_000_000.0;
		System.out.println("Writing output terminated in " + seconds + " seconds");
	}
}
