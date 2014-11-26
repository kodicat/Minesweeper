package model;

import java.util.Timer;
import java.util.TimerTask;

import controller.Controller;

public class TimerModel extends Timer {

	private int time;
	
	public TimerModel(Controller controller) {
		time = 0;
		Task tickTask = new Task(controller);
	 	schedule(tickTask, 0L, 1000L);
	}
	
	public void cancelAndCLear() {
		super.cancel();
		time = 0;
	}
	
	public int getTime() {
		return time;
	}
	
	private class Task extends TimerTask {
		private Controller controller;
		
		private Task (Controller controller) {
			this.controller = controller;
		}
		
		@Override
		public void run() {
			// if not more than we can display
			if (time++ < 1000) {
				controller.tick();
			}
		}
	}
}
