/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import akka.actor.{ActorRef, ActorSystem, Props}
import labs.amethyst.CraveCaseClasses.QueryBundle
import net.sf.javaml.classification._
import net.sf.javaml.classification.bayes.NaiveBayesClassifier
import net.sf.javaml.clustering._
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors
import net.sf.javaml.core.{Dataset, DefaultDataset, DenseInstance, Instance}

import scala.collection.JavaConverters._

object CraveCore {

    /////////////////////////////////////////////////////////Variables Section

    val coreDataSet = new DefaultDataset()
    val classify = "classify"
    val instanceLimit = 4
    val craveSystem = ActorSystem("AmethystLabs")
    val bufferDataSet = craveSystem.actorOf(Props[BufferDataSet])
    val server = craveSystem.actorOf(Props[Server])
    var clusterize: ActorRef = null
    val classifier = craveSystem.actorOf(Props[Classify])
    val clusterAlgorithms = List(farthestFirst)
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

    def aqbc = (dataSet: Dataset) => {
        val clusterCreator = new AQBC()
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


    def predictNaive(time: Double) = {
        val classifier = new NaiveBayesClassifier(false, false, false)
        classifier.buildClassifier(coreDataSet)
        val array = new Array[Double](instanceLimit)
        0.until(instanceLimit).foreach(i => array(i) = time)
        val instance = new DenseInstance(array, "crave")
        classifier.classify(instance)

    }

    def predictKDTreesKNN(time: Double) = {
        val classifier = new KDtreeKNN(4)
        classifier.buildClassifier(coreDataSet)
        val array = new Array[Double](instanceLimit)
        0.until(instanceLimit).foreach(i => array(i) = time)
        val instance = new DenseInstance(array, "crave")
        classifier.classify(instance)

    }

    def predictKNearestNeighbor(time: Double) = {
        val classifier = new KNearestNeighbors(4)
        classifier.buildClassifier(coreDataSet)
        val array = new Array[Double](instanceLimit)
        0.until(instanceLimit).foreach(i => array(i) = time)
        val instance = new DenseInstance(array, "crave")
        classifier.classify(instance)

    }

    def predictMeanFeautureVoting(time: Double) = {
        val classifier = new MeanFeatureVotingClassifier
        classifier.buildClassifier(coreDataSet)
        val array = new Array[Double](instanceLimit)
        0.until(instanceLimit).foreach(i => array(i) = time)
        val instance = new DenseInstance(array, "crave")
        classifier.classify(instance)

    }

    def predictNearestMean(time: Double) = {
        val classifier = new NearestMeanClassifier
        classifier.buildClassifier(coreDataSet)
        val array = new Array[Double](instanceLimit)
        0.until(instanceLimit).foreach(i => array(i) = time)
        val instance = new DenseInstance(array, "crave")
        classifier.classify(instance)

    }

    def predictZeroR(time: Double) = {
        val classifier = new ZeroR
        classifier.buildClassifier(coreDataSet)
        val array = new Array[Double](instanceLimit)
        0.until(instanceLimit).foreach(i => array(i) = time)
        val instance = new DenseInstance(array, "crave")
        classifier.classify(instance)

    }

    def generateRange(dataSets: List[Dataset]): Unit = {
        dataSets.foreach {
            dataSet =>
                val values = dataSet.>.map(k => Double2double(k.get(0))).toArray

        }
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
