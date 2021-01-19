package com.sda.rideshare.services;

import com.sda.rideshare.controllers.BaseController;
import com.sda.rideshare.entities.BookingEntity;
import com.sda.rideshare.entities.RideEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail (List<String> emails, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            InternetAddress[] internetAddresses = getInternetAddresses(emails);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("noreply@baeldung.com");
            mimeMessageHelper.setTo(internetAddresses);
            mimeMessageHelper.setSubject("Notificare - Ia-ma, Nene!");
            mimeMessage.setContent(content, "text/html");
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            e.getMessage();
        }
    }

    public void sendEmail (String email, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("noreply@baeldung.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Notificare - Ia-ma, Nene!");
            mimeMessage.setContent(content, "text/html");
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            e.getMessage();
        }
    }

    private InternetAddress[] getInternetAddresses(List<String> emails) throws AddressException {
        InternetAddress[] internetAddresses = new InternetAddress[emails.size()];
        for (int i = 0; i < emails.size(); i++) {
            internetAddresses[i] = new InternetAddress(emails.get(i));
        }
        return internetAddresses;
    }
    public  String getContent (RideEntity rideEntity) {
        return "Stimate client,<br> " +
                    "Cursa de la "+ rideEntity.getDepartureCity() + " la " + rideEntity.getArrivalCity()+ " din data de "+ rideEntity.getDepartureDate()+ " a fost anulata de catre "+
                    rideEntity.getUser().getName()+ ".<br> Pentru alte curse pe acelasi traseu, va rugam sa verificati site-ul nostru.<br>"+
                    " Numai bine,<br> " +
                    " Familia Ia-maNene ";
    }

    public String getContentBooking (RideEntity rideEntity, BookingEntity bookingEntity){
        return "Stimate client,<br><br> " +
                bookingEntity.getUser().getName()+ " a rezervat " + bookingEntity.getBookedSeats()+ " loc/locuri pentru cursa de la "+ rideEntity.getDepartureCity() + " la " + rideEntity.getArrivalCity()+ " din data de "+ rideEntity.getDepartureDate()+ ". <br>"+
                 "Poti lua legatura cu el/ea la numarul de telefon: "+bookingEntity.getUser().getPhoneNumber()+ " .<br><br>"+
                " Numai bine,<br> " +
                " Familia Ia-maNene ";
    }

    public String getContentBookingCancellation (RideEntity rideEntity, BookingEntity bookingEntity){
        return "Stimate client,<br><br> " +
                bookingEntity.getUser().getName()+ " a  anulat rezervarea pentru cursa de la "+ rideEntity.getDepartureCity() + " la " + rideEntity.getArrivalCity()+ " din data de "+ rideEntity.getDepartureDate()+ ". <br>"+
                "Nu te descuraja! Alti membrii ai comunitatii pot apela la tine!<br><br>"+
                " Numai bine,<br> " +
                " Familia Ia-maNene ";
    }
}
