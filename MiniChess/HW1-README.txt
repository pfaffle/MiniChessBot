HW 1   : Build a MiniChess Random Player
Author : Craig Meinschein
===========================================

I built my MiniChess player in Java using the Eclipse IDE. I chose to use Java partly because of Bart's
suggestion of using a strongly-typed language, and partially because I wanted more practice writing
code in it.

The structure of the program is as follows:

src/MiniChessPlayer.java : Has a smattering of testing code and the main function which actually starts
                           and plays the chess game.
         src/Square.java : Defines the Square class which is mainly just a data structure that holds
                           x and y coordinates for a space on the chessboard for easy reference.
           src/Move.java : Defines the Move class which is mainly just a data structure that
                           holds a starting and stopping Square object to refer to a move.
          src/State.java : Defines most of the functions that make the chess game work, including
                           maintaining the status of the chess board, what pieces are where, whose
                           move it is, and how many moves have been played. It also returns updated
                           game states after a move has been executed.
          src/Piece.java : Defines a few helper functions for dealing with pieces on the board. Mainly
                           exists for improved code readability.

My primary design considerations were to make my code easy to read and to understand, so that it would be
easy to debug. I included comments describing the purpose of each function, the arguments it accepts, and
the output and return values it produces, for the same reason. I also included in-line comments to describe
the steps of the algorithms I used to make them more clear.

I implemented the MiniChess board as a 2D array. Once again, I sacrificed the possible efficiency gains of
using other data structures like a 1D array or a bitboard in order to make my code easier to understand. It
was easier this way to form a mental model of how my data was represented, and easier to write my code to
access and manipulate it as a result. I went against my normal inclinations to stick to good use of spatial
locality in memory by indexing the array the way I did, in column-major ([x][y]) order, simply because it
made it easier for me to visualize the board as a mathematical graph with (x,y) coordinates. With that said,
I also tried to ensure that I only accessed data in the array through the function getPieceAtIndex (or the
wrapper function getPieceAtSquare), so that if I wanted to change the implementation later, it would be
easier to do so.

I wrote several functions in the State class and other classes which were not - strictly speaking - necessary,
in order to make it clearer what my thought process was and the kinds of things I was trying to check. Some
examples include pieceIsOnMove, indexIsValid, and squareIsValid in the State class, and isWhite and isBlack
in the Piece class. All of these are essentially just small snippets of code that check a square on the board
and perform some comparisons with class variables. Having them in their own functions makes it clearer what
I'm doing and also reduces the chance that I'll make a mistake and omit something important.