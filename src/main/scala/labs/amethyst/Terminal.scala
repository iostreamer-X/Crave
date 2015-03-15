/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import labs.amethyst.Clusterize._
import labs.amethyst.CraveCore._
import rx.lang.scala.JavaConversions
import rx.schedulers.Schedulers

object Terminal {

    def initialize = {
        server ! "host"
        clusterObserver
                .observeOn(JavaConversions.javaSchedulerToScalaScheduler(Schedulers.newThread()))
                .subscribe()
    }

    /*This is a really Trippy code and I have no idea what I have done exactly. But, I will try to recall
    * and document. And I hate myself for not doing it when I was writing this code.
    */

    def ?() = {
        clusterObserver
                .observeOn(JavaConversions.javaSchedulerToScalaScheduler(Schedulers.newThread()))
                .subscribe {
            query =>
                var frequencyDistribution = Map[String, Int]()

                //The string part of the map ie the key, tells the hour and the value ie the Map
                //tells the frequency distribution of the candidate optimum times in that hour.
                // For example "8 Map(8.01->3, 8.3-> 1, 8.45 ->7)"
                var optimus = Map[String, Map[java.lang.Double, Int]]()
                query.datSet.foreach {
                    dataSet =>
                        //Calculates the no. of candidate optimum times in an hour.
                        try {
                            val past = frequencyDistribution(dataSet.get(0).classValue().toString)
                            frequencyDistribution += (dataSet.get(0).classValue().toString -> (dataSet.size() + past))
                        }
                        catch {
                            case e: Exception =>
                                frequencyDistribution += (dataSet.get(0).classValue().toString -> dataSet.size())
                        }

                        //I am convinced that I wrote this when I was high.
                        val unique = dataSet.>.toSet
                        unique.foreach {
                            instance =>
                                val tag = instance.classValue().toString
                                val updateMap = try {
                                    val map = optimus(tag) + (instance.get(0) -> dataSet.>.count(_ == instance))
                                    tag -> map
                                }
                                catch {
                                    case e: Exception =>
                                        tag -> Map(instance.get(0) -> dataSet.>.count(_ == instance))
                                }
                                optimus += updateMap
                        }

                }
                optimus.foreach(kv => println(kv._1 + " " + kv._2))
                println()
        }

    }

    def ?(time: Double) = {
        println(predictNaive(time))
        println(predictKDTreesKNN(time))
        println(predictKNearestNeighbor(time))
        println(predictMeanFeautureVoting(time))
        println(predictNearestMean(time))
        println(predictZeroR(time))
    }
}