package ui;

import model.*;
import xml.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private PersonList personList;

    public MainFrame() {
        super("JAXB CRUD Application");
        this.personList = XmlHandler.loadXml();
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        JPanel panel = new JPanel();
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(10);
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(5);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Name", "Age"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        refreshTable(tableModel);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            int age;
            try {
                age = Integer.parseInt(ageField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Formato de edad incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            personList.getPersons().add(new Person(name, age));
            XmlHandler.saveXml(personList);
            refreshTable(tableModel);
            nameField.setText("");
            ageField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                personList.getPersons().remove(selectedRow);
                XmlHandler.saveXml(personList);
                refreshTable(tableModel);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona la fila que deseas eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String name = nameField.getText();
                String ageText = ageField.getText();

                if (name.isEmpty() || ageText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Los campos de edad y nombre no pueden estar vacios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Formato de edad incorrecto. Introduce un numero, por favor.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Actualizar el registro seleccionado
                Person person = personList.getPersons().get(selectedRow);
                person.setName(name);
                person.setAge(age);
                XmlHandler.saveXml(personList);
                refreshTable(tableModel);

                // Limpiar los campos
                nameField.setText("");
                ageField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona la fila que deseas actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void refreshTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Person person : personList.getPersons()) {
            tableModel.addRow(new Object[]{person.getName(), person.getAge()});
        }
    }
}
