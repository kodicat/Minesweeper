package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class ScorePanel extends JPanel {

	private static final long serialVersionUID = 42L;

	public ScorePanel(BombsComponent bombs, SmileComponent smile,
					BombsComponent timer) {
		
		
		setBackground(new Color(192, 192, 192));
		SpringLayout layout = new SpringLayout();
		
		smile.setPreferredSize(new Dimension(smile.getSize()));
		System.out.println(smile.getSize());
		// constraints for spring layout
		layout.putConstraint(SpringLayout.WEST, bombs, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bombs, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, smile, 5, SpringLayout.EAST, bombs);
		layout.putConstraint(SpringLayout.NORTH, smile, -1, SpringLayout.NORTH, bombs);
		layout.putConstraint(SpringLayout.WEST, timer, 5, SpringLayout.EAST, smile);
		layout.putConstraint(SpringLayout.NORTH, timer, 0, SpringLayout.NORTH, bombs);
		
		layout.putConstraint(SpringLayout.EAST, this, 5 + timer.getWidth(), SpringLayout.WEST, timer);
		layout.putConstraint(SpringLayout.SOUTH, this, 4, SpringLayout.SOUTH, timer);
		
		System.out.println(smile.getSize());
		
		setLayout(layout);
		
		this.add(bombs);
		this.add(smile);
		this.add(timer);
	}
}
