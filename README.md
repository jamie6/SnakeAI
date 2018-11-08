# SnakeAI
Senior Project. This is an application that uses an A.I. to learn how to play the game Snake. It uses neural networks to make decisions
and it uses genetic algorithm to learn. 

## Rules of Snake
Eat food, don't crash into walls, and don't crash into self. 

## Compile and Run Instructions
Enter into command prompt:
> javac *.java  
> java SnakeAI  

## The user is presented with 4 options:
1. Train Snake A.I.
--Generates 100 snakes each with a randomly generated neural network. After many generations the snakes will eventually learn how to play
the game snake. 

2. Test Snake A.I.
--This option allows the user to test their previously saved neural networks. Just press Open button, click on neural network txt file
and press start button.

3. Play Snake
--Allows user to play the game Snake. Press start button to play.

4. Exit
--Terminates the application.


## Report
### Neural Network
<p>For my project I created an A.I. that could learn how to play the game Snake. I did this using neural networks and genetic algorithm.
The type of neural network I used for this project is called a feed forward neural network. This means that the data being fed into the
neural network can only move forward in one direction. The data moves in one direction through three layers called the input layer, 
hidden layer, and output layer. Also, it is good to note that the hidden layer can be comprised of many layers. I have found that the
number of hidden layers can affect the convergence of the neural network. For example, having too many hidden layers can cause the neural
network to give out less varied output with no matter what input it receives. This can cause a snake to make the same choice regardless of 
the situation it is in. I used only one hidden layer.<p/>
<p>Another thing that can affect the convergence is the number of nodes that are in the input layer and hidden layer. Having too few
nodes may allow your neural network to not converge. For my neural network, I used 16 nodes for the input layer and 32 nodes for the 
hidden layer. I tried many different number of nodes for the hidden layer but 32 seemed to work the best. For the input layer, there is
a more logical reason why I chose 16 nodes and that number is determined by what my snake A.I. can see. My snake can see in eight
directions: north, south, east, west, north east, north west, south east, and south west. And it can only see two different things:
walls and food. So when my snake A.I. is looking, it can see walls in 8 directions and it calculates the distance from its head
to the wall. It does a similar thing with food except with food it does not calculate distance, it just determines whether there is
food in one of the eight directions. If there is food return a 1, if there no food return a 0. This data is stored in an array to be
passed to the neural network as input.<p/>
<p>When the neural network receives the input as an array, it converts it into a column matrix. From there I am able to normalize the
data by inversing it. The next thing I do is create a new matrix by copying the data from the column matrix and adding an extra row. The
element in the new row will be set to 1. This is called a "dummy input." After that, the data in this matrix is ready to travel to the
next layer. Before this data reaches the next layer it must travel through the links that connect the layers together. Each of the links
have weights to them. These weights are stored in matrices and have values from -1 to 1. A matrix multiplication is applied to the
weight matrix representing the links between the input layer and hidden layer and the column matrix with dummy input. After this is done,
an activation function must be applied to the resulting matrix. The activation function used in my project is called the sigmoid function.
This process repeats until the data has reached the output layer. The output layer will output a column matrix with 3 values.<p/>
<p>The reason why the output layer only outputs a column matrix with 3 values is because the output layer only has 3 nodes. Each node
represents the actions the snake A.I. can make: go left, go right, and go straight. The A.I.'s action will be determined by which node
has the greatest value.<p/>

### Genetic Algorithm
<p>Initially when the snake A.I. are being trained, the application will generate 100 snakes each with their own randomly generated
neural networks. The neural networks will each have 3 layers, with 16 input nodes, 32 hidden nodes, and 3 output nodes. The values of 
the weights of the links that connect the layers together are randomly generated. Usually these randomly generated snakes will
perform badly and die right away by crashing into a wall or they will live forever by looping in a circle forever. To prevent a snake
from looping in a circle forever, I added a counter that counts the number of steps a snake can take before it dies. This value can be
adjusted while the application is running. As the snakes become more intelligent, the user may want to increase the number of steps it
can take before it dies so it can consume more food.<p/>
<p>When all 100 of the snakes eventually die, their fitnesses are calculated. The fitness function considers the number of steps a snake
takes before it died, how close it was to its food when it died, and how much food it has consumed before it died. A snake's fitness
will benefit from taking maximum number of steps, dying close to its food, and eating food. Eating food is the most valuable thing a 
snake can do to increase its fitness.<p/>
<p>After the fitness is calculated, the snakes will go through the selection process which will determine which snakes will crossover.
The probability of a snake being chosen for crossover is based on its fitness. The greater the fitness, the greater chance it will be 
chosen for crossover. Once two snakes are chosen for crossover, their weight matrices are taken and a random pair of values will be 
generated for each weight matrix. This value will determine where to splice and combine the matrices. This newly spliced together
matrix will be used in the new neural network of the child. After the splices are completed, the new weight matrices are put into the
new neural network and it is then used for the child's neural network. This is done until 50 children are created. The last 50 children
will be created by only allowing the most fittest snake to crossover with itself. Although this creates clones, they still 
exprience the chance of mutations. To mutate the neural network, random values in the weight matrices are chosen and their values 
are incremented or decremented by random values. After the mutation process is finished, the next generation is ready and the process
repeats.<p/>

### GUI
<p>I created the graphical user interface using the components provided by the javax.swing package. My GUI allows the user to 
stop and pause, change the speed, and manually change the maximum number of steps a snake can make before it dies. The neural networks
of high fitness snakes are usually automatically saved into a txt file but the GUI also allows the user to manually save the neural
network of the fittest snake.<p/>
<p>The GUI also provides a table that contains relevant information of the current snakes in the population, such as the length,
amount of food consumed, steps taken, and fitness.<p/>


