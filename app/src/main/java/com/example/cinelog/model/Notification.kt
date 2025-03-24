import com.google.firebase.Timestamp

data class Notification(
    val movieName: String = "",
    val posterUrl: String = "",
    val releaseDate: String = "",
    val timestamp: Timestamp = Timestamp.now()  // Use Firebase's Timestamp
) {
    // Optional: You can add a method to convert Timestamp to Long if you need it
    fun getTimestampAsLong(): Long {
        return timestamp.seconds * 1000  // Convert to milliseconds
    }
}