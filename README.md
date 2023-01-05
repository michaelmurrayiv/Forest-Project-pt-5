# CSC 203 Forest Project

This project outputs a "Viewport" that displays part of a virtual world that is created. In the world, various entities spawn and interact with each other. The early parts of this project required refactoring the given base code so it follows the tenets of object-oriented programming while remaining functional. 
  
Later, we added special functionality into the code for when the mouse is clicked inside the Viewport. For every two clicks:

  1. The first click spawns a "zombie" entity that hunts down "dude" entities in the world, and also transforms a nearby tree entity into a warrior that attacks the zombies. Most of the background images around the click area are transformed into lava.
  
  2. The second click spawns a "bomb" entity that animates and then explodes, changing the background images to a “crater” and removing entities in the vicinity (they got "blown up").
