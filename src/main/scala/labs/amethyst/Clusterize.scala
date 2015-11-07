/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import akka.actor.{Actor, Props}
import labs.amethyst.CraveCaseClasses.QueryBundle
import labs.amethyst.CraveCore._
import net.sf.javaml.core.DefaultDataset
import rx.lang.scala.Observable
import labs.amethyst.ClassifyX._

/*Clusterize is the actor which receives an array of Double values from bufferDataSet. The actor adds the double value instance
* to its data set and Uses ClassifyX to make optimized clusters from it. Every time a buffer is read, the cluster is made and
* and is passed onto the subscriber of the Observable.*/
object Clusterize {
    val clusterObserver: Observable[QueryBundle] = Observable {
        observer =>
            clusterize = craveSystem.actorOf(Props(new CoreClusterize))
            class CoreClusterize extends Actor {

                val dataSet = new DefaultDataset()
                def receive = {
                    case features: Array[Double] =>
                        features.foreach {
                            f =>
                                dataSet.add(singleFeatureInstance(f))
                        }
                        val classy=classyfy(dataSet)
                        val finalBundle=new QueryBundle(classy)
                        observer.onNext(finalBundle)

                    case _ =>
                }
            }
    }
}