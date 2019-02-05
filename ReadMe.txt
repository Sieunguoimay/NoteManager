2/5/2019
--------->
	Coming to finish the app and first publishing:
		//- problem: new note/folder shift the reference to the root
		//- problem: stuffs still remain there for several second after open a folder
		//	or delete an object
		//- problem: new note should be laid at the top of the list
		//- problem: blink effect when open folder.
		//- problem: rotating sign of waiting for data tobe loaded
		- problem: trash invisible behind the people bar

		- feature: new note notification on the people's icon
		- feature: notification sound on new note come.
		- feature: once clicked on people's icon having new notification, go directly
			to the folder containing the new note.
		- finishing: icon design.
		- finishing: clicked on the search area: pop up a message say: 
			"Just for decoration purpose"

	--> let's get started.
22/1/2019
---------> Facing the problem with Google API
	->but after a few minute sit down and read the manual.
	things are now clear. 
	-> the problem is: "you will get a screen to select application type(like Android, web application, iOS, Chrome app, PlayStation 4).There you need to select web application(you should not select android because by selecting android you will not get client secret key)"

14/1/2019
--------->To much time during a day spent on this app. :(
	https://www.youtube.com/watch?v=9qcJk9DA4Sg
	
10/1/2019
--------->
	view.setTag()-> this function allows you to attach
	anything to the view. then wherever you can access
	view, you can access your object. :V
	
	->remember to set the view invisible effect 
	on the next notifyDataSetChanged(). and the 
	view at that position of the old view will be 	still invisible

9/1/2019
---------> create RecyclerView for folder region.
	and the 	listener

8/1/2019
---------> Successful implementing RecyclerView. :V just copy it up :V
	https://www.androidhive.info/2016/01/android-working-with-recycler-view/
7/1/2019
--------->
	Create New Note
	Enter Note Dialog
5/1/2019
---------> Enter Folder name
	and Create new Folder

	Next-> Enter Note Dialog

4/1/2019
---------> Problems:
	When the tablet rotates, the folders disappear.
	I think because of the xml file is empty,
	and it is loaded again at that moment.
	