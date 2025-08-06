package users.management.service;

import users.management.exception.EmailException;

public interface EmailSender {

    void sendEmail(String recipient, String title, String content) throws EmailException;

}
