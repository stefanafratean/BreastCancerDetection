package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import java.awt.Window.Type;
import java.awt.GridLayout;

import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Color;

import javax.swing.SwingConstants;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;

import javax.swing.JMenuBar;
import java.awt.Toolkit;

public class RadiologistUi extends JFrame {
	private static final long serialVersionUID = -1047828493188901643L;
	private JPanel contentPane;
	JPanel panel_1;

	/**
	 * Create the frame.
	 */
	public RadiologistUi() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Stefana\\Licenta\\workspace\\CancerDetection\\resources\\pinkRibbon.jpg"));
		setBackground(Color.BLACK);
		 try {
		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 } catch (Exception e) {
		 e.printStackTrace();
		 }

//		try {
//			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//				if ("Nimbus".equals(info.getName())) {
//					UIManager.setLookAndFeel(info.getClassName());
//					break;
//				}
//			}
//		} catch (UnsupportedLookAndFeelException e) {
//			// handle exception
//		} catch (ClassNotFoundException e) {
//			// handle exception
//		} catch (InstantiationException e) {
//			// handle exception
//		} catch (IllegalAccessException e) {
//			// handle exception
//		}
		createMainComponents();

		BufferedImage img = null;
		try {
			img = ImageIO
					.read(new File(
							"E:\\Stefana\\Licenta\\workspace\\CancerDetection\\lib\\PhotoDatabase\\Normal\\A_0027_1.RIGHT_MLO.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image dimg = img.getScaledInstance(256, 512, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		JLabel label = new JLabel(imageIcon);
		panel_1.add(label);

	}

	private void createMainComponents() {
		setTitle("Sistem de diagnosticare a cancerului mamar");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 567, 692);

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("Fi\u0219ier");
		ImageIcon openIcon = new ImageIcon("E:\\Stefana\\Licenta\\workspace\\CancerDetection\\resources\\openIcon.png");
		JMenuItem addRadMenuItem = new JMenuItem("Deschide mamografie", openIcon);
		file.add(addRadMenuItem);

		ImageIcon exitIcon = new ImageIcon("E:\\Stefana\\Licenta\\workspace\\CancerDetection\\resources\\exitIcon.png");
		JMenuItem eMenuItem = new JMenuItem("Ie\u0219ire", exitIcon);
		eMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		file.add(eMenuItem);

		menuBar.add(file);

		JMenu help = new JMenu("Ajutor");
		menuBar.add(help);
		
		JMenu about = new JMenu("Informa\u021Bii");
		menuBar.add(about);
		
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.BLACK);
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.DARK_GRAY);
		panel.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.DARK_GRAY);
		panel_5.add(panel_10);

		JLabel lblDiagnostic = new JLabel("Diagnostic:");
		panel_10.add(lblDiagnostic);
		lblDiagnostic.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDiagnostic.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiagnostic.setForeground(Color.WHITE);

		JLabel lblCancer = new JLabel("S\u0102N\u0102TOS");
		panel_10.add(lblCancer);
		lblCancer.setForeground(new Color(255, 204, 255));
		lblCancer.setFont(new Font("Tahoma", Font.BOLD, 49));

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.DARK_GRAY);
		panel_5.add(panel_9, BorderLayout.NORTH);

		JPanel panel_11 = new JPanel();
		panel_11.setBackground(Color.DARK_GRAY);
		panel_5.add(panel_11, BorderLayout.SOUTH);
		// panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new
		// Component[]{lblCancer}));

		panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_1, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_2, BorderLayout.NORTH);

		JButton btnInv = new JButton("New button");
		btnInv.setVisible(false);
		panel_2.setLayout(new GridLayout(1, 1, 0, 0));
		panel_2.add(btnInv);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_3, BorderLayout.EAST);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_4, BorderLayout.WEST);
		panel_4.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_6.getLayout();
		panel_6.setBackground(Color.DARK_GRAY);
		panel_4.add(panel_6, BorderLayout.NORTH);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(Color.DARK_GRAY);
		panel_6.add(panel_8);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.DARK_GRAY);
		panel_6.add(panel_7);
	}
	
	  protected static ImageIcon createImageIcon(String path) {
//	        java.net.URL imgURL = RadiologistUi.class.getResource(path);
//	        if (imgURL != null) {
	            return new ImageIcon("E:\\Stefana\\Licenta\\workspace\\CancerDetection\\resources\\openIcon.png");
//	        } else {
//	            System.err.println("Couldn't find file: " + path);
//	            return null;
//	        }
	    }

}
