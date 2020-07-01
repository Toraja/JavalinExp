package inquiry.inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import inquiry.util.exception.SystemFailureException;
import inquiry.util.repository.Option;

public class InquiryDB implements InquiryRepository {

    private final DataSource ds;

    public InquiryDB(DataSource ds) {
        this.ds = ds;
    }

    public Inquiry findByID(long id)
            throws InquiryNotFoundException, SystemFailureException {
        String query = "SELECT id, name, email, title, detail FROM inquiry WHERE id = ?;";
        Inquiry inq = null;
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new InquiryNotFoundException(String.format(
                                "Inquery with id %d was not found.", id));
                    }
                    inq = new Inquiry();
                    inq.id = rs.getLong(1);
                    inq.name = rs.getString(2);
                    inq.email = rs.getString(3);
                    inq.title = rs.getString(4);
                    inq.detail = rs.getString(5);
                }
            }
        } catch (SQLException e) {
            throw new SystemFailureException(
                    "Error occured while dealing with repository", e);
        }
        return inq;
    };

    public Inquiry[] findAll(Option opt) throws SystemFailureException {
        String query = "SELECT id, name, email, title FROM inquiry %s;";
        String extraQuery = "";
        List<Integer> extraParams = new ArrayList<>();
        if (opt.limit > 0) {
            extraQuery += "LIMIT ?";
            extraParams.add(opt.limit);
            if (opt.page > 1) { // OFFSET is allowed only along with LIMIT
                extraQuery += " OFFSET ?";
                extraParams.add((opt.page - 1) * opt.limit);
            }
        }
        query = String.format(query, extraQuery);
        List<Inquiry> inquiries = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                int n = 0;
                for (int e : extraParams) {
                    n++;
                    stmt.setInt(n, e);
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Inquiry inq = new Inquiry();
                        inq.id = rs.getLong(1);
                        inq.name = rs.getString(2);
                        inq.email = rs.getString(3);
                        inq.title = rs.getString(4);
                        inquiries.add(inq);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemFailureException(
                    "Error occured while dealing with repository", e);
        }
        return inquiries.toArray(new Inquiry[inquiries.size()]);
    };

    public int getTotalCount() throws SystemFailureException {
        String query = "SELECT COUNT(1) FROM inquiry;";
        int count = 0;
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemFailureException(
                    "Error occured while dealing with repository", e);
        }
        return count;
    }

    public long save(Inquiry inquiry) throws SystemFailureException {
        String query = "INSERT INTO inquiry (id, name, email, title, detail) VALUES (?, ?, ?, ?, ?);";
        long newID = 0;
        try (Connection conn = ds.getConnection()) {
            newID = getLastID() + 1;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, newID);
            stmt.setString(2, inquiry.name);
            stmt.setString(3, inquiry.email);
            stmt.setString(4, inquiry.title);
            stmt.setString(5, inquiry.detail);
            stmt.execute();
        } catch (SQLException e) {
            throw new SystemFailureException(
                    "Error occured while dealing with repository", e);
        }
        return newID;
    };

    private long getLastID() throws SystemFailureException {
        String query = "SELECT MAX(id) FROM inquiry;";
        long lastID = 0;
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lastID = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SystemFailureException(
                    "Error occured while dealing with repository", e);
        }
        return lastID;
    }
}
