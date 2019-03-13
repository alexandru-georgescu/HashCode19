import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		
		long startTime = System.nanoTime();    

		Scanner scanner = new Scanner(new File(args[0] + ".txt"));
		PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(args[0] + "out.txt")));

		ArrayList<ArrayList<Photo>> photos = new ArrayList<ArrayList<Photo>>();
		photos = IO.read(scanner);
		ArrayList<Photo> horizontalPhotos = photos.get(0);
		ArrayList<Photo> verticalPhotos = photos.get(1);
		
		ArrayList<Photo> finalPhotos = Photo.mergeVerticals(verticalPhotos);
		finalPhotos.addAll(horizontalPhotos); //now we only have one-slided photos
		
		//Photo.photoView(finalPhotos); //not recommended for large inputs
		Photo.photoSimpleView(finalPhotos);
		
		Collections.sort(finalPhotos, Photo.horizontalComparator); //sort ascending by number of tags
		Queue<Photo> results = Photo.getResults(finalPhotos, 1000);
		IO.write(printer, results);
		
		scanner.close();
		printer.close();
		
		long finishTime = System.nanoTime() - startTime;
		double seconds = (double)finishTime / 1_000_000_000.0;
		System.out.println("Program terminated in " + seconds + " seconds");
	}

}
