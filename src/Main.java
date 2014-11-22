import java.awt.EventQueue;
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
				MainFrame view = new MainFrame(model);
				// controller
				Controller controller = new Controller(model, view);
				
				view.setVisible(true);
			}
		});
	}
}
