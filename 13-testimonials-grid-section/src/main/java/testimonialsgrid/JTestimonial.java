package testimonialsgrid;

public class JTestimonial {
    private String author;
    private String text;
    private int age;

    // Constructor
    public JTestimonial(String author, String text, int age) {
        this.author = author;
        this.text = text;
        this.age = age;
    }

    // Getters
    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public int getAge() {
        return age;
    }

    // Setters
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
