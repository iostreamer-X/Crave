# Crave

Crave is an autonomous notification system which constantly learns from user activity and is able to post notifications at times which are most favorable. The system is able to listen for updates which include the time of day when user checks phone or uses it the most, and then depending on the frequency a set of time is made which is classified as good for sending a notification.

#Usage

Download the source and edit the Crave.scala to begin.

Your Crave.scala should look like something like this if you want to start the system:
  ```scala
  package labs.amethyst

  import labs.amethyst.CraveCore._

  object Crave extends App {
    Terminal.initialize
    Terminal.?
  }
  ```
The system is able to listen through a TCP connection on port 2148. What exactly it listens to is just numbers of Double data type. This standard is chosen because a double can very well represent time of day as hh.mm and various other types data points. The server is initialized through 
  ```scala
  Terminal.initialize
  ```
  
To test the system you can send values through the standard TCP way by constructing a TCP client or you can directly test it from code by making a list of values and sending it to the system like this:
  ```scala
  object Crave extends App {
    Terminal.initialize
    Terminal.?
    var testData = List(9.0, 9.1, 9.2, 8.2, 10.11, 10.19, 10.42, 9.3, 8.4, 8.14)
    testData.foreach(i => testData = testData.::(i + 12))		
    testData.foreach(bufferDataSet !)
  }
  ```
