package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message insert(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
            }
            rs.close();
            ps.close();
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getById(int messageId) {
        Message message = null;
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Message delete(int messageId) {
        Message message = getById(messageId);
        if (message == null) return null;

        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Message updateMessageText(int messageId, String newText) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newText);
            ps.setInt(2, messageId);
            int rowsAffected = ps.executeUpdate();
            ps.close();
            if (rowsAffected > 0) {
                return getById(messageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                ));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
