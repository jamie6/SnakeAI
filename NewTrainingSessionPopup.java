import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Jamie
 */
public class NewTrainingSessionPopup
{

    JFrame frame;
    JButton okButton, cancelButton;
    JLabel populationJL, hiddenLayersJL, hiddenNodesJL;
    JTextField populationJTF, hiddenLayersJTF, hiddenNodesJTF;
    JPanel panel = new JPanel();

    public NewTrainingSessionPopup()
    {
        frame = new JFrame();

        okButton = new JButton();
        okButton.setText("OK");
        cancelButton = new JButton();
        cancelButton.setText("CANCEL");

        NTSPButtonActionListener ntspbal = new NTSPButtonActionListener();
        okButton.addActionListener(ntspbal);
        cancelButton.addActionListener(ntspbal);

        populationJL = new JLabel();
        populationJL.setText("Population:");
        hiddenLayersJL = new JLabel();
        hiddenLayersJL.setText("Hidden Layers:");
        hiddenNodesJL = new JLabel();
        hiddenNodesJL.setText("Hidden Nodes:");

        populationJTF = new JTextField();
        populationJTF.setText(String.valueOf(TrainSnakeWindow.snakePopulation));
        populationJTF.setPreferredSize(new Dimension(200, 30));
        populationJTF.setMaximumSize(new Dimension(200, 30));
        hiddenLayersJTF = new JTextField();
        hiddenLayersJTF.setText(String.valueOf(Snake.hiddenLayers));
        hiddenLayersJTF.setPreferredSize(new Dimension(200, 30));
        hiddenLayersJTF.setMaximumSize(new Dimension(200, 30));
        hiddenNodesJTF = new JTextField();
        hiddenNodesJTF.setText(String.valueOf(Snake.hiddenNodes));
        hiddenNodesJTF.setPreferredSize(new Dimension(200, 30));
        hiddenNodesJTF.setMaximumSize(new Dimension(200, 30));

        panel.setLayout(new GridLayout(4, 2));
        panel.add(populationJL);
        panel.add(populationJTF);
        panel.add(hiddenLayersJL);
        panel.add(hiddenLayersJTF);
        panel.add(hiddenNodesJL);
        panel.add(hiddenNodesJTF);
        panel.add(cancelButton);
        panel.add(okButton);

        frame.add(panel);
        frame.setTitle("New Training Session");
        frame.setSize(400, 400);
        frame.setAlwaysOnTop(true);
        frame.pack();
        frame.setVisible(true);
    }

    public void addActionListener(ActionListener al)
    {
        okButton.addActionListener(al);
    }

    class NTSPButtonActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getSource().equals(okButton))
            {
                int population = Integer.parseInt(populationJTF.getText());
                int hiddenNodes = Integer.parseInt(hiddenNodesJTF.getText());
                int hiddenLayers = Integer.parseInt(hiddenLayersJTF.getText());

                if (population < 1)
                    population = 1;
                if (hiddenNodes < 1)
                    hiddenNodes = 1;
                if (hiddenLayers < 1)
                    hiddenLayers = 1;

                frame.dispose();

                try
                {
                    Runtime.getRuntime().exec("java SnakeAI " + population + " " +  hiddenNodes + " " + hiddenLayers);
                    
                } catch (IOException ex)
                {
                    Logger.getLogger(NewTrainingSessionPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
                    

                }else if (ae.getSource().equals(cancelButton))
            {
                frame.dispose();
            }
            }
        }
    }
