package labs.amethyst

/*
 * Copyright (C) Amethyst Labs, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Amethyst Team <team@amethystlabs.org>, December 2014
 */

import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors
import net.sf.javaml.core.Dataset


object CraveCaseClasses {

    case class QueryBundle(datSet: Array[Dataset], score: Double){
        def this(datSet:Array[Dataset]){
            this(datSet,new SumOfSquaredErrors().score(datSet))
        }
    }
    case class Classified(xClose:Array[Dataset],close:Array[Dataset])

}
