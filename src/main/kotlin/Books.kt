/**
 * Simple data class which encapsulate Price
 */
data class Price(val value: Double, val currency: String = "$")

/**
 * This is the immutable Book class
 */
data class Book(
  val isbn: String,
  val name: String,
  val pages: Int,
  val price: Price,
  val weight: Double,
  val year: Int,
  val authors: List<String> = emptyList(),
  val language: String
)

val books = listOf<Book>(
  Book(
    "8850333404",
    "Android 6: guida per lo sviluppatore",
    846,
    Price(39.26, "£"),
    2.1,
    2016,
    listOf("Massimo Carli"),
    "Italian"
  ),
  Book(
    "1617293296",
    "Kotlin in Action",
    334,
    Price( 14.48 , "€"),
    1.3,
    2017,
    listOf("Dmitry Jemerov","Svetlana Isakova"),
    "English"
  ),
  Book(
    "1617293296",
    "Atomic Kotlin",
    636,
    Price( 59.51 , "€"),
    2.1,
    2021,
    listOf("Bruce Eckel","Svetlana Isakova"),
    "English"
  ),
  Book(
    "0134494164",
    "Clean Architecture: A Craftsman's Guide to Software Structure and Design",
    432,
    Price( 21.49 , "€"),
    1.4,
    2017,
    listOf(" Martin Robert"),
    "English"
  ),
  Book(
    "0201633612",
    "Design patterns: elements of reusable object-oriented software",
    395,
    Price( 34.99, "€"),
    1.1,
    1995,
    listOf("Erich Gamma","Richard Helm","Ralph Johnson","John Vlissides"),
    "English"
  ),
  Book(
    "1449373321",
    "Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems",
    590,
    Price( 27.68, "£"),
    1.9,
    2016,
    listOf(" Martin Kleppmann"),
    "English"
  ),
  Book(
    "9089655964",
    "De tien mythes van Agile werken: en hoe je daarmee om kunt gaan",
    160,
    Price( 24.99, "€"),
    0.9,
    2021,
    listOf("Harry Valkink","Giovanni Dhondt"),
    "Dutch"
  )
)

