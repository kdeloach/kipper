# kipper

Kipper is a 2D space shooter video game written in Java.

## Screenshots

![](https://github.com/kdeloach/kipper/raw/master/images/status_20140202.png)

### Old screenshots

![](https://github.com/kdeloach/kipper/raw/master/images/status_20070225.png)
![](https://github.com/kdeloach/kipper/raw/master/images/status_20140114.png)
![](https://github.com/kdeloach/kipper/raw/master/images/status_20140124.png)
![](https://github.com/kdeloach/kipper/raw/master/images/status_20140125.png)
![](https://github.com/kdeloach/kipper/raw/master/images/status_20140126.png)

## Controls

* Mouse1 - Fire weapon
* 1-4 - Swtich weapons
* Q - Open upgrade screen to customize weapons
* Shift - Halt ship movement
* Spacebar - Pause

Run the game with the following command:

    > ant
    or
    > java -cp bin/release/kipper.jar kipper.Main

# Game Editing Tools

## Mask tool

To create Polygon masks used for collision detection.

Open the mask tool:

    > ant mtool
    or
    > java -cp bin/release/kipper.jar kipper.tools.MaskTool

![](https://github.com/kdeloach/kipper/raw/master/images/masktool_20140116.png)

## Particle tool

To create particle effects for projectile collisions, entity trails, explosions, and more. This tool allows you to prototype and experiment with particle emitter systems using the embedded scripting language Jython.

*Note: Particle tool has a dependency on Jython 2.5.3.*

### Controls

* F5 - Parse Jython script and update draw panel

Open the particle tool:

    > ant ptool
    or
    > java -cp bin/release/kipper.jar;jython.jar kipper.tools.ParticleTool

### Screenshots

![](https://github.com/kdeloach/kipper/raw/master/images/particletool_20140123.png)
![](https://github.com/kdeloach/kipper/raw/master/images/particletool_20140124.png)
