 import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class Validation2 extends JFrame {

    JTextField tname, tage;
    JDateChooser dateChooser;
    JTable table;
    DefaultTableModel model;

    public Validation2() {
        setTitle("Validation Form");
        setSize(420, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
    }

    public void initialize() {

        JLabel name = new JLabel("Name:");
        name.setBounds(20, 20, 80, 25);
        add(name);

        tname = new JTextField();
        tname.setBounds(100, 20, 165, 25);
        add(tname);

        JLabel age = new JLabel("Age:");
        age.setBounds(20, 60, 80, 25);
        add(age);

        tage = new JTextField();
        tage.setBounds(100, 60, 165, 25);
        add(tage);

        JLabel dob = new JLabel("DOB:");
        dob.setBounds(20, 100, 80, 25);
        add(dob);

        //JDateChooser
        dateChooser = new JDateChooser();
        dateChooser.setBounds(100, 100, 165, 25);
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(dateChooser);

        // Auto Age Calculate
        dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    Date selectedDate = dateChooser.getDate();
                    if (selectedDate == null) return;

                    LocalDate dob = selectedDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    LocalDate today = LocalDate.now();

                    if (dob.isAfter(today)) {
                        JOptionPane.showMessageDialog(null, "Invalid DOB!");
                        return;
                    }

                    int age = Period.between(dob, today).getYears();
                    tage.setText(String.valueOf(age));
                }
            }
        });

        JButton submit = new JButton("Submit");
        submit.setBounds(20, 140, 80, 25);
        add(submit);

        JButton update = new JButton("Update");
        update.setBounds(110, 140, 80, 25);
        add(update);

        JButton delete = new JButton("Delete");
        delete.setBounds(200, 140, 80, 25);
        add(delete);

        JButton retrieve = new JButton("Retrieve");
        retrieve.setBounds(290, 140, 90, 25);
        add(retrieve);

        // Table
        model = new DefaultTableModel(new String[]{"Name", "Age", "DOB"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 180, 360, 100);
        add(scroll);

        //Name Validation
        tname.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (!Character.isLetter(ch) && ch != ' ') {
                    e.consume();
                }
            }
        });

        //Submit
        submit.addActionListener(e -> {
            if (dateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Select DOB!");
                return;
            }

            Vector<String> row = new Vector<>();
            row.add(tname.getText());
            row.add(tage.getText());
            row.add(dateChooser.getDate().toString());
            model.addRow(row);

            insert();
        });

        retrieve.addActionListener(e -> retrieve());
        update.addActionListener(e -> update());
        delete.addActionListener(e -> delete());
    }

    // INSERT
    public void insert() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=Revise;user=sa;password=2005;encrypt=false;"
            );

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO validation1 (name, age, dob) VALUES (?, ?, ?)"
            );

            ps.setString(1, tname.getText());
            ps.setInt(2, Integer.parseInt(tage.getText()));
            ps.setDate(3, new java.sql.Date(dateChooser.getDate().getTime()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Inserted Successfully!");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //RETRIEVE
    public void retrieve() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=Revise;user=sa;password=2005;encrypt=false;"
            );

            PreparedStatement ps = con.prepareStatement("SELECT * FROM validation1");
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("name"));
                row.add(String.valueOf(rs.getInt("age")));
                row.add(rs.getDate("dob").toString());
                model.addRow(row);
            }
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void update() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=Revise;user=sa;password=2005;encrypt=false;"
            );

            PreparedStatement ps = con.prepareStatement(
                "UPDATE validation1 SET age=?, dob=? WHERE name=?"
            );

            ps.setInt(1, Integer.parseInt(tage.getText()));
            ps.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            ps.setString(3, tname.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Updated Successfully!");
           clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //DELETE
    public void delete() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=Revise;user=sa;password=2005;encrypt=false;"
            );

            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM validation1 WHERE name=?"
            );

            ps.setString(1, tname.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Deleted Successfully!");
           clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clearFields() {
    tname.setText("");
    tage.setText("");
    dateChooser.setDate(null);

    
    tname.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Validation2().setVisible(true));
    }
}