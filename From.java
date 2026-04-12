import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class From extends JFrame {

    JTextField t1,t2,t3,t4,t5,t6,t7;

    From(){
        setTitle("login form");
        setLayout(null);
        setBounds(200,200,500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void Component(){

        JButton insert,update,delete, retrieve;

        JLabel l1 = new JLabel("Name");
        l1.setBounds(30, 30, 100, 25);
        add(l1);

        t1 = new JTextField();
        t1.setBounds(120, 30, 150, 25);
        add(t1);

        JLabel l2 = new JLabel("Age");
        l2.setBounds(30, 70, 100, 25);
        add(l2);

        t2 = new JTextField();
        t2.setBounds(120, 70, 150, 25);
        add(t2);

        JLabel l3 = new JLabel("Dob");
        l3.setBounds(30, 110, 100, 25);
        add(l3);

        t3 = new JTextField();
        t3.setBounds(120, 110, 150, 25);
        add(t3);

        JLabel l4 = new JLabel("Address");
        l4.setBounds(30, 150, 100, 25);
        add(l4);

        t4 = new JTextField();
        t4.setBounds(120, 150, 150, 25);
        add(t4);

        JLabel l5 = new JLabel("City");
        l5.setBounds(30, 190, 100, 25);
        add(l5);

        t5 = new JTextField();
        t5.setBounds(120, 190, 150, 25);
        add(t5);

        JLabel l6 = new JLabel("State");
        l6.setBounds(30, 230, 100, 25);
        add(l6);

        t6 = new JTextField();
        t6.setBounds(120, 230, 150, 25);
        add(t6);

        JLabel l7 = new JLabel("Country");
        l7.setBounds(30, 270, 100, 25);
        add(l7);

        t7 = new JTextField();
        t7.setBounds(120, 270, 150, 25);
        add(t7);

        insert = new JButton("Insert");
        insert.setBounds(290, 30, 90, 30);
        add(insert);
        update = new JButton("Update");
         update.setBounds(290, 80, 90, 30); add(update);
          delete = new JButton("Delete");
           delete.setBounds(290, 130, 90, 30); add(delete);
            retrieve = new JButton("Retrieve");
             retrieve.setBounds(290, 180, 90, 30); add(retrieve);


        insert.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                insert();
            }
        });
            retrieve.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
              retrieve();
            }
           });
          delete.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
           delete();
         }
     });
     update.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e){
        update();
    }
    });
    }

    public void insert(){
        String url="jdbc:sqlserver://localhost:1433;databaseName=CODE;user=sa;password=2005;encrypt=false;";
        String query="INSERT INTO Data VALUES(?,?,?,?,?,?,?)";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(url);
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, t1.getText());
            ps.setString(2, t2.getText());
            ps.setString(3, t3.getText());
            ps.setString(4, t4.getText());
            ps.setString(5, t5.getText());
            ps.setString(6, t6.getText());
            ps.setString(7, t7.getText());

            ps.executeUpdate();

            System.out.println("Data Inserted Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void retrieve(){

    String url="jdbc:sqlserver://localhost:1433;databaseName=CODE;user=sa;password=2005;encrypt=false;";
    String query="SELECT * FROM Data WHERE Name=?";

    try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(url);

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, t1.getText()); // Name se search

        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            t2.setText(rs.getString(2));
            t3.setText(rs.getString(3));
            t4.setText(rs.getString(4));
            t5.setText(rs.getString(5));
            t6.setText(rs.getString(6));
            t7.setText(rs.getString(7));
        } else {
            System.out.println("Data Not Found");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void delete(){

    String url="jdbc:sqlserver://localhost:1433;databaseName=CODE;user=sa;password=2005;encrypt=false;";
    String query="DELETE FROM Data WHERE Name=?";

    try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(url);

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, t1.getText());

        int rows = ps.executeUpdate();

        if(rows > 0){
            System.out.println("Data Deleted Successfully");

            // Fields clear kar do
            t1.setText("");
            t2.setText("");
            t3.setText("");
            t4.setText("");
            t5.setText("");
            t6.setText("");
            t7.setText("");
        }
        else{
            System.out.println("Data Not Found");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void update(){

    String url="jdbc:sqlserver://localhost:1433;databaseName=CODE;user=sa;password=2005;encrypt=false;";
    
    String query="UPDATE Data SET Age=?, Dob=?, Address=?, City=?, State=?, Country=? WHERE Name=?";

    try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection(url);
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, t2.getText());
        ps.setString(2, t3.getText());
        ps.setString(3, t4.getText());
        ps.setString(4, t5.getText());
        ps.setString(5, t6.getText());
        ps.setString(6, t7.getText());
        ps.setString(7, t1.getText()); 
        int rows = ps.executeUpdate();
        if(rows > 0){
            System.out.println("Data Updated Successfully");
        } else {
            System.out.println("Data Not Found");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                From f=new From();
                f.Component();
                f.setVisible(true);
            }
        });
    }
}