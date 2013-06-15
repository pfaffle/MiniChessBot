MiniChessBot
============

Written by: Craig Meinschein

A program written in Java which can play MinitChess, a variant of MiniChess, which itself is a simplified version of Chess.

This program was written as a term project for the CS 442/552: Combinatorial AI course at Portland State University.

It implements the following major features and algorithms:
* Negamax move search (with alpha-beta pruning).
* Iterative deepening of search depth for the negamax search, as long as it remains within a defined move time limit.
* A Transposition Table (with Zobrist hashing).
* A sophisticated board state evaluation function which takes piece position into account.


All code was written by me, except as otherwise notated in the comments of the source files.
