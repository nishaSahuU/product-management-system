import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class JavaCrud {
	private JPanel Main;
	private JTextField txtName;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton updateButton;
	private JTextField txtpid;
	private JTextField txtPrice;
	private JTextField txtQty;
	private JButton searchButton;
	private JTable table1;
	private JScrollPane table_1;
	private JButton refreshbtn;

	public static void main(String[] args) {
		JFrame frame = new JFrame("JavaCrud");
		frame.setContentPane(new JavaCrud().Main);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	Connection con;
	PreparedStatement pst;
	public JavaCrud() {
		Connect();
        table_load();

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name, price, qty;

				name = txtName.getText();
				price = txtPrice.getText();
				qty= txtQty.getText();

				try {
					pst = con.prepareStatement("insert into products(pname,price,qty)values(?,?,?)");
					pst.setString(1, name);
					pst.setString(2, price);
					pst.setString(3, qty);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Addedddd!!!!!");
					table_load();
					txtName.setText("");
					txtPrice.setText("");
					txtQty.setText("");
					txtName.requestFocus();
				}

				catch (SQLException e1)
				{
					e1.printStackTrace();
				}

			}

			}
		);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String pid = txtpid.getText();
					pst = con.prepareStatement("select pname,price,qty from products where pid = ?");
					pst.setString(1, pid);
					ResultSet rs = pst.executeQuery();

					if(rs.next()==true)
					{
						String pname = rs.getString(1);
						String price = rs.getString(2);
						String qty = rs.getString(3);
						txtName.setText(pname);
						txtPrice.setText(price);
						txtQty.setText(qty);
					}
					else
					{
						txtName.setText("");
						txtPrice.setText("");
						txtQty.setText("");
						JOptionPane.showMessageDialog(null,"Invalid Product No");

					}
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			}
			}
		);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String  name, price, qty,pid;
				name = txtName.getText();
				price = txtPrice.getText();
				qty= txtQty.getText();
				pid= txtpid.getText();
				try {
					pst = con.prepareStatement("update products set pname = ?, price = ?, qty = ? where pid = ?");
					pst.setString(1, name);
					pst.setString(2, price);
					pst.setString(3, qty);
					pst.setString(4, pid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Updateee!!!!!");
					table_load();
					txtName.setText("");
					txtPrice.setText("");
					txtQty.setText("");
					txtName.requestFocus();


				}

				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}

		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String pid;
				pid = txtpid.getText();

				try {
					pst = con.prepareStatement("delete from products where pid = ?");
					pst.setString(1, pid);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record Deleteeeeee!!!!!");
					table_load();
					txtName.setText("");
					txtPrice.setText("");
					txtQty.setText("");
					txtName.requestFocus();
					txtpid.setText("");
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}

		});
		table1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				    DefaultTableModel Dr =(DefaultTableModel)table1.getModel();
					int selectIndex = table1.getSelectedRow();
					txtName.setText(Dr.getValueAt(selectIndex, 1).toString());
					txtPrice.setText(Dr.getValueAt(selectIndex, 2).toString());
					txtQty.setText(Dr.getValueAt(selectIndex, 3).toString());
				txtpid.setText(Dr.getValueAt(selectIndex, 0).toString());

			}
		});

		
	}

	void table_load()
	{
		try{
			pst= con.prepareStatement("select * from products");
			ResultSet rs= pst.executeQuery();
			table1.setModel(DbUtils.resultSetToTableModel(rs));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/gbproducts", "root", "");
		System.out.println("success");
		}
			catch (ClassNotFoundException ex){
				ex.printStackTrace();
			}
		catch (SQLException ex){
			ex.printStackTrace();

		}
		}
	}
