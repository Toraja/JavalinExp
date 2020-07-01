package inquiry.inquiry;

class Inquiry {
    long id;
    String name;
    String email;
    String title;
    String detail;

    public Inquiry() {
    }

    Inquiry(String name, String email, String title, String detail) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Inquiry object = (Inquiry) o;

        if (id != object.id)
            return false;
        if (name != null ? !name.equals(object.name) : object.name != null)
            return false;
        if (email != null ? !email.equals(object.email) : object.email != null)
            return false;
        if (title != null ? !title.equals(object.title) : object.title != null)
            return false;
        return !(detail != null ? !detail.equals(object.detail)
                : object.detail != null);
    }

    @Override
    public String toString() {
        return "Inquiry{" + "id = " + id + ", name = " + name + ", email = "
                + email + ", title = " + title + ", detail = " + detail + "}";
    }
}
