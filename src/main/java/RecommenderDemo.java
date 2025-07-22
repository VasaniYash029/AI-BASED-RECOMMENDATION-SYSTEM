import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RecommenderDemo {

    // A simple class to hold movie metadata
    private static class Movie {
        final String title;
        final String genre;

        Movie(String title, String genre) {
            this.title = title;
            this.genre = genre;
        }
    }

    public static void main(String[] args) {
        try {
            // Load movie metadata (title and genre)
            Map<Long, Movie> movies = loadMovies("data/movies.csv");
            System.out.println("Loaded " + movies.size() + " movies.");

            // Load user ratings data
            DataModel model = new FileDataModel(new File("data/ratings.csv"));

            // Setup recommender
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            // Increase neighborhood size from 2 to 5 for better results on a small dataset
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Get user input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter user ID (1-10): ");
            int userId = scanner.nextInt();
            System.out.print("Enter preferred genre (e.g., Action, Drama): ");
            String preferredGenre = scanner.next();

            // Get up to 100 recommendations to ensure we find matches for the genre
            List<RecommendedItem> recommendations = recommender.recommend(userId, 100);

            // Filter recommendations by genre
            List<RecommendedItem> filteredRecommendations = new ArrayList<>();
            for (RecommendedItem item : recommendations) {
                Movie movie = movies.get(item.getItemID());
                if (movie != null && movie.genre.equalsIgnoreCase(preferredGenre.trim())) {
                    filteredRecommendations.add(item);
                }
            }

            // Print the top 3 filtered recommendations
            System.out.println("\nTop 3 recommendations for user " + userId + " in genre '" + preferredGenre + "':");
            int count = 0;
            for (RecommendedItem recommendation : filteredRecommendations) {
                if (count >= 3) break;
                Movie movie = movies.get(recommendation.getItemID());
                System.out.println("Movie: " + movie.title + ", Score: " + recommendation.getValue());
                count++;
            }

            if (count == 0) {
                System.out.println("No recommendations found for this genre. This could be because the user has already rated all the good matches, or there are no similar users. Try another genre or user!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<Long, Movie> loadMovies(String filePath) throws IOException {
        Map<Long, Movie> movies = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Robustly parse "id,title,year,genre" format where titles may contain commas
                int firstComma = line.indexOf(',');
                if (firstComma == -1) continue;

                int lastComma = line.lastIndexOf(',');
                if (lastComma <= firstComma) continue; // Malformed line

                int yearComma = line.lastIndexOf(',', lastComma - 1);
                if (yearComma == -1 || yearComma < firstComma) continue; // Malformed line

                try {
                    long movieID = Long.parseLong(line.substring(0, firstComma));
                    String title = line.substring(firstComma + 1, yearComma);
                    // String year = line.substring(yearComma + 1, lastComma); // Year is available if needed
                    String genre = line.substring(lastComma + 1);
                    movies.put(movieID, new Movie(title.trim(), genre.trim()));
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        }
        return movies;
    }
} 