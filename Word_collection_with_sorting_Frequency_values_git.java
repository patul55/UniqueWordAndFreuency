import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.TreeSet;

public class Word_collection_with_sorting_Frequency_values {
	static void RecursivePrint(File[] arr, int level) throws IOException {
		boolean dbg = false;
		int count1 = 0;
		// tree set for getting unique value
		TreeSet<String> ts1 = new TreeSet<String>();

		FileOutputStream outputStream = new FileOutputStream("PathToDIrecotry/word_dict.txt");
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

		FileOutputStream outputStream_frequncy = new FileOutputStream(
				"PathToDIrecotry/word_dict_Frequency.csv");
		OutputStreamWriter outputStreamWriter_frequency = new OutputStreamWriter(outputStream_frequncy, "UTF-8");
		BufferedWriter bufferedWriter_frequency = new BufferedWriter(outputStreamWriter_frequency);
		// map for getting frequency value
		Map<String, Integer> map = new HashMap<>();

		// pattern for getting only Hindi words
		String asciStartString = "[\\u0900-\\u097F]";
		Pattern regexAsciiStart = Pattern.compile(asciStartString);

		// for-each loop for main directory files
		for (File f : arr) {
			count1 += 1;
			// tabs for internal levels
			for (int i = 0; i < level; i++)
				if (dbg)
					System.out.print("\t");

			if (f.isFile()) {
				// if(dbg)
				System.out.println(f.getName());
				try {
					FileReader reader = new FileReader(f.getAbsoluteFile());
					BufferedReader bufferedReader = new BufferedReader(reader);

					String line;
					// TreeSet<String> ts1 = new TreeSet<String>();
					while ((line = bufferedReader.readLine()) != null) {
						String[] word_split = line.replaceAll("[0-9]", " ").replaceAll("[a-zA-Z ]", " ")
								.replaceAll("\\p{Punct}", " ").replaceAll("।", " ").split("\\s+");
						// String[] word_split = line.split("\\s+");

						for (int i = 0; i < word_split.length; i++) {
							Matcher regexMatcher = regexAsciiStart.matcher(word_split[i]);
							if (regexMatcher.find()) {
								if (dbg)
									System.out.println(word_split[i]);
								ts1.add(word_split[i]);
								Integer n = map.get(word_split[i]);
								n = (n == null) ? 1 : ++n;
								map.put(word_split[i], n);
							}
						}
					}

					reader.close();
					// bufferedWriter.close();
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}

			} else if (f.isDirectory()) {
				if (dbg)
					System.out.println("[" + f.getName() + "]");

				// recursion for sub-directories
				RecursivePrint(f.listFiles(), level + 1);
			}
		}
		Iterator<String> IT = ts1.iterator();
		while (IT.hasNext()) {
			bufferedWriter.write(IT.next());
			bufferedWriter.newLine();
		}
		bufferedWriter.close();

		Iterator<Entry<String, Integer>> itr = entriesSortedByValues(map).iterator();

		while (itr.hasNext()) {
			Map.Entry<String, Integer> entry = itr.next();
			//if frequency values is greater than 2 it will add that value in file
			if(entry.getValue()>=2) {
			bufferedWriter_frequency.write(entry.getKey() + "," + entry.getValue());
			bufferedWriter_frequency.newLine();
			if (dbg)
				System.out.println("Key = " + entry.getKey() + " , Value = " + entry.getValue());
			}
		}
		bufferedWriter_frequency.close();

	}

// this method use for sorting map values in descending order 
	static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

	// Driver Method
	public static void main(String[] args) throws IOException {
		// Provide full path for directory(change accordingly)
		String maindirpath = "PathOfFolderToExtractWords/master";

		// File object
		File maindir = new File(maindirpath);

		if (maindir.exists() && maindir.isDirectory()) {
			// array for files and sub-directories
			// of directory pointed by maindir
			File arr[] = maindir.listFiles();

			System.out.println("**********************************************");
			System.out.println("Files from main directory : " + maindir);
			System.out.println("**********************************************");

			// Calling recursive method
			RecursivePrint(arr, 0);
		}
	}
}
