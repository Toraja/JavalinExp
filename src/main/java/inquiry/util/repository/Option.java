package inquiry.util.repository;

public class Option {
    public final int limit;
    public final int page;

    public Option(int limit) {
        this.limit = limit;
        this.page = 0;
    }

    public Option(int limit, int page) {
        this.limit = limit;
        this.page = page;
    }
}
