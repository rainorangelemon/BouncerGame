- **Starter** loads data (including picture and brick configuration) into a database, and pick the related data when running the current level. Alos, it deals with user's input, and give them as parametre to the an object of LevelRunning. **LevelRunning** is mainly responsible for the refresh of the screen. Other classes are basically a concept of a image in the screen, which are changed and reseted by **LevelRunning**.

- explains, in detail, how to add new features to your project:
 
 - add new kind of PowerUp: make a new class which extends **PowerUp** class. And insert the *if-else* sentence in the related line you want to add.

 - add new brick. Just remember to load it in loadData method in Starter.java:
    ```
 	  loadPicture(a character which reperesents the brick, a string that shows your brick picture name);
    ```

- Instead of spearating the Vision mode and Controller mode, I merge them together and show them as several lower lcasses of ImageView. I prefer this one, because although there exists a mixture of Vision and Controller, but it is easier to refresh the objects when changes of these objects(such as positions and speed) comes.

- I uses several lower class of PowerUp to avoid the conditional sentence, and this makes the code more extendable.
   
   ```
public final Boolean stickBall() {
		for (PowerUp powerup : powerUpsCaught) {
			if (powerup.stickBall() == true) {
				return true;
			}
		}
		return false;
	}
	```
	and only those powerUps which are responsible for the ball stick can return *true* value in the *stickBall()* method.