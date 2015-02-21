package main;

import java.awt.EventQueue;

import userInterface.RadiologistUi;

public class CancerDiagnosys {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RadiologistUi frame = new RadiologistUi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
