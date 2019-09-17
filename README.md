Micromanager plugin which hijacks the MDA sequence to produce multiple images per acquire and puts them in a montage.
Useful for SIM

TODO:

Add User save setting for control box drop down
Add feature for not recording montages (try SINGLE_PAGE_TIFF mode)
Think of better GUI


Issues:

Currently pressing the Stop SIM Mode button clears all runnables which is not ideal.
    Possible fix could be using the runnable to call an object which changes later