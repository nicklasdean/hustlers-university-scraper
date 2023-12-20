import java.util.Objects;

public class Post {
    private String author;
    private String timestamp;
    private String body;
    private String replyFromAuthor;
    private int engagement;

    public Post(){}

    public Post(String author, String timestamp, String body){
        this.author = author;
        this.timestamp = timestamp;
        this.body = body;
        this.replyFromAuthor = "";
        this.engagement = 0;

    }
    public Post(String body){
        this.author = "";
        this.body = body;
        this.timestamp = "";
        this.replyFromAuthor = "";
        this.engagement = 0;
    }

    public Post(String body, int engagement) {
        this.author = "";
        this.body = body;
        this.engagement = engagement;
        this.replyFromAuthor = "";
        this.timestamp = "";
    }

    public Post(String author, String timestamp, String body, String replyFromAuthor, int engagement) {
        this.author = author;
        this.timestamp = timestamp;
        this.body = body;
        this.replyFromAuthor = replyFromAuthor;
        this.engagement = engagement;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReplyFromAuthor() {
        return replyFromAuthor;
    }

    public void setReplyFromAuthor(String replyFromAuthor) {
        this.replyFromAuthor = replyFromAuthor;
    }

    public int getEngagement() {
        return engagement;
    }

    public void setEngagement(int engagement) {
        this.engagement = engagement;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author='" + author + '\'' +
                ", date='" + timestamp + '\'' +
                ", body='" + body + '\'' +
                ", replyFromAuthor='" + replyFromAuthor + '\'' +
                ", engagement=" + engagement +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return engagement == post.engagement && Objects.equals(author, post.author) && Objects.equals(timestamp, post.timestamp) && Objects.equals(body, post.body) && Objects.equals(replyFromAuthor, post.replyFromAuthor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, timestamp, body, replyFromAuthor, engagement);
    }
}
