package com.simulator.bank.api;

import com.simulator.bank.config.DataSetup;
import com.simulator.bank.dao.AccountDAO;
import com.simulator.bank.dao.AccountJdbcDao;
import com.simulator.bank.model.Account;
import com.simulator.bank.validation.AccountValidator;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final AccountDAO dao =
            new AccountJdbcDao(DataSetup.getDataSource());

    /** Return all accounts */
    @GET
    public Response listAll() throws SQLException {
        List<Account> list = dao.listAll();
        return Response.ok(list).build();
    }

    /** Fetch by DB id */
    @GET
    @Path("/{id}")
    public Response getOne(@PathParam("id") int id) throws SQLException {
        Account a = dao.findById(id);
        if (a == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(a).build();
    }

    /** Fetch by account number */
    @GET
    @Path("/by-number/{accNumber}")
    public Response getByNumber(@PathParam("accNumber") String accNumber)
            throws SQLException {
        Optional<Account> opt = dao.findByAccountNumber(accNumber);
        return opt.map(Response::ok)
                  .orElseGet(() -> Response.status(Response.Status.NOT_FOUND))
                  .build();
    }

    /** Create */
    @POST
    public Response create(Account a) throws SQLException {
        try {
            AccountValidator.validate(a);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                           .build();
        }
        int id = dao.create(a);
        a.setAccountId(id);
        return Response.status(Response.Status.CREATED).entity(a).build();
    }

    /** Update all fields */
    @PUT
    @Path("/{id}")
    public Response updateAccount(@PathParam("id") int id, Account updated)
            throws SQLException {
        try {
            AccountValidator.validate(updated);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                           .build();
        }
        boolean ok = dao.updateAccount(id, updated);
        return ok ? Response.ok(updated).build()
                  : Response.status(Response.Status.NOT_FOUND).build();
    }

    /** Update only balance */
    @PUT
    @Path("/{id}/balance")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateBalance(@PathParam("id") int id, String newBalance)
            throws SQLException {
        boolean ok = dao.updateBalance(id, new java.math.BigDecimal(newBalance.trim()));
        return ok ? Response.ok().build()
                  : Response.status(Response.Status.NOT_FOUND).build();
    }

    /** Delete by account number */
    @DELETE
    @Path("/by-number/{accNumber}")
    public Response deleteByAccountNumber(@PathParam("accNumber") String accNumber)
            throws SQLException {
        boolean ok = dao.deleteByAccountNumber(accNumber);
        return ok ? Response.noContent().build()
                  : Response.status(Response.Status.NOT_FOUND).build();
    }
}
