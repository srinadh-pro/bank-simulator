package com.simulator.bank.api;

import com.simulator.bank.config.DataSetup;
import com.simulator.bank.dao.TransactionJdbcDao;
import com.simulator.bank.model.Transaction;
import com.simulator.bank.validation.TransactionValidator;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final TransactionJdbcDao dao =
            new TransactionJdbcDao(DataSetup.getDataSource());

    @POST
    @Path("/deposit")
    public Response deposit(Transaction tx) {
        try {
            TransactionValidator.validate(tx);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                           .build();
        }
        try {
            dao.deposit(tx.getAccountId(),
                        tx.getTransactionAmount(),
                        tx.getDescription(),
                        tx.getModeOfTransaction());
            return Response.status(Response.Status.CREATED)
                           .entity("{\"message\":\"Deposit successful\"}")
                           .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    @POST
    @Path("/withdraw")
    public Response withdraw(Transaction tx) {
        try {
            TransactionValidator.validate(tx);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                           .build();
        }
        try {
            dao.withdraw(tx.getAccountId(),
                         tx.getTransactionAmount(),
                         tx.getDescription(),
                         tx.getModeOfTransaction());
            return Response.status(Response.Status.CREATED)
                           .entity("{\"message\":\"Withdrawal successful\"}")
                           .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    @GET
    @Path("/by-account/{accountId}")
    public Response getAllByAccount(@PathParam("accountId") int accountId) {
        try {
            List<Transaction> list = dao.listByAccountId(accountId);
            return Response.ok(list).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }
}
