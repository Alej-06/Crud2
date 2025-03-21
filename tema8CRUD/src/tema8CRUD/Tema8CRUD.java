package tema8CRUD;

import java.awt.EventQueue;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tema8CRUD {

	private JFrame frmAgenda;
	private JTable table;
	private JTextField textFieldNom;
	private JTextField textFieldApe;
	private JTextField textFieldDNI;

	boolean comprobarExpReg(String cadena, String patron) {
		Pattern pat=Pattern.compile(patron);
		Matcher mat=pat.matcher(cadena);
		
		if (mat.matches()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tema8CRUD window = new Tema8CRUD();
					window.frmAgenda.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tema8CRUD() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAgenda = new JFrame();
		frmAgenda.setTitle("Agenda");
		frmAgenda.setBounds(100, 100, 782, 453);
		frmAgenda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAgenda.getContentPane().setLayout(null);
		
		DefaultTableModel contactos = new DefaultTableModel();
		contactos.addColumn("Nombre");
		contactos.addColumn("Apellido");
		contactos.addColumn("DNI");
		
		table = new JTable(contactos);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int indice = table.getSelectedRow();
				TableModel modelo = table.getModel();
				
				textFieldNom.setText(modelo.getValueAt(indice, 0).toString());
				textFieldApe.setText(modelo.getValueAt(indice, 1).toString());
				textFieldDNI.setText(modelo.getValueAt(indice, 2).toString());
				
				
			}
		});
		table.setBounds(449, 79, 321, 325);
		frmAgenda.getContentPane().add(table);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(32, 94, 70, 15);
		frmAgenda.getContentPane().add(lblNombre);
		
		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setBounds(32, 192, 70, 15);
		frmAgenda.getContentPane().add(lblApellido);
		
		JLabel lblDni = new JLabel("DNI");
		lblDni.setBounds(32, 289, 70, 15);
		frmAgenda.getContentPane().add(lblDni);

		textFieldNom = new JTextField();		
		textFieldNom.setBounds(120, 92, 242, 19);
		frmAgenda.getContentPane().add(textFieldNom);
		textFieldNom.setColumns(10);
		
		textFieldApe = new JTextField();
		textFieldApe.setColumns(10);
		textFieldApe.setBounds(120, 190, 242, 19);
		frmAgenda.getContentPane().add(textFieldApe);
		
		textFieldDNI = new JTextField();
		textFieldDNI.setColumns(10);
		textFieldDNI.setBounds(120, 287, 242, 19);
		frmAgenda.getContentPane().add(textFieldDNI);
		
		JLabel lblAplicacinCrudSimple = new JLabel("Aplicación CRUD simple");
		lblAplicacinCrudSimple.setFont(new Font("Dialog", Font.BOLD, 23));
		lblAplicacinCrudSimple.setBounds(242, 12, 309, 36);
		frmAgenda.getContentPane().add(lblAplicacinCrudSimple);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(449, 79, 321, 325);
		frmAgenda.getContentPane().add(scrollPane);
		

		
		try {
			Connection con =ConnectionSingleton.getConnection();
			Statement muestra = con.createStatement();
			ResultSet visualizar=muestra.executeQuery("SELECT * FROM contacto");
			contactos.setRowCount(0);
			while(visualizar.next()) {
				Object[] row = new Object [3];
				row[0] = visualizar.getString("nombre");
				row[1] = visualizar.getString("apellido");
				row[2] = visualizar.getInt("dni");
				contactos.addRow(row);
			}
		
		JButton btnGuardar = new JButton("GUARDAR");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldNom.getText().length()==0) {
					JOptionPane.showMessageDialog(frmAgenda, "El nombre está vacío","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldApe.getText().length()==0) {
					JOptionPane.showMessageDialog(frmAgenda, "El apellido está vacío","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldDNI.getText().length()==0) {
					JOptionPane.showMessageDialog(frmAgenda, "El DNI está vacío","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldNom.getText(), "^[a-zA-z]+$")){
					JOptionPane.showMessageDialog(frmAgenda, "El nombre debe ser una cadena de caracteres alfabéticos","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldApe.getText(), "^[a-zA-z]+$")){
					JOptionPane.showMessageDialog(frmAgenda, "El apellido debe ser una cadena de caracteres alfabéticos","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldDNI.getText(), "^[0-9]{8}$")){
					JOptionPane.showMessageDialog(frmAgenda, "El DNI debe ser un número entero de 9 caracteres","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else {
				try {
					Connection con =ConnectionSingleton.getConnection();
					PreparedStatement insCont = con.prepareStatement("INSERT INTO contacto (nombre,apellido,dni) VALUES (?,?,?)");
					String nom = textFieldNom.getText();
					String ape = textFieldApe.getText();
					int dni = Integer.parseInt(textFieldDNI.getText());
					insCont.setString(1, nom);
					insCont.setString(2, ape);
					insCont.setInt(3, dni);
					insCont.executeUpdate();
					insCont.close();
					JOptionPane.showMessageDialog(frmAgenda, "Se ha añadido el contacto");
					Statement muestra = con.createStatement();
					ResultSet visualizar=muestra.executeQuery("SELECT * FROM contacto");
					contactos.setRowCount(0);
					while(visualizar.next()) {
						Object[] row = new Object [3];
						row[0] = visualizar.getString("nombre");
						row[1] = visualizar.getString("apellido");
						row[2] = visualizar.getInt("dni");
						contactos.addRow(row);
					}
				}catch (SQLException ex) {
					JOptionPane.showMessageDialog(frmAgenda, ex.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		});
		btnGuardar.setBounds(12, 349, 117, 25);
		frmAgenda.getContentPane().add(btnGuardar);
		
		JButton btnActualizar = new JButton("ACTUALIZAR");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (textFieldNom.getText().length()==0) {
					JOptionPane.showMessageDialog(frmAgenda, "El nombre está vacío","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldApe.getText().length()==0) {
					JOptionPane.showMessageDialog(frmAgenda, "El apellido está vacío","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldDNI.getText().length()==0) {
					JOptionPane.showMessageDialog(frmAgenda, "El DNI está vacío","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldNom.getText(), "^[a-zA-z]+$")){
					JOptionPane.showMessageDialog(frmAgenda, "El nombre debe ser una cadena de caracteres alfabéticos","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldApe.getText(), "^[a-zA-z]+$")){
					JOptionPane.showMessageDialog(frmAgenda, "El apellido debe ser una cadena de caracteres alfabéticos","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldDNI.getText(), "^[0-9]{8}$")){
					JOptionPane.showMessageDialog(frmAgenda, "El DNI debe ser un número entero de 9 caracteres","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else {
				try {
					int codp=table.getSelectedRow();
					TableModel model= table.getModel();
					
					Connection con =ConnectionSingleton.getConnection();
					PreparedStatement updCont = con.prepareStatement("UPDATE contacto SET nombre=? ,apellido=? ,dni=? WHERE dni=?");
					String nom = textFieldNom.getText();
					String ape = textFieldApe.getText();
					int dni = Integer.parseInt(textFieldDNI.getText());
					updCont.setString(1, nom);
					updCont.setString(2, ape);
					updCont.setInt(3, dni);
					updCont.setInt(4, (int) model.getValueAt(codp, 2));
					updCont.executeUpdate();
					updCont.close();
					JOptionPane.showMessageDialog(frmAgenda, "Se ha actualizado el contacto");
					Statement muestra = con.createStatement();
					ResultSet visualizar=muestra.executeQuery("SELECT * FROM contacto");
					contactos.setRowCount(0);
					while(visualizar.next()) {
						Object[] row = new Object [3];
						row[0] = visualizar.getString("nombre");
						row[1] = visualizar.getString("apellido");
						row[2] = visualizar.getInt("dni");
						contactos.addRow(row);
					}
				}catch (SQLException ex) {
					JOptionPane.showMessageDialog(frmAgenda, ex.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		});
		btnActualizar.setBounds(141, 349, 132, 25);
		frmAgenda.getContentPane().add(btnActualizar);
		
		JButton btnBorrar = new JButton("BORRAR");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int codp=table.getSelectedRow();
					TableModel model= table.getModel();
					
					Connection con =ConnectionSingleton.getConnection();
					PreparedStatement updCont = con.prepareStatement("DELETE FROM contacto WHERE codigo=?");
					updCont.setInt(1, (int) model.getValueAt(codp, 2));
					updCont.executeUpdate();
					updCont.close();
					JOptionPane.showMessageDialog(frmAgenda, "Se ha borrado el contacto");
					Statement muestra = con.createStatement();
					ResultSet visualizar=muestra.executeQuery("SELECT * FROM contacto");
					contactos.setRowCount(0);
					while(visualizar.next()) {
						Object[] row = new Object [3];
						row[0] = visualizar.getString("nombre");
						row[1] = visualizar.getString("apellido");
						row[2] = visualizar.getInt("dni");
						contactos.addRow(row);
					}
				}catch (SQLException ex) {
					JOptionPane.showMessageDialog(frmAgenda, ex.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBorrar.setBounds(285, 349, 117, 25);
		frmAgenda.getContentPane().add(btnBorrar);
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(frmAgenda, e.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
		}
	}
}
