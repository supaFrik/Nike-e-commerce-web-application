package vn.demo.nike.features.auth.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject("Mã code xác minh email của bạn");
            helper.setText(buildTemplate(code), true);
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not send verification email", ex);
        }
    }

    private String buildTemplate(String code) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Your Nike Member profile code</title>
                </head>
                <body style="margin:0;padding:0;background:#ffffff;font-family:Arial,Helvetica,sans-serif;color:#111111;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background:#ffffff;">
                        <tr>
                            <td align="center" style="padding:42px 18px 26px;">
                                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="max-width:430px;width:100%%;">
                                    <tr>
                                        <td align="center" style="padding-bottom:44px;">
                                            <img src="https://upload.wikimedia.org/wikipedia/commons/a/a6/Logo_NIKE.svg" width="66" alt="Nike" style="display:block;width:66px;height:auto;border:0;">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size:20px;line-height:28px;font-weight:400;padding-bottom:14px;">
                                            Your Nike Member profile code
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size:12px;line-height:20px;font-weight:700;padding-bottom:46px;">
                                            Here's the one-time verification code you requested:
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="border-top:1px solid #d6d6d6;border-bottom:1px solid #d6d6d6;text-align:center;padding:30px 0 37px;font-size:34px;line-height:40px;font-weight:400;letter-spacing:0;">
                                            %s
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size:14px;line-height:22px;font-weight:400;padding-top:12px;">
                                            This code expires after 15 minutes.
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size:13px;line-height:22px;color:#666666;padding-top:10px;padding-bottom:66px;">
                                            If you've already received this code or don't need it any more, ignore this email.
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="border-top:1px solid #d6d6d6;padding-top:50px;padding-bottom:34px;font-size:20px;line-height:28px;">
                                            Nike.com
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="border-top:1px solid #d6d6d6;padding-top:28px;font-size:11px;line-height:18px;color:#9a9a9a;">
                                            &copy; 2026 Nike, Inc. All Rights Reserved.
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size:11px;line-height:16px;color:#9a9a9a;padding-top:14px;">
                                            Nike European Operations Netherlands, B.V. Colosseum 1,<br>
                                            1213 NL, Hilversum, The Netherlands
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="font-size:11px;line-height:16px;color:#9a9a9a;padding-top:32px;">
                                            <a href="https://www.nike.com/help/a/privacy-policy" style="color:#9a9a9a;text-decoration:none;">Privacy Policy</a>
                                            <span style="display:inline-block;width:18px;">&nbsp;</span>
                                            <a href="https://www.nike.com/help" style="color:#9a9a9a;text-decoration:none;">Get Help</a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(code);
    }
}
