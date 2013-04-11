HW 1   : Build a MiniChess Random Player
Author : Craig Meinschein
===========================================

I built my MiniChess player in Java using the Eclipse IDE. I chose to use Java partly because of Bart's
suggestion of using a strongly-typed language, and partially because I wanted more practice writing
code in it.

The structure of the program is as follows:

src/MiniChessPlayer.java : Has a smattering of testing code and the main function which actually plays
                           the chess game.
         src/Square.java : Defines the Square class which is mainly just a data structure that holds
                           x and y coordinates for a space on the chessboard for easy reference.
           src/Move.java : Defines the Move class which is mainly just a data structure that
                           holds a starting and stopping Square object to refer to a move.
          src/State.java : Defines most of the functions that make the chess game work, including
                           maintaining the status of the chess board, what pieces are where, whose
                           move it is, and how many moves have been played. It also returns updated
                           game states after a move has been executed.

