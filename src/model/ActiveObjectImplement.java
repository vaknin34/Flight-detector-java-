package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActiveObjectImplement{

	
	Thread activeThread;
	BlockingQueue<Runnable> queue;
	volatile boolean stop;
	
	public ActiveObjectImplement() {
		super();
		this.queue = new LinkedBlockingQueue<Runnable>();
		this.stop = true;
	}

	
	public void start() {
		if(stop==false)
			return;
		stop=false;
		activeThread = new Thread(()->this.run());
		activeThread.start();
	}

	
	public void stop() {
		this.stop=true;
		this.queue = new LinkedBlockingQueue<Runnable>();
		//this.activeThread.interrupt();
		
	}

	
	public void execute(Runnable r) {
		this.queue.add(r);
	}

	
	public void join() {
		try {
			this.activeThread.join();
		} catch (InterruptedException e) {}
		
	}
	
	public void run() {
		while(!stop) {
			try {
				queue.take().run();
			}catch(InterruptedException e) {}
		}
	}

	
	public void pause() {
		this.stop = true;
		this.activeThread.interrupt();
	}
	public void ClearTasks() {
		queue = new LinkedBlockingQueue<Runnable>();
	}
	

}
