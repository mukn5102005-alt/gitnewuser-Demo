 import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// FIX 1: Removed wrong generic type <JCalendar> from class declaration
public class Vadiation extends JFrame {

    // ─── Components ───────────────────────────────────────────────
    JLabel      name, dod, age, address, city, state, country, gender, skills;
    JTextField  nField, aField, sField, coField;
    JComboBox<String> cityCombo;
    JDateChooser dateChooser;    
    JCalendar cal;      // FIX 2: proper type (no generic shadow)
    JRadioButton male, female, other;
    JCheckBox    c, cpp, java, python, js;
    JTextArea    txtArea;
    JScrollPane  aJScrollPanescroll;
    ButtonGroup  bGroup;
    JButton      Insert, Update, Delete, Reteive, save;

    // ─── DB URL (shared constant) ──────────────────────────────────
    // FIX 3: single consistent DB name used everywhere
    private static final String DB_URL =
        "jdbc:sqlserver://localhost:1433;databaseName=Fromdata;" +
        "user=sa;password=2005;encrypt=false;";

    public Vadiation() {
        setTitle("Vadiation");
        setLayout(null);
        // FIX 4: increased frame size so all buttons are visible
        setSize(650, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 
     */
    public void component() {

        // ── Name ──────────────────────────────────────────────────
        name = new JLabel("Name");
        name.setBounds(50, 30, 100, 30);
        add(name);
        nField = new JTextField();
        nField.setBounds(200, 30, 200, 30);
        add(nField);

        // ── Date of Birth ─────────────────────────────────────────
        dod = new JLabel("Date of Birth");
        dod.setBounds(50, 70, 100, 30);
        add(dod);
         dateChooser = new JDateChooser();
       dateChooser.setBounds(200, 70, 200, 30);   // ✅ ye add karo
       dateChooser.setDateFormatString("yyyy-MM-dd");
        add(dateChooser);
     // FIX 5: removed the broken override of add(JDateChooser)

        dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!"date".equals(evt.getPropertyName())) return;
                Date selectedDate = dateChooser.getDate();
                if (selectedDate == null) return;
                LocalDate ld  = selectedDate.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                LocalDate now = LocalDate.now();
                if (ld.isAfter(now)) {
                    JOptionPane.showMessageDialog(null,
                        "Please select a valid (past) date of birth.");
                    return;
                }
                int calculatedAge = Period.between(ld, now).getYears();
                aField.setText(String.valueOf(calculatedAge));
            }
        });

        // ── Age ───────────────────────────────────────────────────
        age = new JLabel("Age");
        age.setBounds(50, 110, 100, 30);
        add(age);
        aField = new JTextField();
        aField.setBounds(200, 110, 200, 30);
        add(aField);

        // ── Address ───────────────────────────────────────────────
        address = new JLabel("Address");
        address.setBounds(50, 150, 100, 30);
        add(address);
        txtArea = new JTextArea();
        aJScrollPanescroll = new JScrollPane(txtArea);
        aJScrollPanescroll.setBounds(200, 150, 200, 80);
        aJScrollPanescroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        aJScrollPanescroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(aJScrollPanescroll);

        // ── City ──────────────────────────────────────────────────
        city = new JLabel("City");
        city.setBounds(50, 250, 100, 30);
        add(city);
        String[] cities = {"Lucknow", "Delhi", "Bihar", "MP"};
        cityCombo = new JComboBox<>(cities);
        cityCombo.setBounds(200, 250, 200, 30);
        add(cityCombo);

        // ── State ─────────────────────────────────────────────────
        state = new JLabel("State");
        state.setBounds(50, 290, 100, 30);
        add(state);
        sField = new JTextField();
        sField.setBounds(200, 290, 200, 30);
        add(sField);

        // ── Country ───────────────────────────────────────────────
        country = new JLabel("Country");
        country.setBounds(50, 330, 100, 30);
        add(country);
        coField = new JTextField();
        coField.setBounds(200, 330, 200, 30);
        add(coField);

        // ── Gender ────────────────────────────────────────────────
        gender = new JLabel("Gender");
        gender.setBounds(50, 370, 100, 30);
        add(gender);
        male   = new JRadioButton("Male");   male.setBounds(200, 370, 70, 30);
        female = new JRadioButton("Female"); female.setBounds(280, 370, 80, 30);
        other  = new JRadioButton("Other");  other.setBounds(370, 370, 70, 30);
        add(male); add(female); add(other);
        bGroup = new ButtonGroup();
        bGroup.add(male); bGroup.add(female); bGroup.add(other);

        // ── Skills ────────────────────────────────────────────────
        skills = new JLabel("Skills");
        skills.setBounds(50, 410, 100, 30);
        add(skills);
        c      = new JCheckBox("C");          c.setBounds(150, 410, 50, 30);
        cpp    = new JCheckBox("C++");        cpp.setBounds(205, 410, 60, 30);
        java   = new JCheckBox("Java");       java.setBounds(270, 410, 60, 30);
        python = new JCheckBox("Python");     python.setBounds(335, 410, 75, 30);
        js     = new JCheckBox("JavaScript"); js.setBounds(415, 410, 100, 30);
        add(c); add(cpp); add(java); add(python); add(js);

        // ── Buttons ───────────────────────────────────────────────
        Insert  = new JButton("Insert");   Insert.setBounds(50,  480, 90, 30);
        Update  = new JButton("Update");   Update.setBounds(150, 480, 90, 30);
        Delete  = new JButton("Delete");   Delete.setBounds(250, 480, 90, 30);
        Reteive = new JButton("Retrieve"); Reteive.setBounds(350, 480, 90, 30);
        save    = new JButton("Save");     save.setBounds(450,   480, 90, 30);
        add(Insert); add(Update); add(Delete); add(Reteive); add(save);

        // ── Action Listeners ──────────────────────────────────────
        Insert.addActionListener(e  -> inset());
        Update.addActionListener(e  -> update());
        Delete.addActionListener(e  -> delete());
        Reteive.addActionListener(e -> retrieve());

        cityCombo.addActionListener(e -> {
            cityboxt();
            fetchdata();
        });

        // ── Key Listeners ─────────────────────────────────────────
        nField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isLetter(e.getKeyChar()) &&
                     e.getKeyChar() != ' ')
                    e.consume();
            }
            public void keyPressed(KeyEvent e)  {}
            public void keyReleased(KeyEvent e) {}
        });

        aField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()))
                    e.consume();
            }
            public void keyPressed(KeyEvent e)  {}
            public void keyReleased(KeyEvent e) {}
        });
    }

     
    // ── Helper: collect gender string ─────────────────────────────
    private String getGender() {
        if (male.isSelected())   return "Male";
        if (female.isSelected()) return "Female";
        if (other.isSelected())  return "Other";
        return "";
    }

    // ── Helper: collect skills string ─────────────────────────────
    private String getSkills() {
        StringBuilder sb = new StringBuilder();
        if (c.isSelected())      sb.append("C ");
        if (cpp.isSelected())    sb.append("C++ ");
        if (java.isSelected())   sb.append("Java ");
        if (python.isSelected()) sb.append("Python ");
        if (js.isSelected())     sb.append("JavaScript ");
        return sb.toString().trim();
    }

    // ── INSERT ────────────────────────────────────────────────────
    public void inset() {
        String query = "INSERT INTO data(name,dob,age,address,city,state,country,gender,skills)" +
                       " VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nField.getText());
            ps.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            ps.setInt(3, Integer.parseInt(aField.getText()));
            ps.setString(4, txtArea.getText());
            ps.setString(5, (String) cityCombo.getSelectedItem());
            ps.setString(6, sField.getText());
            ps.setString(7, coField.getText());
            ps.setString(8, getGender());
            ps.setString(9, getSkills());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Inserted Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Insert Failed: " + e.getMessage());
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────
    // FIX 6: added missing ps.setString(10, ...) for WHERE clause
    public void update() {
        String query = "UPDATE data SET name=?,dob=?,age=?,address=?,city=?," +
                       "state=?,country=?,gender=?,skills=? WHERE name=?";
        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nField.getText());
            ps.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));
            ps.setInt(3, Integer.parseInt(aField.getText()));
            ps.setString(4, txtArea.getText());
            ps.setString(5, (String) cityCombo.getSelectedItem());
            ps.setString(6, sField.getText());
            ps.setString(7, coField.getText());
            ps.setString(8, getGender());
            ps.setString(9, getSkills());
            ps.setString(10, nField.getText()); // ← WHERE name = ?  (was MISSING)
            int rows = ps.executeUpdate();
            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Data Updated Successfully!");
            else
                JOptionPane.showMessageDialog(this, "No record found with that name.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update Failed: " + e.getMessage());
        }
    }

    // ── DELETE ────────────────────────────────────────────────────
    public void delete() {
        String query = "DELETE FROM data WHERE name=?";
        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nField.getText());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Data Deleted Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No record found with that name.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Delete Failed: " + e.getMessage());
        }
    }

    // ── RETRIEVE ──────────────────────────────────────────────────
    // FIX 7: was using wrong DB "CODE" — corrected to DB_URL (Fromdata)
    public void retrieve() {
        String query = "SELECT * FROM data WHERE name=?";
        try (Connection con = DriverManager.getConnection(DB_URL);   // ← fixed DB
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nField.getText());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nField.setText(rs.getString("name"));
                dateChooser.setDate(rs.getDate("dob"));
                aField.setText(String.valueOf(rs.getInt("age")));
                txtArea.setText(rs.getString("address"));
                cityCombo.setSelectedItem(rs.getString("city"));
                sField.setText(rs.getString("state"));
                coField.setText(rs.getString("country"));
                String g = rs.getString("gender");
                if ("Male".equals(g))        male.setSelected(true);
                else if ("Female".equals(g)) female.setSelected(true);
                else                         other.setSelected(true);

                // Skills checkboxes
                String sk = rs.getString("skills");
                if (sk != null) {
                    c.setSelected(sk.contains("C ") || sk.equals("C"));
                    cpp.setSelected(sk.contains("C++"));
                    java.setSelected(sk.contains("Java"));
                    python.setSelected(sk.contains("Python"));
                    js.setSelected(sk.contains("JavaScript"));
                }
                JOptionPane.showMessageDialog(this, "Data Retrieved Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with that name.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Retrieve Failed: " + e.getMessage());
        }
    }

    // ── CITY COMBO populate from DB ───────────────────────────────
    public void cityboxt() {
        String url   = "jdbc:sqlserver://localhost:1433;databaseName=abhi;" +
                       "user=sa;password=2005;encrypt=false;";
        String query = "SELECT ctname FROM city";
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            cityCombo.removeAllItems(); // avoid duplicates on repeated calls
            while (rs.next()) cityCombo.addItem(rs.getString("ctname"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── AUTO-FILL State & Country based on selected city ──────────
    public void fetchdata() {
        String url   = "jdbc:sqlserver://localhost:1433;databaseName=abhi;" +
                       "user=sa;password=2005;encrypt=false;";
        String query = "SELECT s.stname AS state, c.cname AS country " +
                       "FROM city ci " +
                       "JOIN state s   ON ci.stid = s.stid " +
                       "JOIN country c ON s.cid   = c.cid " +
                       "WHERE ci.ctname = ?";
        try {
            String selectedCity = (String) cityCombo.getSelectedItem();
            if (selectedCity == null) return;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection con = DriverManager.getConnection(url);
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, selectedCity);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    sField.setText(rs.getString("state"));
                    coField.setText(rs.getString("country"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Clear all fields ──────────────────────────────────────────
    private void clearFields() {
        nField.setText("");
        aField.setText("");
        txtArea.setText("");
        sField.setText("");
        coField.setText("");
        dateChooser.setDate(null);
        bGroup.clearSelection();
        c.setSelected(false);
        cpp.setSelected(false);
        java.setSelected(false);
        python.setSelected(false);
        js.setSelected(false);
    }

    // ── Main ──────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Vadiation v = new Vadiation();
            v.component();
            v.setVisible(true);
        });
    }
}