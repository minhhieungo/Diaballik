
This project is a part of the OOP class at INSA de Rennes.

## Layout

`diaballik-backend` is back-end of the application. It contains the Java code of the server.
It is in charge with providing a REST API and to manage games.

`diaballik-frontend` is the Angular front-end (user interface) of the application.

## For the Web front-end (Angular, https://angular.io/guide/quickstart)

# On your personal computer only (apt-get for Debian)
sudo dnf install npm docker maven
Java 8 must be used.

# for installing angular
sudo npm install -g @angular/cli 

# Rule of the game
Each opponent has a team composed of seven players, one of which has a ball. 

The goal of the game is to bring a player to the opponent's side with the ball on it.

Opponents take turns and must take three actions during their turn. Then they have to click end turn to end their turn. 

Possible actions are move and pass the ball.

A move is to move one player just one horizontal or vertical space.

The player with the ball can throw his ball to another player of his team who is located in the vertical line, the horizontal line or a diagonal line and if no opposing player is in the way. As in handball, the player with the ball cannot move while he has the ball. So to move, the ball holder must pass to one of his partners. Moves can only be made in the horizontal or vertical direction and only if the destination is unoccupied. Moving diagonally requires two moves, since one must move horizontally, then vertically, in two stages (as such one cannot move diagonally between two pieces).

UNDO is to undo the previous action.

REDO is to undo the undo action.

END TURN is to finish your turn. Once you end your turn you can't undo your action.

=======
This project is a part of the OOP class at INSA de Rennes.

## Layout

`diaballik-backend` is back-end of the application. It contains the Java code of the server.
It is in charge with providing a REST API and to manage games.

`diaballik-frontend` is the Angular front-end (user interface) of the application.

## For the Web front-end (Angular, https://angular.io/guide/quickstart)

# On your personal computer only (apt-get for Debian)
sudo dnf install npm docker maven
Java 8 must be used.

# for installing angular
sudo npm install -g @angular/cli 

# Rule of the game
Each opponent has a team composed of seven players, one of which has a ball. 

The goal of the game is to bring a player to the opponent's side with the ball on it.

Opponents take turns and must take three actions during their turn. Then they have to click end turn to end their turn. 

Possible actions are move and pass the ball.

A move is to move one player just one horizontal or vertical space.

The player with the ball can throw his ball to another player of his team who is located in the vertical line, the horizontal line or a diagonal line and if no opposing player is in the way. As in handball, the player with the ball cannot move while he has the ball. So to move, the ball holder must pass to one of his partners. Moves can only be made in the horizontal or vertical direction and only if the destination is unoccupied. Moving diagonally requires two moves, since one must move horizontally, then vertically, in two stages (as such one cannot move diagonally between two pieces).

UNDO is to undo the previous action.

REDO is to undo the undo action.

END TURN is to finish your turn. Once you end your turn you can't undo your action.


