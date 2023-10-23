# WRD-JavaRayCaster

Welcome to the ray caster project, titled William Robinson's Dungeon escape (or WRD for short)!

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

project background info:

This was a project designed to explore the basics of the implementations of 3-dimensions in code. From learning how to use java's built in UI system, to solving linear algebra problems, to recursive algorithms, this project was a massive learning experience. The code is quite complex and suboptimal; it was written by myself, along with conceptual help from my good friend Evan Rabinovich, at a time when neither of us were extremely experienced. The extent of our programming knowledge was what was taught to us in AP Computer Science A, and whatever random things we had learned on the internet. Because of this, many techniques were used that were NOT good, and are recognizably wrong and ineficient. Some mistakes include having almost no variable privacy (to avoid mutators and accessors, a fault of laziness), poor variable naming, extremely poor memory usage and tracking, overuse of tracking variables, and countless other small things. Many of these mistakes stemmed from running on a "first solution basis", using only the first solution we found to solve problems and nothing more. We also were on a time constraint, and tried focussing less on optimization and readable code, and more on having a cool game to show the class at the end of the school year. All in all, while the project is somewhat polished in its frontend, its backend is rough and needs work.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

functionality:

this project was designed to have a lot of functionality and useful features. For starters, the backend features. 
1. The code is designed to handle a map file, taking in any large array of numbers seperated by spaces in a txt file and reading it into the game. There are some limitations, the map must be of rectangular shape (one row of numbers cannot have more numbers than another row), and all numbers must correspond to texture      images stored in the TextureManager class. These textured images can be changed, and any amount can be added or removed, so long as the array is resized in the code properly.
2. Any images can be changed in the game, by manually changing their path files in the code. This means the wall images can be changed, entity images and animations, and so on. I would not recommend changing animated images though because the code is so poorly structured that changing the number of images in an            animation would require changing up to 5 or 6 other random variables.
3. Sounds can also be directly modified by changing the paths and array locations of the sound files in the "Sound" java file.
in terms of the front end, there are many features:
1. there are multiple screens, including a loading screen, game screen, paused screen, death screen, and win screen. Switching between them is intuitive and occurs during gameplay
2. while in game, there is:
      1. a health bar
      2. numbers representing the current selected weapon
      3. current clips ammo, along with backup ammo
      4. your players current score
      5. the current round
      6. the number of enemies left in the round
3. there are doors that must be opened to progress in the game, and can only be opened after completing progressively difficult rounds. These doors lead to seperate "rooms" where new enemies spawn
4. animated enimies with death animations, running animations, and hit animations
5. multiple weapons, picked up progressively throughout play
6. hearts that can be picked up to regenerate health
7. boss fight!!!!
      1. the boss is "William Robinson", based off of the name of the boss assett when downloaded online (William Robinson is the artist)
           1. ^^the only assett in the game is the boss, an animated monster designed by William Robinson, who can be found here: https://www.artstation.com/williamrobinson/albums/all
      2. the boss spawns enemies periodically
      3. boss shoots slime at the player periodically
      4. Killing the boss requires a sequence of dealing damage and unlocking his next health bar by picking up health orbs
      5. the boss room contains 3 regen hears, and one final weapon, the zap
      6. zaps kill all enemies within a certain radius when used, they have a relatively long cooldown, and introduce an awesome animation of lightning striking each enemy
         once killed, the boss dies along with all enemies and the win screen is presented!

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

project contributers and roles:
me - designed all code. Had input on gameplay choices and some art.
Evan Rabinovich - helped with solving coding problems functionally (problem solving together with graph paper) and designed game logic and functionality. Oversaw my code to make sure gameplay was as intended.
Alan (not sure of last name) - designed almost all art and assetts
