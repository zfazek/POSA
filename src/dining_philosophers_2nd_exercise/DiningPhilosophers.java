package dining_philosophers_2nd_exercise;

import java.util.Random;

class Monitor {

	private boolean free = true;

	public boolean isFree() {
		return free;
	}

	public synchronized void picksUp(String philosopher, String stick) {
		while (! free) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		free = false;
		System.out.format("Philosopher %s picks up %s chopstick.\n", 
				philosopher, stick);
		notifyAll();
	}

	public static void eat(String philosopher) {
		System.out.format("Philosopher %s eats.\n", philosopher);
	}

	public synchronized void putsDown(String philosopher, String stick) {
		free = true;
		System.out.format("Philosopher %s puts down %s chopstick.\n", 
				philosopher, stick);
		notifyAll();
	}

}

class Philosopher implements Runnable {

	private String name;
	private Monitor mLeft;
	private Monitor mRight;
	private int N = 5;

	public Philosopher(String name, Monitor mLeft, Monitor mRight) {
		this.name = name;
		this.mLeft = mLeft;
		this.mRight = mRight;
	}

	@Override
	public void run() {
		for (int i = 0; i < N; i++) {
			synchronized (mRight) {
				mLeft.picksUp(name, "left");
				mRight.picksUp(name, "right");
			}
			delay();
			Monitor.eat(name);
			delay();
			mRight.putsDown(name, "right");
			delay();
			mLeft.putsDown(name, "left");
			delay();
		}
	}

	private void delay() {
		Random r = new Random();
		try {
			Thread.sleep(r.nextInt(500));
		} catch (InterruptedException e) {}
	}
}

// m5 p1 m1 p2 m2 p3 m3 p4 m4 p5 m5
public class DiningPhilosophers {

	public static void main(String[] args) throws InterruptedException {
		Monitor m1 = new Monitor();		
		Monitor m2 = new Monitor();
		Monitor m3 = new Monitor();
		Monitor m4 = new Monitor();
		Monitor m5 = new Monitor();
		Thread p1 = new Thread(new Philosopher("1", m5, m1));
		Thread p2 = new Thread(new Philosopher("2", m1, m2));
		Thread p3 = new Thread(new Philosopher("3", m2, m3));
		Thread p4 = new Thread(new Philosopher("4", m3, m4));
		Thread p5 = new Thread(new Philosopher("5", m4, m5));
		System.out.println("\nDinner is starting!\n");
		p1.start();
		p2.start();
		p3.start();
		p4.start();
		p5.start();
		p1.join();
		p2.join();
		p3.join();
		p4.join();
		p5.join();
		System.out.println("Dinner is over!");
	}

}
