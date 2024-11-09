import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Bryson Lindy
 * @version 1.0
 */
public class QuizAnswer
{
    private static final int NUM_VOWELS = 2;
    private static final int NONE       = 0;

    public static void main(final String[] args)
    {
        final Path countriesPath = Path.of("countries.txt");

        try
        {
            final List<String> countries;
            final String[]     countriesAOrT;

            countries = Files.readAllLines(countriesPath);

            countries.add(null);
            countries.add(" ");

            countriesAOrT = filteredStream(countries.stream())
                    .filter(country -> country.startsWith("A") || country.startsWith("T"))
                    .map(String::toUpperCase)
                    .sorted(Comparator.comparing(String::length))
                    .toArray(String[]::new);

            System.out.println("Part A: Countries starting with A or T, in uppercase, sorted by length");
            for(final String country : countriesAOrT)
            {
                System.out.println(country);
            }

            System.out.println();

            System.out.println("Part B: Countries with exactly two vowels, reversed.");
            filteredStream(countries.stream())
                    .filter(country -> countNumVowels(country) == NUM_VOWELS)
                    .map(QuizAnswer::reversedString)
                    .forEach(System.out::println);

        }
        catch (final IOException e)
        {
            System.err.println("Error reading file. That can't be good for my mark.");
        }
    }

    /*
    Filters out nulls and blank Strings
     */
    private static Stream<String> filteredStream(final Stream<String> stream)
    {
        return stream.filter(Objects::nonNull)
                     .filter(country -> !country.isBlank());
    }

    /*
    Converts an input String into an array of lowercase char. Checks if each index of the array is a vowel and
    returns the count.
     */
    private static int countNumVowels(final String str)
    {
        final char[] strAsArray;
        int count;

        strAsArray = str.toLowerCase().toCharArray();
        count      = NONE;

        for(final char ch : strAsArray)
        {
            if("aeiou".indexOf(ch) >= NONE)
            {
                count++;
            }
        }

        return count;

    }

    /*
    Reverses the input String
     */
    private static String reversedString(final String str)
    {
        return new StringBuilder(str).reverse().toString();
    }
}
