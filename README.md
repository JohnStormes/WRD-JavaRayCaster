# WRD-JavaRayCaster

Welcome to the raycaster project, William Robinson's Dungeon Escape (or WRD for short)!

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

project background info:

This was a project designed to explore the basics of the implementations of 3-dimensions in code. From learning how to use Java's built-in UI system, to solving linear algebra problems, to recursive algorithms, this project was a massive learning experience. The code is quite complex and involved. Unfortunately, the code is also suboptimal and somewhat poorly written. It was written by myself, along with conceptual help from my good friend Evan Rabinovich, when neither of us was extremely experienced. The extent of our programming knowledge was what was taught to us in AP Computer Science A, and whatever random things we had learned on the internet. Because of this, many techniques were used that were NOT good and are recognizably wrong and inefficient. Some mistakes include having almost no variable privacy (to avoid mutators and accessors, a fault of laziness), poor variable naming, extremely poor memory usage and tracking, overuse of tracking variables, and countless other small things. Many of these mistakes stemmed from running on a "first solution basis", using only the first solution we found to solve problems and nothing more. We also were on a time constraint and tried focussing less on optimization and readable code, and more on having a cool game to show the class at the end of the school year. While the project is somewhat polished in its front end, its back end is rough and needs work.

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

functionality:

This project was designed to have a lot of functionality and useful features. For starters, the backend features. 
1. The code is designed to handle a map file, taking in any large array of numbers separated by spaces in a Txt file and reading it into the game. There are some limitations, the map must be rectangular (one row of numbers cannot have more numbers than another), and all numbers must correspond to texture images stored in the TextureManager class. These textured images can be changed, and any amount can be added or removed, so long as the array is resized in the code properly.
2. Any images can be changed in the game, by manually changing their path files in the code. This means the wall images can be changed, along with entity images and animations, and so on. I would not recommend changing animated images though because the code is so poorly structured that changing the number of images in an animation would require changing up to 5 or 6 other random variables.
3. Sounds can also be directly modified by changing the paths and array locations of the sound files in the "Sound" java file.

In terms of the front end, there are many features:

1. There are multiple screens, including a loading screen, game screen, paused screen, death screen, and win screen. Switching between them is intuitive and occurs during gameplay
2. While in-game:
      a. a health bar
      b. numbers representing the currently selected weapon
      c. current clips ammo, along with backup ammo
      d. your player's current score
      e. the current round
      f. the number of enemies left in the round
3. Doors must be opened to progress in the game, and can only be opened after completing progressively difficult rounds. These doors lead to separate "rooms" where new enemies spawn
4. Wnimated enemies with death animations, running animations, and hit animations
5. Multiple weapons, picked up progressively throughout gameplay
6. Hearts that can be picked up to regenerate health
7. Boss fight!!!!
      a. The boss is "William Robinson", based on the name of the boss asset when downloaded online (William Robinson is the artist)
           1. ^^ The only asset in the game is the boss, an animated monster designed by William Robinson, who can be found here: https://www.artstation.com/williamrobinson/albums/all
      b. The boss spawns enemies periodically
      c. The boss shoots slime at the player periodically
      d. Killing the boss requires a sequence of dealing damage and unlocking his next health bar by picking up health orbs
      e. The boss room contains 3 regen hears, and one final weapon, the zap
      f. Zaps kill all enemies within a certain radius when used, they have a relatively long cooldown, and introduce an awesome animation of lightning striking each enemy
      g. Once killed, the boss and all spawned enemies die in a grand animation, and the win screen is presented!

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

project contributors and roles:
1. me
      - Designed all code. I provided some input on gameplay choices and made some art.
3. Evan Rabinovich
      - Helped solve coding problems functionally (problem-solving together with graph paper) and designed game logic and functionality.
      - Oversaw my code to make sure gameplay was as intended.
5. Alan (not sure of his last name)
      - Designed almost all art and assets.
