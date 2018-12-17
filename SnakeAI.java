import javax.swing.JOptionPane;
/**
 *
 * @author Jamie
 */
public class SnakeAI
{
    static boolean mainLoopIsRunning = true;
    static int FPS;
    static int ST;
    static Window window;

    public static void main(String[] args)
    {
        String[] options = {"Train Snake A.I.", "Test Snake A.I.", "Play Snake Game"};
        String choice = (String)JOptionPane.showInputDialog(
                            null, "Choose an option: ", "Snake A.I. start menu",
                            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if ( options[0].equals(choice) )
        {
            window = new TrainSnakeWindow();
        }
        else if (options[1].equals(choice))
        {
            window = new TestSnakeWindow();
        }
        else if ( options[2].equals(choice))
        {
            window = new PlaySnakeWindow();
        }
        if ( window != null ) mainLoop();
    }
    
    public static void mainLoop()
    {
        FPS = window.getFPS();
	ST = window.getST();
	long nextTick = System.currentTimeMillis();
	long sleepTime = 0;
	try
	{
	    while ( mainLoopIsRunning )
	    {
		ST = window.getST();
		window.update();
                window.draw();
		nextTick += ST;
		sleepTime = nextTick - System.currentTimeMillis();
		if ( sleepTime >= 0 ) Thread.sleep(sleepTime);
		else {} // running behind
	    }
	}
	catch ( Exception e )
	{
	    System.out.println( e.getMessage() );
            StackTraceElement[] ste = e.getStackTrace();
            System.out.println("stack trace element");
            for ( StackTraceElement s : ste ) System.out.println(s);
	}
    }
}
