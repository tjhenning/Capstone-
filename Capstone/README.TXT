Controls:
AD= Left, Right
S=Crouch
Space=Jump/Air Flutter
Shift=Run, Shoot Projectile
------------------------------------------------------------------------

My capstone is basically just an attempt at making a Mario game. It's a scrolling 2D platformer
with powerups that have projectiles and enemies. It also has infinite randomly generated levels! 
(Plus some handcrafted ones.)


My capstone "synthesizes various concepts" in many ways, especially since it is so complicated. I did a lot
with Graphics2D, KeyListeners, animations/creating sprites, collision, scrolling (enemies keep with
projectiles and ground, some AI (like random level gen), making things modular (I can make a separate
functional enemy with one line of code), superclasses (FlyingEnemy--> Entity--> Shape), and all sorts 
of stuff. I took a little bit of code from StackOverflow, but it was just the code to pause the game and 
to make an Image object.

I don't think I need any pseudocode algorithms?

In order to run it, just run the main method of the GameViewer class. Controls are above, and a tip is
that you can wall jump off the same wall to get height. Also, if you collect a powerup that gets you
to the level that you are currently at (i.e. getting a mushroom as big mario in Mario Bros) you will
go to the next powerup stage. There's also an easter egg if you get to a high enough level!