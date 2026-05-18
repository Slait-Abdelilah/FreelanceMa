package org.example.backfreelance.service;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendVerificationEmail(String toEmail, String token) {
        String link = frontendUrl + "/verify-email/" + token;

        String html = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background: #14A800; padding: 30px; text-align: center; border-radius: 12px 12px 0 0;">
                    <h1 style="color: white; margin: 0; font-size: 28px;">FreelanceMa</h1>
                </div>
                <div style="background: #f9f9f9; padding: 40px; border-radius: 0 0 12px 12px;">
                    <h2 style="color: #001E00; margin-bottom: 16px;">Vérifiez votre email</h2>
                    <p style="color: #5E6D55; line-height: 1.6; margin-bottom: 24px;">
                        Merci de vous être inscrit sur FreelanceMa !
                        Cliquez sur le bouton ci-dessous pour activer votre compte.
                    </p>
                    <div style="text-align: center; margin: 32px 0;">
                        <a href="%s"
                           style="background: #14A800; color: white; padding: 14px 32px;
                                  border-radius: 50px; text-decoration: none;
                                  font-weight: bold; font-size: 16px;">
                            Activer mon compte
                        </a>
                    </div>
                    <p style="color: #5E6D55; font-size: 13px; margin-top: 24px;">
                        Ce lien expire dans <strong>24 heures</strong>.<br>
                        Si vous n'avez pas créé de compte, ignorez cet email.
                    </p>
                </div>
            </div>
            """.formatted(link);

        sendEmail(toEmail, "Activez votre compte FreelanceMa", html);
    }

    public void sendResetCodeEmail(String toEmail, String code) {

        String html = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
            <div style="background: #14A800; padding: 30px; text-align: center; border-radius: 12px 12px 0 0;">
                <h1 style="color: white; margin: 0;">FreelanceMa</h1>
            </div>
            <div style="background: #f9f9f9; padding: 40px; border-radius: 0 0 12px 12px;">
                <h2 style="color: #001E00;">Votre code de vérification</h2>
                <p style="color: #5E6D55; margin-bottom: 24px;">
                    Utilisez ce code pour réinitialiser votre mot de passe :
                </p>
                <div style="background: white; border: 2px solid #14A800;
                            border-radius: 12px; padding: 24px; text-align: center;
                            margin: 24px 0;">
                    <div style="font-size: 42px; font-weight: bold;
                                letter-spacing: 12px; color: #001E00;">
                        %s
                    </div>
                </div>
                <p style="color: #5E6D55; font-size: 13px;">
                    Ce code expire dans <strong>15 minutes</strong>.<br>
                    Si vous n'avez pas fait cette demande, ignorez cet email.
                </p>
            </div>
        </div>
        """.formatted(code);

        sendEmail(toEmail, "Votre code FreelanceMa", html);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, "UTF-8"
            );
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("noreply@freelancema.ma");
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("❌ Erreur envoi email : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }
    }

    public void sendVerificationCodeEmail(String toEmail, String code) {

        String html = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
            <div style="background: #14A800; padding: 30px; text-align: center; border-radius: 12px 12px 0 0;">
                <h1 style="color: white; margin: 0;">FreelanceMa</h1>
            </div>
            <div style="background: #f9f9f9; padding: 40px; border-radius: 0 0 12px 12px;">
                <h2 style="color: #001E00;">Bienvenue sur FreelanceMa !</h2>
                <p style="color: #5E6D55; margin-bottom: 24px;">
                    Pour activer votre compte, entrez ce code dans la page de vérification :
                </p>
                <div style="background: white; border: 2px solid #14A800;
                            border-radius: 12px; padding: 24px; text-align: center;
                            margin: 24px 0;">
                    <div style="font-size: 42px; font-weight: bold;
                                letter-spacing: 12px; color: #001E00;">
                        %s
                    </div>
                </div>
                <p style="color: #5E6D55; font-size: 13px;">
                    Ce code expire dans <strong>15 minutes</strong>.
                </p>
            </div>
        </div>
        """.formatted(code);

        sendEmail(toEmail, "Activez votre compte FreelanceMa", html);
    }
}