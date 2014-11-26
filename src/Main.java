import java.awt.EventQueue;
import java.io.IOException;

import controller.Controller;
import model.GameModel;
import view.MainFrame;


public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GameModel model = new GameModel(9, 9, 10);
				
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
