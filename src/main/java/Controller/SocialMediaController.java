package Controller;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();
        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO, accountDAO);
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByAccountId);

        return app;
    }

    private void handleRegister(Context ctx) {
        Account newAccount = ctx.bodyAsClass(Account.class);
        Account created = accountService.register(newAccount);
        if (created != null) {
            ctx.json(created);
        } else {
            ctx.status(400);
        }
    }

    private void handleLogin(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class);
        Account loggedIn = accountService.login(credentials);
        if (loggedIn != null) {
            ctx.json(loggedIn);
        } else {
            ctx.status(401);
        }
    }

    private void handleCreateMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message created = messageService.createMessage(message);
        if (created != null) {
            ctx.json(created);
        } else {
            ctx.status(400);
        }
    }

    private void handleGetAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void handleGetMessageById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.json(""); // Empty body if not found
        }
    }

    private void handleDeleteMessage(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(id);
        if (deleted != null) {
            ctx.json(deleted);
        } else {
            ctx.json(""); // Always 200 with empty body if not found
        }
    }

    private void handleUpdateMessage(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message incoming = ctx.bodyAsClass(Message.class);
        Message updated = messageService.updateMessage(id, incoming.getMessage_text());
        if (updated != null) {
            ctx.json(updated);
        } else {
            ctx.status(400);
        }
    }

    private void handleGetMessagesByAccountId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByAccountId(accountId));
    }
}
