package mjaroslav.bots.kurisu.objects;

public class BreakableThread extends Thread {
	private boolean breaked = false;
	private boolean paused = true;
	private boolean single = false;

	public void breakThread() {
		breaked = true;
	}

	public void pauseThread() {
		paused = true;
	}

	public void playThread() {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isBreaked() {
		return breaked;
	}

	public void work() {

	}

	public void whileWork() {
		single = true;
	}

	@Override
	public void run() {
		work();
		while (!breaked) {
			if (!paused) {
				whileWork();
				if (single)
					breakThread();
			}
		}
	}
}
