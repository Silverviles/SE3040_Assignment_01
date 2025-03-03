package com.silverviles.af_assignment.notification;

import java.io.IOException;

public interface EmailService {
    void sendEmail(String to, String subject, String body) throws IOException;
}
