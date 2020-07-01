package inquiry.inquiry;

class InquiryNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    InquiryNotFoundException(String message) {
        super(message);
    }
}
