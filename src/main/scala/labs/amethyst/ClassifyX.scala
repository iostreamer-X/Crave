/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

package labs.amethyst

import labs.amethyst.CraveCaseClasses.Classified
import labs.amethyst.CraveCore._
import net.sf.javaml.core.{DefaultDataset, Dataset}

import scala.collection.mutable.ListBuffer

/*ClassifyX is the version used in place of Classify. ClassifyX is advanced and uses more complex algorithms.
* Moreover, it is not an actor. The actor approach was flawed and yielded many design problems.
*/
object ClassifyX {
    /*This overloaded function is to optimize the list of clusters/dataSets it receives.
    * The algorithm applies the cluster function yet again on the already made cluster.
    * The result is a newly made cluster but with better relations. The dataSets begin to contain instances
    * which are closely related and hence the set which contains more than one instances, is copied in a list buffer.
    * This list buffer contains all those sets with more than one instances and hence is naturally classified as "good".
    * Now, those dataSets which don't have multiple instances but rather have single, isolated ones, are packed together
    * and copied to another list buffer, this list buffer is naturally classified into "average".
    * */
    def classyfy(dataSets: Array[Dataset]) = {
        var close=new ListBuffer[Dataset]() //tag=average
        val xclose=new ListBuffer[Dataset]() //tag=good
        dataSets.foreach {
            dataSet =>
                val results = clusterAlgorithms.par.map(_(dataSet))
                val set = results.minBy(_.score).datSet
                val tempDataSet=new DefaultDataset()
                set.foreach{
                    ds=>
                        if(ds.size()>1)
                            xclose+=ds
                        else if(ds.size()==1){
                            tempDataSet.add(ds.get(0))
                        }

                }
                if(tempDataSet.size()>0)
                    close+=tempDataSet
        }
        Classified(xclose.toArray,close.toArray)
    }

    /*This version receives a DataSet with single instances. This function clusterizes the data Set
    * and uses the overloaded function for optimization.Use this method to toggle between xClose and close.*/
    def classyfy(dataSet:Dataset):Array[Dataset]={
        val results = clusterAlgorithms.par.map(_(dataSet)) //This again is one classy code. Calculates result in a parallel manner.
        val bundle = results.minBy(_.score)
        if(bundle.score>5)
            classyfy(bundle.datSet).xClose //change here to work on close.
        else
            bundle.datSet
    }
}