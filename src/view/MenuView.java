package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.GameModel;
import controller.Controller;

public class MenuView extends JMenuBar {
	private static final long serialVersionUID = 42L;
	MainFrame view;
	
	public MenuView(MainFrame view){
		this.view = view;
		
		JMenuItem novice;
		JMenuItem buvshyi;
		JMenuItem expert;
		novice = new JMenuItem(new NewGameAction("Новичок", 9, 9, 10));
		buvshyi = new JMenuItem(new NewGameAction("Бывалый", 16, 16, 40));
		expert = new JMenuItem(new NewGameAction("Эксперт", 30, 16, 99));
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(novice);
		fileMenu.add(buvshyi);
		fileMenu.add(expert);
		
		add(fileMenu);
	}
	
	public class NewGameAction extends AbstractAction {
		private static final long serialVersionUID = 42L;
		
		private final int width;
		private final int height;
		private final int bombs;

		public NewGameAction(String name, int width, int height, int bombs) {
			super(name);
			this.width = width;
			this.height = height;
			this.bombs = bombs;
		}
		
		public void actionPerformed(ActionEvent event) {
			view.dispose();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					GameModel model = new GameModel(width, height, bombs);

					MainFrame view = null;
					try {
						view = new MainFrame(model);
					} catch (IOException e) {}

					Controller controller = new Controller(model, view);

					model.addController(controller);

					view.setVisible(true);
				}
			});
		}
	}
}
