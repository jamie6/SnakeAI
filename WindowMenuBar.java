import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Jamie
 */
public class WindowMenuBar
{
    static JMenuBar getWindowMenuBar(JFrame frame)
    {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileJMenu = new JMenu("Session");
        jMenuBar.add(fileJMenu);
        JMenuItem newTrainingSessionJMI = new JMenuItem( new AbstractAction("New Training Session")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                NewTrainingSessionPopup m = new NewTrainingSessionPopup();
                m.addActionListener((ActionEvent ae) ->
                {
                    frame.dispose();
                });
            }
        }   
        );
        fileJMenu.add(newTrainingSessionJMI);
        
        JMenuItem newTestingSessionJMI = new JMenuItem( new AbstractAction("New Testing Session")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
                try
                {
                    Runtime.getRuntime().exec("java SnakeAI 2");
                    
                } catch (IOException ex)
                {
                    Logger.getLogger(NewTrainingSessionPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        fileJMenu.add(newTestingSessionJMI);
        
        JMenuItem newPlayingSessionJMI = new JMenuItem( new AbstractAction("New Play Session")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
                try
                {
                    Runtime.getRuntime().exec("java SnakeAI 3");
                    
                } catch (IOException ex)
                {
                    Logger.getLogger(NewTrainingSessionPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        fileJMenu.add(newPlayingSessionJMI);
        
        return jMenuBar;
    }
}
