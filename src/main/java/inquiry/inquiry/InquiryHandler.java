package inquiry.inquiry;

import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import inquiry.util.repository.Option;
import io.javalin.Javalin;

public class InquiryHandler {

    private final String formItemName = "name";
    private final String formItemEmail = "email";
    private final String formItemTitle = "title";
    private final String formItemDetail = "detail";

    private final InquiryUsecase usecase;

    public InquiryHandler(InquiryUsecase usecase) {
        this.usecase = usecase;
    }

    public void addHandlers(Javalin server) {
        server.get("/", this::index);
        server.get("/new", this::getForm);
        server.post("/new", this::saveInquiry);
        server.get("/new/submitted", this::inquerySubmitted);
        server.get("/inquiries", this::getInquiries);
        server.get("/inquiries/:id", this::getInquiryDetail);
    }

    void index(Context ctx) {
        ctx.html(InquiryIndexView.render());
    }

    void getForm(Context ctx) {
        ctx.html(NewInquiryView.render());
    }

    void saveInquiry(Context ctx) {
        try {
            SaveInquiryOutput out = usecase.saveInquiry(new SaveInquiryInput(
                    ctx.formParam(formItemName), ctx.formParam(formItemEmail),
                    ctx.formParam(formItemTitle),
                    ctx.formParam(formItemDetail)));
            ctx.redirect("/new/submitted?id=" + out.inquiryID, 303);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }

    void inquerySubmitted(Context ctx) {
        long id = Long.parseLong(ctx.queryParam("id"));
        ctx.html(InquirySubmittedView.render(id));
    }

    void getInquiryDetail(Context ctx) {
        try {
            GetInquiryDetailOutput out = usecase
                    .getInquiryDetail(new GetInquiryDetailInput(
                            Long.parseLong(ctx.pathParam("id"))));
            ctx.html(InquiryDetailView.render(out.inquiry));
        } catch (InquiryNotFoundException e) {
            throw new NotFoundResponse();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }

    void getInquiries(Context ctx) {
        try {
            Option opt = new Option(
                    Integer.parseInt(ctx.queryParam("limit", "10")),
                    Integer.parseInt(ctx.queryParam("page", "1")));
            GetInquiriesOutput out = usecase
                    .getInquiries(new GetInquiriesInput(opt));
            ctx.html(InquiriesView.render(out.inquiries, out.totalCount, opt));
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }
}
