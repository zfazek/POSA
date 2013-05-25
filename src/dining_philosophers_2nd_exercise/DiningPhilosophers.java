package dining_philosophers_2nd_exercise;

import java.util.Random;

// The Dining Philosophers problem is as follows:  A group of philosophers are 
// sitting down at a circular table with food in the middle of the table, and 
// a chopstick on each side of each philosopher.  At any time, they are either 
// thinking or eating.  In order to eat, they need to have two chopsticks.  
// If the chopstick to their left or right is currently being used, they must 
// wait for the other philosopher to put it down.  You may notice that if each 
// philosopher decides to eat at the same time, and each picks up the chopstick
// to his or her right, he or she will not be able to eat, because everyone is
// waiting for the chopstick on their left.  This situation is called 
// “deadlock”.  In this assignment, you will use the Monitor Object pattern in 
// an algorithm to prevent deadlock.
//
// The Java language implements the Monitor Object pattern in its 
// implementation of object-level locking and the 
// synchronized/wait()/notify()/notifyAll() constructs.  You might want to look
// at these resources to see how the pattern is realized by using those Java 
// statements. There should be 5 philosophers and 5 chopsticks, and each
// philosopher should eat exactly five times, and be represented by a Thread.
// The program should create output that looks something like this:
//
// Dinner is starting!
//
// Philosopher 1 picks up left chopstick.
// Philosopher 1 picks up right chopstick.
// Philosopher 1 eats.
// Philosopher 3 picks up left chopstick
// Philosopher 1 puts down right chopstick.
// Philosopher 3 picks up right chopstick.
// Philosopher 2 picks up left chopstick.
// Philosopher 1 puts down left chopstick.
// Philosopher 3 eats.
// Philosopher 2 picks up right chopstick.
// Philosopher 2 eats.
// Philosopher 2 puts down right chopstick.
// Philosopher 2 puts down left chopstick.
// Philosopher 3 puts down right chopstick.
// Philosopher 3 puts down left chopstick.
// …
// Dinner is over!
//
// Use Java's synchronization and object locking to design a simple model of
// the Dining Philosophers problem in Java  and an algorithm to prevent
// deadlock.

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
