package com.expeditionfortreasure.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.expeditionfortreasure.logic.GameLogic;

public class FileHandling {

	/**
	* @Author Emil Vikström
	* Class with static methods to save the statehandler for nine men morris game
	*
	*/
	// Saves the file
	public static void saveFile(Context c, GameLogic gl) {
		Log.v("File", "saving");
		String filename = "gamelogic.dat";
		ObjectOutput out = null;

		try {
			out = new ObjectOutputStream(new FileOutputStream(new File(
					c.getFilesDir(), "")
					+ File.separator + filename));

			out.writeObject(gl);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.v("File", "saving done");
		

	}


	// Loads the file
	public static GameLogic loadFile(Context c) {
		Log.v("File", "loading");
		ObjectInputStream input;
		String filename = "gamelogic.dat";
		GameLogic loaded = null;

			try {

				input = new ObjectInputStream(new FileInputStream(new File(
						new File(c.getFilesDir(), "") + File.separator
								+ filename)));

				loaded = (GameLogic) input.readObject();

				input.close();
			} catch (Exception e) {
				CharSequence text = "Something went wrong when loading file"
						+ "\nCreating new board";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(c, text, duration);

				toast.show();
			} 
		// Needed to be here or else the file won't load propebly
			return loaded;
	}

}
