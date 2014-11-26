import java.awt.EventQueue;
import java.io.IOException;

import controller.Controller;
import model.GameModel;
import view.MainFrame;


public class Main {

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				// model
				GameModel model = new GameModel(9, 9, 10);
				// view
				MainFrame view = null;
				try {
					view = new MainFrame(model);
				} catch (IOException e) {}
				// controller
				Controller controller = new Controller(model, view);
				// add controller to the game
				model.addController(controller);
				
				view.setVisible(true);
			}
		});
	}
}
