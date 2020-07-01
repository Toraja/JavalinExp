package inquiry.inquiry;

import inquiry.util.exception.SystemFailureException;
import inquiry.util.repository.Option;

public interface InquiryRepository {
    Inquiry findByID(long id)
            throws InquiryNotFoundException, SystemFailureException;

    Inquiry[] findAll(Option opt) throws SystemFailureException;

    int getTotalCount() throws SystemFailureException;

    long save(Inquiry inquiry) throws SystemFailureException;
}
