/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mallikharjuna Rao Dande
 */
@Named(value = "controllerBean")
@SessionScoped
public class ControllerBean implements Serializable {
    NewItem requestedItem = new NewItem();
    String searchString;
    boolean loggedIn;
    Booking bookingEntry;
    Date bookingDate;
    Equipment equipment;
    Equipment bookedEquipment;
    ArrayList<Equipment> equipments = new ArrayList();
    String type;
    String companyName;
    String studentId;
    String password;
    Student studentInSession;
    boolean isLoggedIn;
    BooksAndJournals book;
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    Student newStudent = new Student();
    public ControllerBean(NewItem requestedItem) {
        this.requestedItem = requestedItem;
    }

    /**
     * Creates a new instance of ControllerBean
     */
    public ControllerBean() {
    }

    public ControllerBean(Booking bookingEntry, Date date, String type, String companyName) {
        this.bookingEntry = bookingEntry;
        this.bookingDate = date;
        this.type = type;
        this.companyName = companyName;

    }

    public Booking getBookingEntry() {
        return bookingEntry;
    }

    public void setBookingEntry(Booking bookingEntry) {
        this.bookingEntry = bookingEntry;
    }

    public NewItem getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(NewItem requestedItem) {
        this.requestedItem = requestedItem;
    }

    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }

    public Equipment getBookedEquipment() {
        return bookedEquipment;
    }

    public void setBookedEquipment(Equipment bookedEquipment) {
        this.bookedEquipment = bookedEquipment;
    }

    public Date getBookingDate() {

        bookingDate = new Date();
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public ArrayList<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStudentId() {
        return studentId;
    }

    public Student getStudentInSession() {
        return studentInSession;
    }

    public void setStudentInSession(Student studentInSession) {
        this.studentInSession = studentInSession;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BooksAndJournals getBook() {
        return book;
    }

    public void setBook(BooksAndJournals book) {
        this.book = book;
    }

    public Equipment getEquipment() {

        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select equipmentId,companyName,type,details from equipment");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                equipments.add(new Equipment(rs.getString("equipmentId"), rs.getString("companyName"), rs.getString("type"), rs.getString("details")));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        for (Equipment E : equipments) {
            if (E.getCompanyName().equals(companyName)) {
                bookedEquipment = E;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("equipment", bookedEquipment);
                return bookedEquipment;
            }
        }
        return bookedEquipment;
    }

    public String createBookingEntry() throws MessagingException {
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String booking = dateFormat.format(bookingDate);
        String bookingId = "1123";
        FacesContext fc = FacesContext.getCurrentInstance();
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int number = 0;
        try {

            pstmt = conn.prepareStatement("insert into BOOKING (itemId,userId,bookingDate,type,details)values('" + bookedEquipment.getProjectorId() + "','" + usr_ID + "','" + booking + "','"+bookedEquipment.getType()+"','"+bookedEquipment.getDetails()+"')");
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("select bookingId,itemId,userId from booking where bookingId=(select max(bookingId) from booking)");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                number = rs.getInt("bookingId");
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        bookingEntry = new Booking(bookedEquipment.getProjectorId(), usr_ID);
        bookingEntry.setBookingDate(bookingDate);
        bookingEntry.setBookingID(bookingId);
        bookingId = number + "";
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingDetails", bookingEntry);
        generateAndSendEmail("Booking", bookingId);
        return bookingId;
    }

    public void login() throws NullPointerException {
        String login = "BootStrap";
        ArrayList<Student> students = new ArrayList<>();
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String role= "Student";
        try {
            pstmt = conn.prepareStatement("select sid,firstName,lastName,department,email,password,isAdmin,firstLogin from student");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getString("sid"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("department"), rs.getString("email"), rs.getString("password"), rs.getString("isAdmin"), rs.getString("firstLogin")));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                if (student.getPassword().equals(password)) {
                    login = "studentLandinPage";

                    studentInSession = student;
                    isLoggedIn = true;
                    if(student.getIsAdmin().equals("true")){
                    role = "Admin";
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", studentId);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", student.getIsAdmin());
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("studentDetails", studentInSession);
                }
            }
        }

        //return login;        
    }

    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session
                = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        isLoggedIn = false;
        return "loggedout";
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void specialLogin() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ArrayList<Student> students = new ArrayList<>();
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select sid,firstName,lastName,department,email,password from student");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getString("sid"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("department"), rs.getString("email"), rs.getString("password")));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                if (student.getPassword().equals(password)) {
                    studentInSession = student;
                    isLoggedIn = true;
                    System.out.println("LoggedIn");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", studentId);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", "Student");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("studentDetails", studentInSession);

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                    String nextURL = (String) session.getAttribute("nextURL");
                    externalContext.redirect(nextURL);
                    //externalContext.redirect("ProjectorBookingDetails.xhtml");
                }
            }
        }

    }

    /**
     * ************ Navigational Conditions**********************
     */
    public void bookingNavigate() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session.getAttribute("userName") == null) {
            session.setAttribute("nextURL", "ProjectorBookingDetails.xhtml");

            externalContext.redirect("LoginPage.xhtml");
        } else {

            externalContext.redirect("ProjectorBookingDetails.xhtml");
        }
    }

    public ArrayList<BooksAndJournals> searchBooksAndJournals() {
        ArrayList<BooksAndJournals> searchResults = new ArrayList<>();
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select bookid,title,author,volume,pages,isbn,type,checkedoutto from booksandjournals where title like '%" + searchString + "%'");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                searchResults.add(new BooksAndJournals(rs.getString("title"), rs.getString("author"), rs.getString("volume"), rs.getInt("bookId") + "", rs.getInt("pages"), rs.getString("isbn"), rs.getString("type"), rs.getString("checkedoutto")));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        return searchResults;
    }

    public void bookBooksAndJournals(BooksAndJournals selectedBook) throws IOException {
        book = selectedBook;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("StudentBooksAndJournalsBookingDetails.xhtml");
    }

    public String createBooksAndJournalsBookingEntry() throws MessagingException {
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String booking = dateFormat.format(bookingDate);
        String bookingId = "1123";
        FacesContext fc = FacesContext.getCurrentInstance();
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int number = 0;
        try {

            pstmt = conn.prepareStatement("insert into BOOKING (itemId,userId,bookingDate,type,details)values('" + book.bookId + "','" + usr_ID + "','" + booking + "','" + book.type + "','" + book.title + "')");
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("update  booksandjournals set checkedoutto = '" + usr_ID + "' where title='" + book.title + "'");
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("select bookingId,itemId,userId from booking where bookingId=(select max(bookingId) from booking)");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                number = rs.getInt("bookingId");
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        bookingEntry = new Booking(book.bookId, usr_ID);
        bookingEntry.setBookingDate(bookingDate);
        bookingId = number + "";
        generateAndSendEmail("Booking", bookingId);
        return bookingId;

    }

    public ArrayList<Booking> getBookingHistory() throws ParseException {
        ArrayList<Booking> bookingHistory = new ArrayList();
        ConnectionDtls connDtls = DBConnection.getConnection();
        FacesContext fc = FacesContext.getCurrentInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");

        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select bookingId,itemId,bookingDate,details,type from booking where userId ='" + usr_ID + "'");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                //String itemId, String userId, Date bookingDate, String bookingID    

                bookingHistory.add(new Booking(rs.getInt("itemId") + "", usr_ID, rs.getString("bookingDate"), rs.getInt("bookingId") + "", rs.getString("type"), rs.getString("details")));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        return bookingHistory;
    }

    public void generateAndSendEmail(String type, String transactionID) throws AddressException, MessagingException {

        FacesContext fc = FacesContext.getCurrentInstance();
        Student student = (Student) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("studentDetails");

        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(student.getEmail()));
        //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@gmail.com"));
        generateMailMessage.setSubject("Greetings from Dream Programs Library..");
        String emailBody = "This email is sent to notify you of your recent " + type + " at the library.<br> Your "
                + type + " Id is: " + transactionID + ". <br> Thankyou, <br> library team";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "YOUR GMAIL IDHERE", "##YOURPASSWORDHERE");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    public void reserveBooksAndJournals(BooksAndJournals selectedBook) throws IOException {
        book = selectedBook;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("StudentReservationDetails.xhtml");
    }

    public String createReservatonEntry() throws MessagingException {

        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String booking = dateFormat.format(bookingDate);
        String bookingId = "1123";
        FacesContext fc = FacesContext.getCurrentInstance();
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int number = 0;
        try {

            pstmt = conn.prepareStatement(
                    "INSERT INTO RESERVATION (userId,type,details,date) values ('" + usr_ID + "','" + book.type + "','" + book.title + "','" + getBookingDate() + "')");
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("select id,userId,type,details,date from reservation where id=(select max(id) from reservation)");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                number = rs.getInt("id");
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        bookingEntry = new Booking(book.bookId, usr_ID);
        bookingEntry.setBookingDate(bookingDate);
        bookingId = number + "";
        generateAndSendEmail("Reservation", bookingId);
        return bookingId;
    }

    public ArrayList<Reservation> geReservations() throws ParseException {
        ArrayList<Reservation> reservations = new ArrayList();
        ConnectionDtls connDtls = DBConnection.getConnection();
        FacesContext fc = FacesContext.getCurrentInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");

        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select id,date,details,type,email from reservation where userId ='" + usr_ID + "'");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                //String itemId, String userId, Date bookingDate, String bookingID    
//String id, String userId, String type, String details, String email, String date
                reservations.add(new Reservation(rs.getInt("id") + "", usr_ID, rs.getString("type"), rs.getString("details"), rs.getString("email"), rs.getString("date")));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        return reservations;
    }

    public void cancelReservations(Reservation selectedReservation) {
        ConnectionDtls connDtls = DBConnection.getConnection();
        FacesContext fc = FacesContext.getCurrentInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");

        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("delete from reservation where id=" + selectedReservation.id);
            pstmt.executeUpdate();
       
        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

    }
    public String createRequestEntry() throws MessagingException{
     DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
       // String booking = dateFormat.format(bookingDate);
        String bookingId = "1123";
       // FacesContext fc = FacesContext.getCurrentInstance();
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");
        ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int number = 0;
        try {

            pstmt = conn.prepareStatement(
                    "insert into request (title,type,author,cost,userId,status) values ('"+requestedItem.title+"','"+requestedItem.type+"','"+requestedItem.author+"','"+requestedItem.cost+"','"+usr_ID+"','Pending')");
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("select id from request where id=(select max(id) from request)");
            rs = pstmt.executeQuery();
            while (rs.next()) {

                number = rs.getInt("id");
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        bookingId = number + "";
        generateAndSendEmail("Purchase Request", bookingId);
        return bookingId;
    
    }
   public void addnewUser() throws IOException{
   
   ConnectionDtls connDtls = DBConnection.getConnection();
        FacesContext fc = FacesContext.getCurrentInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");

        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt =
 conn.prepareStatement("insert into student (sid,firstName,lastName,department,email,password,isAdmin,firstLogin"
         + ") values ('"+newStudent.getStudentId()+"','"+newStudent.getFirstName()+"','"+newStudent.getLastName()+"','"+
         newStudent.getDepartment()+"','"+newStudent.getEmail()+"','password','false','true')");
            pstmt.executeUpdate();
       
        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("LibrarianAddNewUserConfirmationPage.xhtml");
   }
    public ArrayList<NewItem> getPurchaseRequests() throws ParseException {
        ArrayList<NewItem> requests = new ArrayList();
        ConnectionDtls connDtls = DBConnection.getConnection();
        FacesContext fc = FacesContext.getCurrentInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM-d-YYYY");
        String usr_ID = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName");

        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select id,type,title,author,cost,userId,status from request where status ='Pending'");
            rs = pstmt.executeQuery();
            while (rs.next()) {
             //  String type, String title, String author, String cost, String userId, String status, String id
      requests.add(new NewItem(rs.getString("type"),rs.getString("title"),rs.getString("author"),rs.getString("cost"),rs.getString("userId"),rs.getString("status"),rs.getInt("id")+""));
            }

        } catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }

        return requests;
    }
}
