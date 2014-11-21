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
				GameModel board = new GameModel(9, 9, 10);
				// view
				MainFrame frame = new MainFrame(board);
				// controller
				Controller controller = new Controller(board, frame);
				
				frame.setVisible(true);
			}
		});
	}
}
