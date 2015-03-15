package labs.amethyst

import akka.actor.Actor
import labs.amethyst.CraveCore._

import scala.collection.mutable.ListBuffer

/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

/*
* The BufferDataSet actor receives a double value and stores it in a buffer till a threshold is reached.
* After which it sends the list of doubles to the clusterize actor.
*/


class BufferDataSet extends Actor{
    var buffer=new ListBuffer[Double]
    def receive={
        case feature:Double =>
            buffer+=feature

            if(buffer.size%10==0){
                clusterize!buffer.toArray
                buffer=new ListBuffer[Double]
            }

        case _ =>
    }
}
