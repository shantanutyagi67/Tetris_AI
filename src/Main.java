
public class Main implements Runnable{
	
	GUI gui = new GUI();

	public static void main(String[] args) {
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gui.repaint();
		}
	}

}
