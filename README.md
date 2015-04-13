# Crave

Crave is an autonomous notification system which constantly learns from user activity and is able to post notifications at times which are most favorable. The system is able to listen for updates which include the time of day when user checks phone or uses it the most, and then depending on the frequency a set of time is made which is classified as good for sending a notification.

#Usage

Download the source and edit the Crave.scala to begin.

Your Crave.scala should look like something like this if you want to start the system:

  package labs.amethyst

  import labs.amethyst.CraveCore._

  object Crave extends App {
    Terminal.initialize
    Terminal.?
  }
