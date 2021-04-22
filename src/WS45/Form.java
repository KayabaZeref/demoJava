/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS45;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Admin
 */
public class Form extends JFrame {

    String filename = "File.txt";
    DefaultMutableTreeNode root = null;
    DefaultMutableTreeNode curDepNode = null;
    DefaultMutableTreeNode curEmpNode = null;
    boolean addNewDep = false;
    boolean addNewEmp = false;

    JFrame theForm;

    JLabel departmentDetail = new JLabel("Department Detail");
    JLabel employeeDetail = new JLabel("Employee Detail");

    JLabel deptCode = new JLabel("Dept.code: ");
    JLabel deptName = new JLabel("Dept.name: ");
    JTextField deptCodeField = new JTextField();
    JTextField deptNameField = new JTextField();
    JButton newButton = new JButton("New");
    JButton removeButton = new JButton("Remove");
    JButton addButton = new JButton("Add");

    JLabel empCode = new JLabel("Emp.code: ");
    JLabel empName = new JLabel("Emp.name: ");
    JLabel empSalary = new JLabel("Emp.Salary: ");

    JTextField empCodeField = new JTextField();
    JTextField empNameField = new JTextField();
    JTextField empSalaryField = new JTextField();
    JButton newButton1 = new JButton("New");
    JButton removeButton1 = new JButton("Remove");
    JButton addButton1 = new JButton("Add");

    JButton fileSaveButton = new JButton("Save File");
    JTree tree = new JTree();

    public Form() {
        initContainer();

        root = new DefaultMutableTreeNode("Type of Department");
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);

        loadData();
        TreePath road = new TreePath(root);
        tree.expandPath(road);

    }

    private void loadData() {
        try {
            String S = "";
            StringTokenizer stk;

            FileReader f = new FileReader(filename);
            BufferedReader bf = new BufferedReader(f);

            while ((S = bf.readLine()) != null) {
                S = S.trim();
                boolean isDept = (S.charAt(S.length() - 1) == ':');

                stk = new StringTokenizer(S, "-:,");
                String code = stk.nextToken().trim();
                String name = stk.nextToken().trim();
                if (isDept) {
                    curDepNode = new DefaultMutableTreeNode(new Department(code, name));
                    root.add(curDepNode);
                } else {
                    double salary = Double.parseDouble(stk.nextToken().trim());
                    curEmpNode = new DefaultMutableTreeNode(new Employee(code, name, salary));
                    curDepNode.add(curEmpNode);
                }
            }
            bf.close();
            f.close();
        } catch (Exception e) {
        }
    }

    private void showEmpAndDept() {
        try {
            Department curDep = null;
            Employee curEmp = null;
            if (curDepNode != null) {
                curDep = (Department) (curDepNode.getUserObject());
            }
            if (curEmpNode != null) {
                curEmp = (Employee) (curEmpNode.getUserObject());
            }
            deptCodeField.setText(curDep != null ? curDep.getDeptCode() : "");
            deptNameField.setText(curDep != null ? curDep.getDeptName() : "");
            empNameField.setText(curEmp != null ? curEmp.getEmpName() : "");
            empCodeField.setText(curEmp != null ? curEmp.getEmpCode() : "");
            empSalaryField.setText("" + (curEmp != null ? curEmp.getSalary() : ""));
            deptCodeField.setEditable(false);
            empCodeField.setEditable(false);
        } catch (Exception e) {
        }
    }

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {
        try {
            tree.cancelEditing();
            TreePath path = tree.getSelectionPath();
            if (tree == null) {
                return;
            }
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (path.getLastPathComponent());

            Object selectedObj = selectedNode.getUserObject();

            if (selectedNode == root) {
                this.curDepNode = this.curEmpNode = null;
                deptCodeField.setEditable(false);
                empCodeField.setEditable(false);
            } else {
                if (selectedObj instanceof Department) {
                    curDepNode = selectedNode;
                    curEmpNode = null;
                } else if (selectedObj instanceof Employee) {
                    curEmpNode = selectedNode;
                    curDepNode = (DefaultMutableTreeNode) (selectedNode.getParent());
                }
            }
            showEmpAndDept();
            addNewDep = addNewEmp = false;
        } catch (Exception e) {
        }
    }

    private void departmentNewActonPerformed(ActionEvent mouse) {
        try {
            addNewDep = true;
            deptCodeField.setText("");
            deptNameField.setText("");
            empCodeField.setText("");
            empNameField.setText("");
            empSalaryField.setText("");
            deptCodeField.setEditable(true);
        } catch (Exception e) {
        }
    }

    private void employeeNewActionPerformed(ActionEvent mouse) {
        try {
            addNewEmp = true;
            empCodeField.setText("");
            empNameField.setText("");
            empSalaryField.setText("");
            empCodeField.setEditable(true);
        } catch (Exception e) {
        }
    }

    private void departmentRemoveActionPerformed(ActionEvent mouse) {
        try {
            if (curDepNode.getChildCount() > 0) {
                JOptionPane.showMessageDialog(this,
                        "You have to delete every employee in this department");
            } else {
                int number = JOptionPane.showConfirmDialog(this, 
                        "Delete this department", filename, JOptionPane.YES_NO_OPTION);
                if (number == JOptionPane.YES_OPTION) {
                    root.remove(curDepNode);
                    tree.updateUI();
                }
            }
            curDepNode = curEmpNode = null;
        } catch (Exception e) {
        }
    }

    private void employeeRemoveActionPerformed(ActionEvent mouse) {
        try {
            if (curEmpNode != null) {
                int number = JOptionPane.showConfirmDialog(this, 
                        "Delete this employee ?", filename, JOptionPane.YES_NO_OPTION);
                if (number == JOptionPane.YES_OPTION) {
                    curDepNode.remove(this.curEmpNode);
                    tree.updateUI();
                }
                curEmpNode = null;
            }
        } catch (Exception e) {
        }
    }

    private void departmentAddActionPerformed(ActionEvent mouse) {
        try {
            String code, name;
            code = deptCodeField.getText().trim();
            deptCodeField.setText(code);
            name = deptNameField.getText().trim();
            deptNameField.setText(name);

            if (addNewDep == true) {
                Department dp = new Department(code, name);
                root.add(new DefaultMutableTreeNode(dp));
            } else {
                Department tmp = ((Department) (curDepNode.getUserObject()));
                tmp.setDeptName(name);
                deptCodeField.setEditable(false);

            }
            tree.updateUI();
            addNewDep = false;

        } catch (Exception e) {
        }
    }

    private void employeeAddActionPerformed(ActionEvent mouse) {
        try {
            String code, name;
            double salary;
            code = empCodeField.getText().trim();
            empCodeField.setText(code);
            name = empNameField.getText().trim();
            empNameField.setText(name);
            salary = Double.parseDouble(empSalaryField.getText().trim());
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Do not add negative number !");
                return;
            }
            if (addNewEmp == true) {
                Employee emp = new Employee(code, name, salary);
                curDepNode.add(new DefaultMutableTreeNode(emp));
            } else {
                Employee tmp = (Employee) (curEmpNode.getUserObject());
                tmp.setEmpName(name);
                tmp.setSalary(salary);
            }
            tree.updateUI();
            addNewEmp = false;

        } catch (Exception e) {
        }
    }

    private void saveFileActionPerformed(ActionEvent mouse) {
        try {
            if (root.getChildCount() == 0) {
                return;
            }
            String S;
            FileWriter fw = new FileWriter(filename);
            PrintWriter pw = new PrintWriter(fw);
            Enumeration depts = root.children();

            while (depts.hasMoreElements()) {
                DefaultMutableTreeNode depNode = (DefaultMutableTreeNode) depts.nextElement();
                Department dept = (Department) (depNode.getUserObject());

                S = dept.getDeptCode() + "-" + dept.getDeptName() + ":";
                pw.println(S);

                Enumeration emps = depNode.children();
                while (emps.hasMoreElements()) {
                    DefaultMutableTreeNode empNode = (DefaultMutableTreeNode) emps.nextElement();
                    Employee emp = (Employee) (empNode.getUserObject());
                    S = emp.getEmpCode() + "," + emp.getEmpName() + "," + emp.getSalary();
                    pw.println(S);
                }
            }
            pw.close();
            fw.close();
            int number = JOptionPane.showConfirmDialog(this, "Exit program ?", "Save file", JOptionPane.YES_NO_OPTION);
            if (number == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } catch (Exception e) {
        }
    }

    public void initContainer() {
        
        
        tree.setBounds(20, 20, 280, 320);

        departmentDetail.setBounds(350, 10, 150, 20);
        departmentDetail.setLayout(new FlowLayout(200, 100 , 300));
        departmentDetail.setForeground(Color.RED);
        departmentDetail.setFont(new Font("Tahoma", Font.BOLD, 14));

        employeeDetail.setBounds(350, 180, 150, 20);
        employeeDetail.setForeground(Color.GREEN);
        employeeDetail.setFont(new Font("Tahoma", Font.BOLD, 14));

        deptCode.setBounds(350, 50, 150, 20);
        deptName.setBounds(350, 80, 150, 20);
        empCode.setBounds(350, 220, 150, 20);
        empName.setBounds(350, 250, 150, 20);
        empSalary.setBounds(350, 280, 150, 20);

        deptCodeField.setBounds(450, 50, 200, 20);
        deptNameField.setBounds(450, 80, 200, 20);
        empCodeField.setBounds(450, 220, 200, 20);
        empNameField.setBounds(450, 250, 200, 20);
        empSalaryField.setBounds(450, 280, 200, 20);

        newButton.setBounds(350, 120, 80, 30);
        removeButton.setBounds(450, 120, 100, 30);
        addButton.setBounds(580, 120, 80, 30);

        newButton1.setBounds(350, 320, 80, 30);
        removeButton1.setBounds(450, 320, 100, 30);
        addButton1.setBounds(580, 320, 80, 30);

        fileSaveButton.setBounds(10, 380, 260, 30);

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                departmentNewActonPerformed(e);
            }
        });
        newButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeNewActionPerformed(e);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                departmentAddActionPerformed(e);
            }
        });
        addButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeAddActionPerformed(e);
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                departmentRemoveActionPerformed(e);
            }
        });
        removeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeRemoveActionPerformed(e);
            }
        });
        fileSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileActionPerformed(e);
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                treeMouseClicked(evt);
            }
        }
        );

        theForm = new JFrame();
        theForm.setTitle("Department Form");
        theForm.setVisible(true);
        theForm.setResizable(false);

        theForm.add(departmentDetail);
        theForm.add(employeeDetail);
        theForm.add(deptCode);
        theForm.add(deptName);
        theForm.add(empCode);
        theForm.add(empName);
        theForm.add(empSalary);
        theForm.add(deptCodeField);
        theForm.add(deptNameField);
        theForm.add(empCodeField);
        theForm.add(empNameField);
        theForm.add(empSalaryField);
        theForm.add(newButton);
        theForm.add(removeButton);
        theForm.add(addButton);
        theForm.add(newButton1);
        theForm.add(removeButton1);
        theForm.add(addButton1);
        theForm.add(fileSaveButton);
        theForm.add(tree);
        theForm.setDefaultCloseOperation(EXIT_ON_CLOSE);

        theForm.setSize(700, 500);
       
        Container container = theForm.getContentPane();
        container.setLayout(null);

    }

    public static void main(String[] args) {

        Form form = new Form();

    }
}
