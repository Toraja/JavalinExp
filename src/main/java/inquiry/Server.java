package inquiry;

import inquiry.inquiry.InquiryDB;
import inquiry.inquiry.InquiryHandler;
import inquiry.inquiry.InquiryRepository;
import inquiry.inquiry.InquiryUsecase;
import inquiry.util.infra.MySQL;
import io.javalin.Javalin;

public class Server {
    public static void main(String[] args) {

        // InquiryRepository repo = new InquiryDB(new SQLite().getDataSource());
        InquiryRepository repo = new InquiryDB(
                new MySQL("db", 3306, "inquiry").getDataSource());
        InquiryUsecase usecase = new InquiryUsecase(repo);
        InquiryHandler handler = new InquiryHandler(usecase);

        Javalin app = Javalin.create().start(8080);
        handler.addHandlers(app);
    }
}
