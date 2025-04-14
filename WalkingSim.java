
import java.io.File;
import java.util.Random;
import processing.core.PImage;

/**
 * The WalkingSim class simulates a GUI where multiple Walker objects can be added, animated, and
 * controlled by mouse and keyboard interactions.
 */
public class WalkingSim {
  private static Random randGen; // Random number generator
  private static int bgColor; // Background color of the program
  private static PImage[] frames; // Array of images representing the animation frames
  private static Walker[] walkers; // Array storing all Walker objects

  /**
   * Initialising the background colour, the walkers' random number generator, loading the *
   * animation frames, and initialising the walker objects at random places are all done by the
   * setup method.
   */
  public static void setup() {
    // Initialize the random number generator to use for randomizing walker positions
    randGen = new Random();
    // Generate a random background color for the simulation
    bgColor = randGen.nextInt();
    // Initialize an array to store the animation frames for the walkers.
    frames = new PImage[Walker.NUM_FRAMES];
    // Create an array of null that holds up to 8 Walker objects.
    walkers = new Walker[8];
    // generating random number for position of walker
    // Initialize a random number of walkers at random positions on the screen
    int numberOfWalkers = randGen.nextInt(walkers.length) + 1;
    for (int i = 0; i < numberOfWalkers; i++) {
      float randomX = randGen.nextFloat() * Utility.width(); // Random x between 0 and screen width
      float randomY = randGen.nextFloat() * Utility.height(); // Random y between 0 and screen
                                                              // height
      walkers[i] = new Walker(randomX, randomY); // Initialize walker with random positions
    }
    // Loads the animation frames for the walkers
    for (int i = 0; i < frames.length; i++) {
      frames[i] = Utility.loadImage("images" + File.separator + "walk-" + i + ".png");
    }
  }

  /**
   * The draw method is responsible for creating the background and updating the position of all the
   * walkers in the simulation.it also makes sure the walkers walk and that they are wrapped to
   * start over at the start of the frame when they reach the end of the frame
   */
  public static void draw() {
    Utility.background(bgColor); // Set background color
    // Loop through all walkers and update their position and animation
    for (int i = 0; i < walkers.length; i++) {
      Walker walker = walkers[i];
      if (walker != null) {
        int currentFrame = walker.getCurrentFrame(); // Get the current animation frame
        float x = walker.getPositionX(); // Get the current x position
        float y = walker.getPositionY(); // Get the current y position
        Utility.image(frames[currentFrame], x, y); // Draw the walker at its position

        // Test if mouse is over the walker
        if (isMouseOver(walker)) {
          System.out.println("Mouse is over walker at index: " + i);
        }
        float movementX = walker.getPositionX() + 3; // Move walker to the right
        // Wrap around screen if walker moves beyond window width
        if (movementX > Utility.width()) {
          movementX = 0;
        }
        // Update the walker position and animation if it is walking
        if (walker.isWalking()) {
          walker.setPositionX(movementX);
          walker.update();
        }
      }
    }
  }

  /**
   * The keyPressed method deals with keyboard input. When the 'a' key is pressed on the keyboard,
   * it adds a new walker at a random position. When the 's' key is pressed on the keyboard , it
   * stops all walkers from walking
   * 
   * @param key the character corresponding to the key that was pressed
   */
  public static void keyPressed(char key) {
    // Add a new walker when 'a' is pressed
    for (int i = 0; i < walkers.length; i++) {
      if (walkers[i] == null) {
        int emptynull = i;
        if (key == 'a') {
          float randomX = randGen.nextFloat() * Utility.width(); // random position x
          float randomY = randGen.nextFloat() * Utility.height(); // random position y
          walkers[emptynull] = new Walker(randomX, randomY);
          break; // Exit after adding walker
        }
      }
    }
    // Stop all walkers when 's' is pressed
    if (key == 's') {
      for (int i = 0; i < walkers.length; i++) {
        if (walkers[i] != null) {
          // Stop the walker by setting its walking state to false
          walkers[i].setWalking(false);
        }
      }
    }
  }

  /**
   * The isMouseOver method checks if the mouse is currently over a walker.
   *
   * @param walker the walker object to check
   * @return true if the mouse is over the walker, false otherwise
   */
  public static boolean isMouseOver(Walker walker) {
    // current frame position
    int currentFrame = walker.getCurrentFrame();
    // Get image for current frame
    PImage currentWalker = frames[currentFrame];
    // width and height of walker image
    float imageWidth = currentWalker.width;
    float imageHeight = currentWalker.height;
    // x,y coordinates of walker image
    float positionX = walker.getPositionX();
    float positionY = walker.getPositionY();
    // mouse pointer location x,y
    int mousePositionX = Utility.mouseX();
    int mousePositionY = Utility.mouseY();
    // range for checking if mouse and walker location is the same
    float range1 = positionX - imageWidth / 2;
    float range2 = positionX + imageWidth / 2;
    float range3 = positionY - imageHeight / 2;
    float range4 = positionY + imageHeight / 2;
    // conditional to see if mouse is in the following ranges
    if (mousePositionX > range1 && mousePositionX < range2 && mousePositionY > range3
        && mousePositionY < range4) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * The mousePressed method detects when the mouse is clicked over a walker and if it is over the
   * walker, that walker starts walking.
   */
  public static void mousePressed() {
    for (int i = 0; i < walkers.length; i++) {
      Walker walker = walkers[i];
      int lowestidx = -1;
      if (walker != null && isMouseOver(walker)) {
        if (lowestidx == -1 || i < lowestidx) {
          lowestidx = i; //// Track the walker closest to the top of the array
        }
      }
      if (lowestidx != -1) {
        walkers[lowestidx].setWalking(true); //// Make the walker start walking
      }

    }

  }

  /**
   * The main method serves as the entry point for the application. It calls the runApplication
   * method to start the simulation program.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    Utility.runApplication();// Start the graphical application
  }
}
