
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mallikharjuna Rao Dande
 */
public class Booking {
    private String itemId;
    private String userId;
    private Date bookingDate;
    private String bookingID;
    private String type;
    private String details;
    private String stringBookinDate;

    public Booking(String itemId, String userId, String stringBookinDate, String bookingID, String type, String details) {
        this.itemId = itemId;
        this.userId = userId;
        this.stringBookinDate = stringBookinDate;
        this.bookingID = bookingID;
        this.type = type;
        this.details = details;
    }
    
    public Booking() {
    }

    public String getStringBookinDate() {
        return stringBookinDate;
    }

    public void setStringBookinDate(String stringBookinDate) {
        this.stringBookinDate = stringBookinDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Booking(String itemId, String userId, Date bookingDate, String bookingID) {
        this.itemId = itemId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.bookingID = bookingID;
    }

    
    
    public Booking(String itemId, String userId) {
        this.itemId = itemId;
        this.userId = userId;
        bookingID="1234";
       bookingDate=Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String s = df.format(bookingDate);
        try {
            bookingDate=df.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(Booking.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    
}
