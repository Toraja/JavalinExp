package inquiry.inquiry;

import inquiry.util.exception.SystemFailureException;
import inquiry.util.repository.Option;

public class InquiryUsecase {

    private final InquiryRepository repo;

    public InquiryUsecase(InquiryRepository repo) {
        this.repo = repo;
    }

    SaveInquiryOutput saveInquiry(SaveInquiryInput in)
            throws SystemFailureException {
        long id = repo
                .save(new Inquiry(in.name, in.email, in.title, in.detail));
        return new SaveInquiryOutput(id);
    }

    GetInquiryDetailOutput getInquiryDetail(GetInquiryDetailInput in)
            throws InquiryNotFoundException, SystemFailureException {
        Inquiry inq = repo.findByID(in.inquiryID);
        return new GetInquiryDetailOutput(inq);
    }

    GetInquiriesOutput getInquiries(GetInquiriesInput in)
            throws SystemFailureException {
        int totalCount = repo.getTotalCount();
        if (totalCount == 0) {
            return new GetInquiriesOutput(null, totalCount);
        }
        Inquiry[] inquiries = repo.findAll(in.opt);
        return new GetInquiriesOutput(inquiries, totalCount);
    }
}

class SaveInquiryInput {
    final String name;
    final String email;
    final String title;
    final String detail;

    SaveInquiryInput(String name, String email, String title, String detail) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.detail = detail;
    }
}

class SaveInquiryOutput {
    final long inquiryID;

    SaveInquiryOutput(long inquiryID) {
        this.inquiryID = inquiryID;
    }
}

class GetInquiryDetailInput {
    final long inquiryID;

    GetInquiryDetailInput(long inquiryID) {
        this.inquiryID = inquiryID;
    }
}

class GetInquiryDetailOutput {
    final Inquiry inquiry;

    GetInquiryDetailOutput(Inquiry inquiry) {
        this.inquiry = inquiry;
    }
}

class GetInquiriesInput {
    final Option opt;

    GetInquiriesInput(Option opt) {
        this.opt = opt;
    }
}

class GetInquiriesOutput {
    final Inquiry[] inquiries;
    final int totalCount;

    GetInquiriesOutput(Inquiry[] inquiries, int totalCount) {
        this.inquiries = inquiries;
        this.totalCount = totalCount;
    }

}
