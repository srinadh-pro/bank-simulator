package com.simulator.bank.dao;

import com.simulator.bank.config.DataSourceFactory;
import com.simulator.bank.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT customer_id, username, email, phone_number, status, dob FROM customers";
        try (var conn = DataSourceFactory.getDataSource().getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            List<Customer> out = new ArrayList<>();
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setUsername(rs.getString("username"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setStatus(rs.getString("status"));
                Date d = rs.getDate("dob");
                if (d != null) c.setDob(d.toLocalDate());
                out.add(c);
            }
            return out;
        }
    }

    public Customer findById(int id) throws SQLException {
        String sql = "SELECT customer_id, username, email, phone_number, status, dob FROM customers WHERE customer_id = ?";
        try (var conn = DataSourceFactory.getDataSource().getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setUsername(rs.getString("username"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setStatus(rs.getString("status"));
                Date d = rs.getDate("dob");
                if (d != null) c.setDob(d.toLocalDate());
                return c;
            }
        }
    }

    public int create(Customer c) throws SQLException {
        String sql = """
            INSERT INTO customers (username, password, email, phone_number, status, dob)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (var conn = DataSourceFactory.getDataSource().getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getPhoneNumber());
            ps.setString(5, c.getStatus());
            if (c.getDob() != null) {
                ps.setDate(6, Date.valueOf(c.getDob()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    c.setCustomerId(id);
                    return id;
                }
            }
            throw new SQLException("No generated key returned");
        }
    }

    public boolean update(int id, Customer c) throws SQLException {
        String sql = """
            UPDATE customers
               SET username=?, email=?, phone_number=?, status=?, dob=?
             WHERE customer_id=?
        """;
        try (var conn = DataSourceFactory.getDataSource().getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getUsername());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhoneNumber());
            ps.setString(4, c.getStatus());
            if (c.getDob() != null) ps.setDate(5, Date.valueOf(c.getDob()));
            else ps.setNull(5, Types.DATE);
            ps.setInt(6, id);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (var conn = DataSourceFactory.getDataSource().getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
