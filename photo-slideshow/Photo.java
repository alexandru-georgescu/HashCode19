import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Set;

public class Photo {

	public static Comparator<Photo> verticalComparator = new Comparator<Photo>() {
		@Override
		public int compare(Photo one, Photo two) {
               if (one.numberOfTags < two.numberOfTags) return -1;
               else if (one.numberOfTags == two.numberOfTags) return 0;
               return 1;
		}
	};
	public static Comparator<Photo> horizontalComparator = new Comparator<Photo>() {
		@Override
		public int compare(Photo one, Photo two) {
               if (one.numberOfTags < two.numberOfTags) return 1;
               else if (one.numberOfTags == two.numberOfTags) return 0;
               return -1;
		}
	};
	
	public static int tagNumber; //the next available tag UID
	public static HashMap<String, Integer> allTags = new HashMap<>(); //set of all existing tags
	public static int numberOfVerticals;
	public static int numberOfHorizontals;
	public static int numberOfPhotos;
	public static int numberOfMerged;
	public static int numberOfSlides;
	
	public String photoID;
	public int numberOfTags;
	public Set <Integer> tags = new HashSet <Integer>();
	
	public Photo() {
		
	}
	
	public Photo(final String photoID, final int numberOfTags, final Set <Integer> tags) {
		this.photoID = photoID;
		this.numberOfTags = numberOfTags;
		this.tags = tags;
	}
	
	public static int commonTagsNumber(Photo one, Photo two) {
		Set <Integer> helper = mergedTags(one, two);
		return one.numberOfTags + two.numberOfTags - helper.size();
	}
	
	public static int interestFactor(Photo one, Photo two) {
		int commonTags = commonTagsNumber(one, two);
		int difference = Math.min(one.numberOfTags - commonTags, two.numberOfTags - commonTags); //one - two VS two - one
		return Math.min(difference, commonTags); //VS the intersection
	}
	
	public static Set <Integer> mergedTags(Photo one, Photo two) {
		Set <Integer> tagsHelper = new HashSet <Integer>();
		tagsHelper.addAll(one.tags);
		tagsHelper.addAll(two.tags);
		return tagsHelper;
	}
	
	public static ArrayList<Photo> mergeVerticals(ArrayList<Photo> verticalPhotos) {
		ArrayList<Photo> newHorizontalPhotos = new ArrayList<Photo>();
		int i = 0, j = Photo.numberOfVerticals - 1;
		Collections.sort(verticalPhotos, verticalComparator);
		Set <Integer> tagsHelper = new HashSet <Integer>();
		int mergedTagsNumber;
		Photo one, two;
		numberOfMerged = 0;
		while (j - i > 0) { //merge until we have one or none photo left
			one = verticalPhotos.get(i);
			two = verticalPhotos.get(j);
			tagsHelper = mergedTags(one, two);
			mergedTagsNumber = tagsHelper.size();
			newHorizontalPhotos.add(new Photo(one.photoID + " " + two.photoID, mergedTagsNumber, tagsHelper));
			i++; j--;
			numberOfMerged++;
		}
		numberOfSlides = numberOfMerged + numberOfHorizontals;
		return newHorizontalPhotos;
	}
	
	public static Queue<Photo> getResults(ArrayList<Photo> finalPhotos, int complexity) {
		Queue<Photo> results = new LinkedList<Photo>();
		LinkedList<Photo> photoList = new LinkedList<Photo>();
		photoList.addAll(finalPhotos);
		
		int count = 0;
		ListIterator<Photo> it = photoList.listIterator();
		
		int nextIndex = it.nextIndex();
		int helperIndex = 0;
		int bestIndex = 0;
		
		int bestInterestFactor = 0, helperInterestFactor = 0;
		
		Photo currentPhoto = it.next(); //initial case, current slide
		Photo helperPhoto;
		
		while(Photo.numberOfSlides > 0) {
			it.remove();
			while(it.hasPrevious()) {
				if (count >= complexity) break;
				helperIndex = it.previousIndex();
				helperPhoto = it.previous();
				helperInterestFactor = Photo.interestFactor(currentPhoto, helperPhoto);
				if (helperInterestFactor >= bestInterestFactor) {
					bestIndex = helperIndex;
					bestInterestFactor = helperInterestFactor;
				}
				count++;
			}
			
			it = photoList.listIterator(nextIndex); count = 0;
			
			while(it.hasNext()) {
				if (count >= complexity) break;
				helperIndex = it.nextIndex();
				helperPhoto = it.next();
				helperInterestFactor = Photo.interestFactor(currentPhoto, helperPhoto);
				if (helperInterestFactor >= bestInterestFactor) {
					bestIndex = helperIndex;
					bestInterestFactor = helperInterestFactor;
				}
				count++;
			}
			
			results.add(currentPhoto);
			it = photoList.listIterator(bestIndex);
			nextIndex = it.nextIndex();
			if (Photo.numberOfSlides > 1) currentPhoto = it.next();
			helperIndex = 0; bestIndex = 0;
			bestInterestFactor = 0; helperInterestFactor = 0;
			Photo.numberOfSlides--; count = 0;
		}
		return results;
	}
	
	/*public static Queue<Photo> getResults(ArrayList<Photo> finalPhotos, int complexity) {
		Queue<Photo> results = new LinkedList<Photo>();
		int[] seenSlides = new int[Photo.numberOfSlides];
		Photo currentPhoto = finalPhotos.get(0);
		
		int currentInterestFactor;
		int currentIndex = 0;
		int bestIndex = 0;
		int bestInterestFactor;
		int count = 1;
		
		while(count <= Photo.numberOfSlides) {
			bestInterestFactor = 0;
			for (int i = 1; i < Photo.numberOfSlides; i++) {
				if (seenSlides[i] == 1 || currentIndex == i) continue;
				currentInterestFactor = Photo.interestFactor(finalPhotos.get(i), currentPhoto);
				if (currentInterestFactor >= bestInterestFactor) {
					bestInterestFactor = currentInterestFactor;
					bestIndex = i;
				}
			}
			results.add(currentPhoto);
			currentPhoto = finalPhotos.get(bestIndex);
			seenSlides[currentIndex] = 1;
			currentIndex = bestIndex;
			count++;
		}
		return results;
	}*/
	
	public static void photoView(ArrayList<Photo> finalPhotos) {
		System.out.println("N: " + numberOfPhotos + "\nH: " + numberOfHorizontals + "\nV: " 
						+ numberOfVerticals + " -> " + numberOfMerged + "\nF: " + numberOfSlides);
		Iterator<Photo> photoViewer = finalPhotos.iterator();
		while (photoViewer.hasNext()) {
			Photo helper = photoViewer.next();
			System.out.println(helper.toString());
		}
	}
	
	public static void photoSimpleView(ArrayList<Photo> finalPhotos) {
		System.out.println("N: " + numberOfPhotos + "\nH: " + numberOfHorizontals + "\nV: " 
						+ numberOfVerticals + " -> " + numberOfMerged + "\nF: " + numberOfSlides); 
		}
	
	@Override
	public String toString() {
		return "ID: " + this.photoID + "; tags: " + this.numberOfTags + "-> " + this.tags;
	}
	
}
