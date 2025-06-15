package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255) {
            return null;
        }
        Account account = accountDAO.getById(message.getPosted_by());
        if (account == null) {
            return null;
        }
        return messageDAO.insert(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAll();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.delete(messageId);
    }

    public Message updateMessage(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getByAccountId(accountId);
    }
}
