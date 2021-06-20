
public class Main implements Runnable{
	
	GUI gui = new GUI();
	//static SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer("C:\\Users\\geekSA67\\Videos\\youtube\\audios\\video game audio\\back.wav"); 

	public static void main(String[] args) {
		//audioPlayer.filePath = "C:\\Users\\geekSA67\\Videos\\youtube\\audios\\background audio\\anb.mp3";
		//audioPlayer.play(); 
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
