/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import java.text.SimpleDateFormat
import java.util.Calendar

import labs.amethyst.Clusterize._
import labs.amethyst.CraveCaseClasses.QueryBundle
import labs.amethyst.CraveCore._
import rx.lang.scala.{JavaConversions, Observable}
import rx.schedulers.Schedulers

import scala.util.Try

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

	def getFrequencyDistribution(query: QueryBundle) = {

		var frequencyDistribution = Map[String, Int]()
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
		}
		frequencyDistribution
	}

	/*The string part of the map ie the key, tells the hour and the value ie the Map
	tells the frequency distribution of the candidate optimum times in that hour.
	For example "8 Map(8.01->3, 8.3-> 1, 8.45 ->7)"*/

	def getResults(query: QueryBundle) = {
		var optimus = Map[String, Map[java.lang.Double, Int]]()
		query.datSet.foreach {
			dataSet =>
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
		optimus
	}

	def ?[A](exec: ((Seq[A]) => Unit))(arg: A*): Unit = {
		clusterObserver
				.observeOn(JavaConversions.javaSchedulerToScalaScheduler(Schedulers.newThread()))
				.subscribe {
			query =>
				val timings = getResults(query)
				timings.foreach {
					hour =>
						val bestTime = Try(hour._2.filter(_._1 > getTimeDouble()).maxBy(_._2)._1)
						println(bestTime + " " + hour._2)
						bestTime.foreach {
							time =>
								isItDone(getTimeDouble() == time).foreach {
									done =>
										exec(arg)
								}
						}

				}

		}

	}

	def getTimeDouble(): Double = {
		new SimpleDateFormat("HH.mm").format(Calendar.getInstance().getTime()).toDouble
	}

	def newThread[A](code: => A): Unit = {
		new Thread {
			override def run() = {
				code
			}
		}.start()
	}

	def isItDone(condition: => Boolean) = {
		Observable[Boolean](
			observer => {
				newThread {
					while (!condition) {
						Thread.sleep(700)
					}
					observer.onNext(true)
				}
			}

		)
	}
}