package ping_pong_1st_exercise;

/*
 * You are to design a simple Java program where you create two threads, 
 * Ping and Pong, to alternately display “Ping” and “Pong” respectively on the 
 * console.  The program should create output that looks like this:
 * 
 * Ready… Set… Go!
 * 
 * Ping!
 * Pong!
 * Ping!
 * Pong!
 * Ping!
 * Pong!
 * Done!
 * 
 * It is up to you to determine how to ensure that the two threads alternate 
 * printing on the console, and how to ensure that the main thread waits until 
 * they are finished to print: “Done!”  The order does not matter (it could 
 * start with "Ping!" or "Pong!").
 * Consider using any of the following concepts discussed in the videos:
 * ·      wait() and notify()
 * ·      Semaphores
 * ·      Mutexes
 * ·      Locks
 * Please design this program in Java without using extra frameworks or 
 * libraries (you may use java.util.concurrent) and contain code in a single 
 * file.  Someone should be able to run something like “javac Program.java” 
 * and “java Program” and your program should successfully run!
 */

public class PingPong {

	public static final int N = 3;

	public static void main(String[] args) throws InterruptedException {
		Monitor m = new Monitor();
		Thread ping = new Thread(new Ping(m));
		Thread pong = new Thread(new Pong(m));
		System.out.println("Ready… Set… Go!\n");
		ping.start();
		pong.start();
		ping.join();
		pong.join();
		System.out.println("Done!");
	}
}

class Monitor {

	private boolean pingTurn = true;

	public synchronized void printPing() {
		while (! pingTurn) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		pingTurn = false;
		System.out.println("Ping!");
		notifyAll();
	}

	public synchronized void printPong() {
		while (pingTurn) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		pingTurn = true;
		System.out.println("Pong!");
		notifyAll();
	}
}

class Ping implements Runnable {

	Monitor m;

	Ping(Monitor m) {
		this.m = m;
	}

	@Override
	public void run() {
		int n = PingPong.N;
		for (int i = 0; i < n; i++) {
			m.printPing();
		}
	}
}
class Pong implements Runnable {

	Monitor m;

	Pong(Monitor m) {
		this.m = m;
	}

	@Override
	public void run() {
		int n = PingPong.N;
		for (int i = 0; i < n; i++) {
			m.printPong();
		}
	}
}

