/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import akka.actor.Actor
import labs.amethyst.CraveCaseClasses.QueryBundle
import labs.amethyst.CraveCore._
import net.sf.javaml.core.DenseInstance

import scala.collection.mutable.ListBuffer

/*
* The Classify class is deprecated and it's original aim was to receive a QueryBundle and tag the dataSet with
* appropriate classifiers.*/
@deprecated
class Classify extends Actor {
    def receive = {
        case bundle: QueryBundle =>
            coreDataSet.removeAllElements()
            val max = bundle.datSet.maxBy(_.size()).size()
            bundle.datSet.foreach {
                set =>
                    /*The algorithm is such that the dataSet with maximum instances will be tagged as good
                    * and with size less that half of max will be tagged as bad, else as average.
                    */
                    val size = set.size()
                    var tag: String = ""
                    if (size == max)
                        tag = "good"
                    else if (size < max * 1.0 / 2)
                        tag = "bad"
                    else
                        tag = "average"
                    val limit = instanceLimit
                    var list = new ListBuffer[Double]
                    for (j <- 0 until limit) {
                        try {
                            list += set.get(j).get(0)
                        }
                        catch {
                            case e: Exception =>
                                list += list.sum / list.size
                        }
                    }
                    val instance = new DenseInstance(list.toArray, tag)
                    coreDataSet.add(instance)
            }
        case _ =>
    }
}
