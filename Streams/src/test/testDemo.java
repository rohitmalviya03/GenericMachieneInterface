package test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class testDemo {



	public static void main(String argvs[])  
	{  
		// an empty stream is created here  
		List<String> list = Arrays.asList("apple", "banana", "cherry", "date");

		// Creating a stream from a collection
		Stream<String> stream1 = list.stream();

		// Creating a stream from an array
		String[] arr = {"apple", "banana", "cherry"};
		Stream<String> stream2 = Arrays.stream(arr);

		// Creating a stream using Stream.of()
		Stream<String> stream3 = Stream.of("apple", "banana", "cherry");

		List<String> filtered = list.stream()
				.filter(s -> s.startsWith("b"))
				.collect(Collectors.toList()); // ["banana"]
		System.out.println(filtered);


		List<String> uppercase = list.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList()); // ["APPLE", "BANANA", "CHERRY", "DATE"]
		System.out.println(uppercase);
		
		List<String> numbers = Arrays.asList("1", "2", "2", "3", "4", "4", "5");
		List<String> distinctNumbers = numbers.stream()
		                                       .distinct()
		                                       .collect(Collectors.toList()); // [1, 2, 3, 4, 5]
		System.out.println(distinctNumbers);
		
		
		List<List<String>> nestedList = Arrays.asList(Arrays.asList("apple", "banana"), Arrays.asList("cherry", "date"));
		List<String> flattened = nestedList.stream()
		                                  .flatMap(List::stream)
		                                  .collect(Collectors.toList()); // ["apple", "banana", "cherry", "date"]

		
		System.out.println("asd"+list);
		
		
		
		
		List<String> collected = list.stream()
                .collect(Collectors.toList()); // Collects elements into a List

		System.out.println(collected);

		Optional<String> concatenated = list.stream()
                .reduce((s1, s2) -> s1 + " " + s2); // Concatenates the strings
		concatenated.ifPresent(System.out::println); // Output: "apple banana cherry date"
		System.out.println(concatenated);
		list.stream().forEach(System.out::println); // Prints each element



			boolean anyMatch = list.stream()
			.anyMatch(s -> s.startsWith("a")); // true
			boolean allMatch = list.stream()
			.allMatch(s -> s.length() > 3); // true
			boolean noneMatch = list.stream()
			 .noneMatch(s -> s.contains("a")); // true
			System.out.println(anyMatch);
			System.out.println(allMatch);
			System.out.println(noneMatch);
	}  
}
