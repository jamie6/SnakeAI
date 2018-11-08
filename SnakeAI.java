import java.util.Scanner;
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
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do
        {
            System.out.println("1) Train Snake A.I.");
            System.out.println("2) Test Snake A.I.");
            System.out.println("3) Play Snake");
            System.out.println("4) Exit");
            choice = scanner.nextInt();
            if ( choice == 1 )
            {
                System.out.println("train ai");
                window = new TrainSnakeWindow();
            }
            else if ( choice == 2 )
            {
                System.out.println("test ai");
                System.out.println("not available");
            }
            else if ( choice == 3 )
            {
                System.out.println("play");
                window = new PlaySnakeWindow();
            }
            else if ( choice == 4 )
            {
                System.out.println("exit");
                System.out.println("Good bye.");
            }
        } while ( choice < 1 || choice > 4 );
        
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
