# Sokoban

Sokoban game with level editor  
Project realized on java 17 with openjfx by:  
- Delplanque Arnaud
- Dramaix Pierre
- Van Rossen David

## Description:

- **Level validation:**
  - 1 player
  - At least 1 box and 1 goal
  - Number of boxes and goals must match
  - Only half of the grid can be used

- **New grid:**
  - You can create a new grid with height/width between 10 and 50

- **Save/Open:**
  - You can save/open a level to/from a *.xsb file
  - The file contains a string translation of the grid

- **Change in the grid:**
  - When any change has been made to the grid, a star (*) appears in the program title window
  - If you try to exit/play/open/create a grid, you will be displayed a popup asking if you want to save the changes you made

- **Resize window:**
  - If you resize the program window, the grid will resize itself along with all the cells it contains
  - The cells must always be square

- **Play the grid:**
  - In level edition mode, a "play" button allows you to actually play the grid
  - The button is disabled if there are validation errors
  - The player can be moved with the arrow keys or zqsd
  - The level ends when you click the "finish" button or if you succeed in pushing all the boxes onto goals
  - You can undo (Ctrl-Z) and redo (Ctrl-Y) all movements, but it will cost you points

- **The mushroom:**
  - While you play a level, a "mushroom" button is at the bottom of the window
  - If you click on it, a mushroom is displayed in the grid and you can't move your player
  - If you click on the button again, the mushroom disappears and you can now move your player
  - If you click where the mushroom was, all the boxes that aren't on a goal will be randomly moved to another cell

Enjoy!
