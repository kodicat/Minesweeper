import java.awt.EventQueue;
import controller.Controller;
import model.BoardModel;
import model.SmileModel;
import view.MainFrame;


public class Main {

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				// model
				BoardModel board = new BoardModel();
				SmileModel smile = new SmileModel();
				// view
				MainFrame frame = new MainFrame(board, smile);
				// controller
				Controller controller = new Controller(board, frame);
				
				frame.setVisible(true);
			}
		});
	}
}
