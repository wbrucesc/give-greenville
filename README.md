# Give Greenville
My Iron Yard final project built in four weeks.
* **Technologies used:** Java, Spring Boot, PostgreSQL, JPA, Thymeleaf, Bcrypt, and more.
* **Storage:** Amazon S3
* **Deployment:** Heroku

[See it live here!](https://www.givegreenville.com)

## What is Give Greenville?

A community in Greenville, SC where you are free to give anything your heart desires and also ask freely for gifts from others without the exchange of money, exchanging items, or bartering!

## Why did I build it?

I was inspired to build this application after coming across the Give Greenville group on Facebook. The group has a strong following and the group's mission really resonated with me. After looking over the group I realized that building a standalone web application for it would allow me to add features that aren't available on Facebook's post and comment style setup. 

## How does it work?

### Users are able to create new posts choosing between 3 different categories: Ask, Give, and Flash Give. 
* Ask posts are meant for users to be able to reach out to the community to ask for something that they need. 
* Give posts allow users to offer up items that they are trying to give away.
* Flash Give posts are similar to Give posts but are more urgent and have a set time frame that they need to give the item by. 

### Users can interact on posts by entering considerations and comments in hopes for being chosen by the author. Authors can manually select a user or use the "Choose Random" feature to select randomly. 

### Once a user is chosen on a post they are notified by email and able to coordinate the exchange directly with the author of the post.

## Features

* **Consider Me** allows users to add a comment and enter their name in for consideration on a post that they are interested in.
* **Choose Random** allows users to randomly select a user from the list of considerations on a post they created. This helps to ensure fairness and decrease bias towards users when choosing. Users also have the option to manually choose if they prefer.
* **My Posts** allows a user to see, edit, delete, and choose a user from considerations on posts that they created. 
* **Chosen Posts** allow a user to see all of the posts they have been either chosen as a donor (on an Ask post) or a recipient (on a Give post)
* **Show On Map** generates a map of the general area and author's location based on zipcode for a particular post. This gives users an idea how close by they are for the sake of possibly meeting up. 
* **Email Confirmation** Once the author of a post selects a user as a recipient or donor the chosen user is sent an email notifying them. The chosen user can simply click reply and directly email the author in order to coordinate the exchange. 

## Future Goals
* Add functionality to allow users to attach images to their considerations and comments. 
* Utilize the Google Geocoding app further to filter posts by users within a certain range 
* Create more functionality for moderators to flag, edit, and disable posts.
* Make the app's layout more responsive for smaller view sizes. 
