import java.util.*;

// Class to represent a Book with an ID and title
class Book {
    private int id;
    private String title;

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

// Class to represent a Rating given by a user to a book
class Rating {
    private int userId;
    private int bookId;
    private double rating;

    public Rating(int userId, int bookId, double rating) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public double getRating() {
        return rating;
    }
}

// Class to represent the Recommendation System
public class BookRecommendationSystem {
    private List<Book> books;
    private List<Rating> ratings;
    private Set<Integer> userIds;

    public BookRecommendationSystem(List<Book> books, List<Rating> ratings) {
        this.books = books;
        this.ratings = ratings;
        this.userIds = new HashSet<>();

        for (Rating rating : ratings) {
            userIds.add(rating.getUserId());
        }
    }

    public List<Book> recommendBooks(int userId, int topN) {
        // Get the list of books the user has rated
        Map<Integer, Double> userRatings = new HashMap<>();
        for (Rating rating : ratings) {
            if (rating.getUserId() == userId) {
                userRatings.put(rating.getBookId(), rating.getRating());
            }
        }

        // Calculate similarity scores with other users
        Map<Integer, Double> similarityScores = new HashMap<>();
        for (Rating rating : ratings) {
            int otherUserId = rating.getUserId();
            if (otherUserId != userId && !similarityScores.containsKey(otherUserId)) {
                double similarity = calculateSimilarity(userRatings, otherUserId);
                similarityScores.put(otherUserId, similarity);
            }
        }

        // Calculate weighted ratings for books not yet rated by the user
        Map<Integer, Double> weightedRatings = new HashMap<>();
        for (Rating rating : ratings) {
            if (!userRatings.containsKey(rating.getBookId())) {
                Double similarityScore = similarityScores.get(rating.getUserId());
                if (similarityScore != null) {
                    weightedRatings.put(rating.getBookId(),
                            weightedRatings.getOrDefault(rating.getBookId(), 0.0) + rating.getRating() * similarityScore);
                }
            }
        }

        // Sort books by weighted ratings and return the top N recommendations
        List<Map.Entry<Integer, Double>> sortedRatings = new ArrayList<>(weightedRatings.entrySet());
        sortedRatings.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<Book> recommendedBooks = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, sortedRatings.size()); i++) {
            recommendedBooks.add(getBookById(sortedRatings.get(i).getKey()));
        }

        // If no books are recommended (e.g., the user has rated all books), return some popular or random books
        if (recommendedBooks.isEmpty()) {
            recommendedBooks.addAll(books.subList(0, Math.min(topN, books.size())));
        }

        return recommendedBooks;
    }

    private double calculateSimilarity(Map<Integer, Double> userRatings, int otherUserId) {
        double similarity = 0.0;
        for (Rating rating : ratings) {
            if (rating.getUserId() == otherUserId && userRatings.containsKey(rating.getBookId())) {
                similarity += rating.getRating() * userRatings.get(rating.getBookId());
            }
        }
        return similarity;
    }

    private Book getBookById(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        return null;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public static void main(String[] args) {
        // Sample data
        List<Book> books = Arrays.asList(
                new Book(1, "Book A"),
                new Book(2, "Book B"),
                new Book(3, "Book C"),
                new Book(4, "Book D"),
                new Book(5, "Book E")
        );

        List<Rating> ratings = Arrays.asList(
                new Rating(1, 1, 5),
                new Rating(1, 2, 3),
                new Rating(1, 3, 4),
                new Rating(2, 2, 5),
                new Rating(2, 3, 4),
                new Rating(3, 1, 4),
                new Rating(3, 4, 2),
                new Rating(3, 5, 3),
                new Rating(4, 2, 4),
                new Rating(4, 3, 5),
                new Rating(5, 5, 4)
        );

        BookRecommendationSystem system = new BookRecommendationSystem(books, ratings);

        // Recommend books for all users
        for (int userId : system.getUserIds()) {
            List<Book> recommendations = system.recommendBooks(userId, 3);
            System.out.println("Recommended books for User " + userId + ":");
            for (Book book : recommendations) {
                System.out.println(book.getTitle());
            }
            System.out.println();
        }
    }
}

