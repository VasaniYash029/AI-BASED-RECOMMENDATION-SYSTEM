# AI-BASED-RECOMMENDATION-SYSTEM

**COMPANY**: CODTECH IT SOLUTIONS

**NAME**: Yash Yogesh Vasani

**INTERN ID**: CT12DL225

**DOMAIN**: JAVA PROGRAMMING

**DURATION**: 12 WEEKS

**MENTOR**: NEELA SANTOSH

**DESCRIPTION**:


# 📽️ AI Recommendation System with Apache Mahout

This Java program demonstrates a **user-based collaborative filtering recommendation system** using **Apache Mahout**, a powerful machine learning library. It recommends movies based on user ratings and filters them by user-preferred genre.

---

## 📦 Libraries and Tools Used

* **Apache Mahout**: Used for building recommender systems.

  * `FileDataModel`, `UserSimilarity`, `UserNeighborhood`, `Recommender`, etc.
* **Java I/O & Utility Classes**: For reading CSV files, storing movie data, and handling user input.

---

## 🧠 Working Principle

This system works using **user-based collaborative filtering**, where the similarity between users is calculated based on their past ratings. Similar users form a "neighborhood", and the system recommends items liked by these neighbors.

---

## 🧱 Code Breakdown

### 1. **Movie Class**

```java
private static class Movie {
    final String title;
    final String genre;
    Movie(String title, String genre) { ... }
}
```

A simple data structure to store a movie’s title and genre. It maps movie IDs to metadata for filtering purposes.

---

### 2. **Main Logic**

#### ✅ Load Movie Metadata

```java
Map<Long, Movie> movies = loadMovies("data/movies.csv");
```

This loads a `movies.csv` file containing movie ID, title, year, and genre. It skips malformed lines and handles commas inside movie titles.

#### ✅ Load Ratings Data

```java
DataModel model = new FileDataModel(new File("data/ratings.csv"));
```

This parses the user rating data into Mahout’s internal format.

#### ✅ Build the Recommender

```java
UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);
Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
```

* **PearsonCorrelationSimilarity**: Measures how similarly two users rate items.
* **NearestNUserNeighborhood**: Uses top 5 most similar users for recommendation.

---

### 3. **User Input**

```java
Scanner scanner = new Scanner(System.in);
System.out.print("Enter user ID (1–10): ");
int userId = scanner.nextInt();
System.out.print("Enter preferred genre: ");
String preferredGenre = scanner.next();
```

Prompts the user to enter their ID and preferred genre (e.g., Action, Comedy, Drama).

---

### 4. **Generate and Filter Recommendations**

```java
List<RecommendedItem> recommendations = recommender.recommend(userId, 100);
```

Generates up to 100 recommendations. These are filtered by comparing each movie's genre with the user’s preferred genre.

```java
if (movie != null && movie.genre.equalsIgnoreCase(preferredGenre.trim())) { ... }
```

Only movies matching the genre are added to the final list.

---

### 5. **Display Top 3 Recommendations**

```java
for (RecommendedItem recommendation : filteredRecommendations) {
    ...
    System.out.println("Movie: " + movie.title + ", Score: " + recommendation.getValue());
}
```

Shows up to 3 recommended movies with their recommendation score. If no recommendations are found, the system provides an appropriate message.

---

## 📁 `loadMovies()` Function

A custom method to load movie data from a CSV. It handles:

* Commas in movie titles
* Invalid or malformed lines
* Parsing ID, title, and genre correctly

---
**OUTPUT**:

<img width="986" height="266" alt="Image" src="https://github.com/user-attachments/assets/e7040bc4-8dd4-4a88-b8d6-dc939bb8e7c4" />
<img width="1005" height="262" alt="Image" src="https://github.com/user-attachments/assets/2c57529b-d3af-4aa3-8895-601b0920cbe8" />
<img width="963" height="601" alt="Image" src="https://github.com/user-attachments/assets/fee89618-dfc4-46cc-acc2-c688df8f7e29" />
