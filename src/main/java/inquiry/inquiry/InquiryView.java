package inquiry.inquiry;

import static j2html.TagCreator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import inquiry.util.repository.Option;
import j2html.tags.ContainerTag;
import j2html.tags.EmptyTag;
import j2html.attributes.Attr;

class InquiryIndexView {
    static final String pageName = "Inquiry - Top";

    static String render() {
        return html(head(title(pageName)),
                body(h1(pageName),
                        div(a("Submit an inquiry").attr(Attr.HREF, "/new")),
                        div(a("View inquiries").attr(Attr.HREF, "/inquiries"))))
                                .render();
    }
}

class NewInquiryView {
    static final String pageName = "New Inquiry";

    static String render() {
        ContainerTag submitButton = button("Submit").attr(Attr.TYPE, "submit");
        List<String> formItems = Arrays.asList("Name", "Email", "Title");
        ContainerTag formTable = table(
                tbody(each(formItems, item -> rowWithInput(item, true)),
                        rowWithTextarea("Detail", true)));
        return html(head(title(pageName)), body(h1(pageName),
                form().withMethod("post").with(formTable, submitButton)))
                        .render();
    }

    private static ContainerTag rowWithInput(String name, boolean required) {
        EmptyTag input = input().withName(name.toLowerCase());
        if (required) {
            input = input.isRequired();
        }
        return tr(td(name), td(input));
    }

    private static ContainerTag rowWithTextarea(String name, boolean required) {
        ContainerTag textarea = textarea().withName(name.toLowerCase());
        if (required) {
            textarea = textarea.isRequired();
        }
        return tr(td(name), td(textarea));
    }
}

class InquirySubmittedView {
    static final String pageName = "Thank you for your inquiry";

    static String render(long inquiryID) {
        return html(head(title(pageName)),
                body(h1(pageName),
                        div("Your inquiry has been successfully submitted!"),
                        div("Inquiry ID is: " + inquiryID),
                        div(a("View your inquiry").attr(Attr.HREF,
                                "/inquiries/" + inquiryID)),
                        div(a("Go back to top").attr(Attr.HREF, "/"))))
                                .render();
    }
}

class InquiryDetailView {
    static final String pageName = "Inquiry Detail";

    static String render(Inquiry inq) {
        ContainerTag table = table(tbody(tr(td("Name"), td(inq.name)),
                tr(td("Email"), td(inq.email)), tr(td("Title"), td(inq.title)),
                tr(td("Detail"), td(pre(inq.detail)))));
        return html(head(title(pageName)), body(h1(pageName), table)).render();
    }
}

class InquiriesView {
    static final String pageName = "Inquiries";

    static String render(Inquiry[] inquiries, int totalCount, Option opt) {
        ContainerTag h = head(title(pageName));
        ContainerTag b = body(h1(pageName));
        ContainerTag whole = html(h, b);

        if (inquiries == null || inquiries.length == 0) {
            b.withText("No inquiries found");
            return whole.render();
        }

        ContainerTag table = table(
                thead(tr(td("ID"), td("Title"), td("Name"), td("Email"))),
                tbody(each(Arrays.asList(inquiries),
                        inq -> tr(
                                td(a(String.valueOf(inq.id)).attr(Attr.HREF,
                                        "inquiries/" + inq.id)),
                                td(inq.title), td(inq.name), td(inq.email)))));
        b.with(table);
        b.with(pagenate(totalCount, opt));
        return whole.render();
    }

    private static ContainerTag pagenate(int totalCount, Option opt) {
        return table(tbody(tr(each(perPage(opt), pp -> td(pp)), td("|"),
                each(pageNav(totalCount, opt), pt -> td(pt)))));
    }

    private static List<ContainerTag> pageNav(int totalCount, Option opt) {
        int lastPage = totalCount / opt.limit;
        if (totalCount % opt.limit != 0) {
            lastPage++;
        }

        final int VISIBLE_PAGE_COUNT = 7;
        final int VISIBLE_PAGE_COUNT_ONE_SIDE = VISIBLE_PAGE_COUNT / 2;
        boolean firstPageVisible = (opt.page
                - VISIBLE_PAGE_COUNT_ONE_SIDE) <= 1;
        int leftmostPage = firstPageVisible ? 1
                : opt.page - VISIBLE_PAGE_COUNT_ONE_SIDE;
        boolean lastPageVisible = (opt.page
                + VISIBLE_PAGE_COUNT_ONE_SIDE) >= lastPage;
        int rightmostPage = lastPageVisible ? lastPage
                : opt.page + VISIBLE_PAGE_COUNT_ONE_SIDE;
        List<Integer> visiblePages = new ArrayList<>();
        for (int i = leftmostPage; i <= rightmostPage; i++) {
            visiblePages.add(i);
        }

        List<ContainerTag> pageTags = new ArrayList<>();
        if (opt.page != 1) {
            pageTags.add(page("Prev", opt.page - 1, opt.limit));
        }
        if (!firstPageVisible) {
            pageTags.add(page("1", 1, opt.limit));
            if (visiblePages.get(0) > 2) {
                pageTags.add(span(".."));
            }
        }
        for (int pageNum : visiblePages) {
            String pageStr = String.valueOf(pageNum);
            if (pageNum == opt.page) {
                pageTags.add(span(pageStr));
                continue;
            }
            pageTags.add(page(pageStr, pageNum, opt.limit));
        }
        if (!lastPageVisible) {
            if (visiblePages.get(visiblePages.size() - 1) < lastPage - 1) {
                pageTags.add(span(".."));
            }
            pageTags.add(page(String.valueOf(lastPage), lastPage, opt.limit));
        }
        if (opt.page != lastPage) {
            pageTags.add(page("Next", opt.page + 1, opt.limit));
        }

        return pageTags;
    }

    private static ContainerTag page(String display, int page, int limit) {
        return a(String.valueOf(display)).attr(Attr.HREF,
                String.format("inquiries?limit=%d&page=%d", limit, page));
    }

    private static List<ContainerTag> perPage(Option opt) {
        int startItemNum = (opt.page - 1) * opt.limit;
        List<ContainerTag> list = new ArrayList<>();
        list.add(span("per page:"));
        for (int limit : new int[] { 10, 20, 50 }) {
            int newPage = startItemNum / limit + 1;
            ContainerTag link = a(String.valueOf(limit)).attr(Attr.HREF, String
                    .format("inquiries?limit=%d&page=%d", limit, newPage));
            list.add(link);
        }
        return list;
    }
}
