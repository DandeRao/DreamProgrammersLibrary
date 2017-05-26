/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mallikharjuna Rao Dande
 */
@Named(value = "studentBean")
@SessionScoped
public class StudentBean implements Serializable {

    private String studentId;
    private String password;
    private Student studentInSession = new Student();
    boolean isLoggedIn;
    private String oldPassword;
    private String newPassword;
    private String newPassword2;
    

    /**
     * Creates a new instance of StudentEquipment
     */
    public StudentBean() {
    }

    public StudentBean(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }

    public StudentBean(String studentId) {
        this.studentId = studentId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    public void login() throws NullPointerException, IOException {
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
        }catch (SQLException sqle) {
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
                    role = "Librarian";
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", studentId);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", role);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("studentDetails", studentInSession);
                   if(student.getFirstLogin().equals("true")){
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("login", "first");
                   }else{
                   
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("login", "notFirst");
                   }
                }
                }
            }
          String first = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("login");
         
          if(first.equals("first")){
                   FacesContext.getCurrentInstance().getExternalContext().redirect("passwordChange.xhtml");
        }

        //return login;        
    }
public void changePassword() throws IOException{
 ConnectionDtls connDtls = DBConnection.getConnection();
        String retMsg = null;
        Connection conn = connDtls.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
       String role= "Student";
        try {
            pstmt = conn.prepareStatement("update student set password='"+newPassword+"' where sid='"+studentId+"'");
            pstmt.executeUpdate();
             pstmt = conn.prepareStatement("update student set firstLogin='false' where sid='"+studentId+"'");
            pstmt.executeUpdate();
            
        }catch (SQLException sqle) {
            retMsg = sqle.getMessage();
        } finally {
            DBConnection.close(pstmt, conn);
        }
FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session
                = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
 FacesContext.getCurrentInstance().getExternalContext().redirect("passwordChangeSuccess.xhtml");

}
    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session
                = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();

        return "loggedout";
    }
public void navigateAccount() throws IOException{

    FacesContext fc = FacesContext.getCurrentInstance();
        String role = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("role");
       
if(role.equals("Librarian")){
 FacesContext.getCurrentInstance().getExternalContext().redirect("LibrarianLandingPage.xhtml");
}
else{
    
    FacesContext.getCurrentInstance().getExternalContext().redirect("studentLandinPage.xhtml");
}

}
    public void permission() throws IOException {

        if(isLoggedIn==false) {
            System.out.println("*** The user has no permission to visit this page. *** ");
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("LoginPage.xhtml");
        } else {
            System.out.println("*** The session is still active. User is logged in. *** ");
        }
    }
}
