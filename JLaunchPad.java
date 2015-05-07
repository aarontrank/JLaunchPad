import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Time;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;


public class JLaunchPad {
	int sec = 1000;
	int min = 60 * sec;
	int hr = 60 * min;
	int delay = 20 * min;

	Process javaProcess;
	Timer tm;
	public JLaunchPad() {
		javaProcess = null;
		tm = null;
	}

	public static void main(String[] args) throws Exception{
		new JLaunchPad().launch();
	}

	public void consumeOutput(Process p) {
		InputStream inStream = p.getInputStream();
		InputStreamReader inStreamReader = new InputStreamReader(inStream);
		BufferedReader bReader = new BufferedReader(inStreamReader);
		String line = null;
		try {
			while ( (line = bReader.readLine()) != null) System.out.println(line);
		} catch (Exception e) {
			tm.cancel();
			tm = new Timer();
			tm.schedule(new DestroyRelaunchTask() , 0);
		}
	}

	public void launch(){
		try {
			javaProcess = Runtime.getRuntime().exec("java -classpath accjwrap.jar;.;mysql.jar Superbad McLovin12Four5 arg2");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Process launched @" + (new Date()).toString());
		tm = new Timer();
		tm.schedule(new DestroyRelaunchTask() , delay);
		consumeOutput(javaProcess);
	}

	public void destroyRelaunch(){
		if (javaProcess!=null) {
			javaProcess.destroy();
			System.out.println("Process destroyed @" + (new Date()).toString());
		}
		launch();
	}

	public class DestroyRelaunchTask extends TimerTask {
		public void run() {
			destroyRelaunch();
		}
	}
}
