Varun Rai
raiv95@csu.fullerton.edu

Movie list app that displays movies by highest rating and most recent. There are two tabs that
each correspond with two separate listviews. These listviews also have their own lists and custom
adapters.

The custom adapter extends ArrayAdapter. When the app first starts up, data is read from the DB and
the listviews are filled. The database stores movie id, movie name, movie date, and movie rating. 

When the user checks ask me later, an intent and pending intent is created. The intent hasExtra
data added to it for remembering the data for the notification. 

When an item is selected from one of the listviews and it gets modifies, both listviews are updated with 
the new/edited item.