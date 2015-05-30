/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import akka.actor.{ActorRef, ActorSystem, Props}
import labs.amethyst.CraveCaseClasses.QueryBundle
import net.sf.javaml.clustering._
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors
import net.sf.javaml.core.{Dataset, DenseInstance, Instance}

import scala.collection.JavaConverters._

object CraveCore {

	/////////////////////////////////////////////////////////Variables Section

	val classify = "classify"
	val instanceLimit = 4
	val craveSystem = ActorSystem("AmethystLabs")
	val bufferDataSet = craveSystem.actorOf(Props[BufferDataSet])
	val server = craveSystem.actorOf(Props[Server])
	var clusterize: ActorRef = null
	/////////////////////////////////////////////////////////Variables Section Ends

	/////////////////////////////////////////////////////////Methods Section

	def singleFeatureInstance(feature: Double) = {
		val tag = feature.toInt.toString
		new DenseInstance(Array(feature), tag)
	}

	def farthestFirst = (dataSet: Dataset) => {
		val clusterCreator = new FarthestFirst()
		val sumOfSquaredError = new SumOfSquaredErrors()
		val clusters = clusterCreator.cluster(dataSet)
		val score = sumOfSquaredError.score(clusters)
		QueryBundle(clusters, score)
	}

	def densityBasedSpatial = (dataSet: Dataset) => {
		val clusterCreator = new DensityBasedSpatialClustering()
		val sumOfSquaredError = new SumOfSquaredErrors()
		val clusters = clusterCreator.cluster(dataSet)
		val score = sumOfSquaredError.score(clusters)
		QueryBundle(clusters, score)
	}

	def kMeans = (dataSet: Dataset) => {
		val clusterCreator = new KMeans()
		val sumOfSquaredError = new SumOfSquaredErrors()
		val clusters = clusterCreator.cluster(dataSet)
		val score = sumOfSquaredError.score(clusters)
		QueryBundle(clusters, score)
	}

	def kMedoids = (dataSet: Dataset) => {
		val clusterCreator = new KMedoids()
		val sumOfSquaredError = new SumOfSquaredErrors()
		val clusters = clusterCreator.cluster(dataSet)
		val score = sumOfSquaredError.score(clusters)
		QueryBundle(clusters, score)
	}


	/////////////////////////////////////////////////////////Methods Section Ends

	/////////////////////////////////////////////////////////Classes Section

	implicit class DataSetIterator(dataSet: Dataset) {
		def >(): Iterable[Instance] = {
			dataSet.asScala
		}
	}

	implicit class ClusterPrinter(dataSets: Array[Dataset]) {
		def > {
			dataSets.foreach(println)
		}

	}


	/////////////////////////////////////////////////////////Classes Section Ends
}
